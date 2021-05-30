package cat.copernic.apptareas.UI

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import cat.copernic.apptareas.Datos.DBElementoTarea
import cat.copernic.apptareas.Datos.DBListaTarea
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.R
import cat.copernic.apptareas.databinding.FragmentTareasBinding
import cat.copernic.apptareas.databinding.PopUpCrearTareaBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.android.synthetic.main.pop_up_crear_tarea.view.*

class tareas : Fragment() {

    private lateinit var binding: FragmentTareasBinding
    private lateinit var popu: PopUpCrearTareaBinding
    private val args by navArgs<tareasArgs>()
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("listaTareas")
    private val dbLstas = DBListaTarea()
    private lateinit var lista : ListaTareas
    private val dbElemento = DBElementoTarea()
    private lateinit var tarea: ElementoTarea

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTareasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val identificador = args.listaID

        lista = ListaTareas(0, "", "")
        coleccion.whereEqualTo("idLista", identificador.toString()).limit(1).get().addOnSuccessListener {
            for (document in it) {
                if (it != null){
                    val listaTareasTmp = ListaTareas(
                        (document.data.get("idLista") as String).toInt(),
                        document.data.get("nombre") as String, document.data.get("categoria") as String
                    )
                    println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + listaTareasTmp.nombre)
                    lista = listaTareasTmp

                    vista(listaTareasTmp)

                }
            }
        }
    }

    fun vista(list: ListaTareas){
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + list.idLista)

        var listado = ArrayList<ListaTareas>()

        listado.add(list)

        dbLstas.recuperarContenido()

        println("**>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + dbLstas.ultimoNumero)
        binding.addLista.setOnClickListener {
            popPupEmergente()
        }
    }

    fun popPupEmergente() {

        val identificador = args.listaID


        popu = PopUpCrearTareaBinding.inflate(layoutInflater)
        val popUp = popu.root

        val builder = AlertDialog.Builder(context)
            .setView(popUp)
            .setTitle("Nueva tarea")

        val dialogo = builder.show()


        popUp.btnCreaTarea.setOnClickListener {
            tarea = ElementoTarea(0, popUp.editNombreTarea.toString(), 0, identificador)

            println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + tarea.padre)

            tarea.tarea = popUp.editNombreTarea.text.toString()

            dbElemento.actualizaUltimoNumero(::numeroUltimo)

            //dbElemento.insertar()


            dialogo.dismiss()
        }
    }

    fun numeroUltimo(numero: Int){
        tarea.idElemento = numero + 1
        tarea.posicion = dbElemento.ultimoNumero + 1

        dbElemento.insertar(tarea)

        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + numero)

    }

    fun elementosDeTarea(list : ArrayList<ListaTareas>){

    }
}