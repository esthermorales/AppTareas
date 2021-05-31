package cat.copernic.apptareas.UI

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.apptareas.Datos.DBElementoTarea
import cat.copernic.apptareas.Datos.DBListaTarea
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.R
import cat.copernic.apptareas.UI.RecyclerTareas.TareasAdapter
import cat.copernic.apptareas.UI.ViewListas.ListaTareasAdapter
import cat.copernic.apptareas.databinding.FragmentTareasBinding
import cat.copernic.apptareas.databinding.LayoutElementoBinding
import cat.copernic.apptareas.databinding.PopUpCrearTareaBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.android.synthetic.main.pop_up_crear_tarea.view.*

class tareas : Fragment(), TareasAdapter.OnTareaClic {

    private lateinit var binding: FragmentTareasBinding
    private lateinit var popu: PopUpCrearTareaBinding
    private lateinit var recicler: LayoutElementoBinding
    private val args by navArgs<tareasArgs>()
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("listaTareas")
    private val dbLstas = DBListaTarea()
    private lateinit var lista : ListaTareas
    private val dbElemento = DBElementoTarea()
    private lateinit var tarea: ElementoTarea
    private lateinit var adapter: TareasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTareasBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val identificador = args.listaID

        recicler = LayoutElementoBinding.inflate(layoutInflater)

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

        adapter = TareasAdapter(this)
        binding.LlistaFaqsView.layoutManager = LinearLayoutManager(context)
        binding.LlistaFaqsView.adapter = adapter

        binding.idTvTitulo.text = list.categoria

        var tareas = ArrayList<ElementoTarea>()

        dbElemento.recuperar(args.listaID.toString(), ::recuperaElementos)

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

            tarea.tarea = popUp.editNombreTarea.text.toString()

            dbElemento.actualizaUltimoNumero(::numeroUltimo)

            dbElemento.recuperar(identificador.toString(), ::recuperaElementos)

            dialogo.dismiss()
        }
    }

    fun numeroUltimo(numero: Int){
        tarea.idElemento = numero + 1
        tarea.posicion = dbElemento.ultimoNumero + 1

        dbElemento.insertar(tarea)


    }

    fun recuperaElementos(list : ArrayList<ElementoTarea>){
        adapter.setListData(list)
        adapter.notifyDataSetChanged()

    }

    override fun onUserClickAction(elemento: ElementoTarea) {
        elemento.hecho = !elemento.hecho
        dbElemento.insertar(elemento)
        adapter.notifyDataSetChanged()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val setings = menu.findItem(R.id.cierreSesion)
        val hola = menu.findItem(R.id.gestionaTareas)
        setings.isVisible = false
        hola.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tareas, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.opcLista -> {
                findNavController().navigate(R.id.action_tareas_to_fragmentCompartirLista)
                true
            }
            R.id.gestionaTareas ->{
                Toast.makeText(context, "La has dado a Gestion", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}