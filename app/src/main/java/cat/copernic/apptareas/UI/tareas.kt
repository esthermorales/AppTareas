package cat.copernic.apptareas.UI

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.apptareas.Comprovaciones
import cat.copernic.apptareas.Datos.DBElementoTarea
import cat.copernic.apptareas.Datos.DBListaTarea
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.R
import cat.copernic.apptareas.UI.RecyclerTareas.EditaTareasAdapter
import cat.copernic.apptareas.UI.RecyclerTareas.TareasAdapter
import cat.copernic.apptareas.UI.ViewListas.ListaTareasAdapter
import cat.copernic.apptareas.databinding.FragmentTareasBinding
import cat.copernic.apptareas.databinding.LayoutElementoBinding
import cat.copernic.apptareas.databinding.PopUpCrearTareaBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.android.synthetic.main.pop_up_crear_tarea.view.*

class tareas : Fragment(), TareasAdapter.OnTareaClic, EditaTareasAdapter.OnTareaClic {

    private lateinit var binding: FragmentTareasBinding
    private lateinit var popu: PopUpCrearTareaBinding
    private lateinit var recicler: LayoutElementoBinding
    private val args by navArgs<tareasArgs>()
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("listaTareas")
    private val dbLstas = DBListaTarea()
    private lateinit var lista: ListaTareas
    private val dbElemento = DBElementoTarea()
    private lateinit var tarea: ElementoTarea
    private lateinit var adapter: TareasAdapter
    private lateinit var editAdapter: EditaTareasAdapter
    var elementosGenerales = ArrayList<ElementoTarea>()
    val comprovaciones = Comprovaciones()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTareasBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        iniciaRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val identificador = args.listaID

        recicler = LayoutElementoBinding.inflate(layoutInflater)

        lista = ListaTareas(0, "", "")
        coleccion.whereEqualTo("idLista", identificador.toString()).limit(1).get()
            .addOnSuccessListener {
                for (document in it) {
                    if (it != null) {
                        val listaTareasTmp = ListaTareas(
                            (document.data.get("idLista") as String).toInt(),
                            document.data.get("nombre") as String,
                            document.data.get("categoria") as String
                        )

                        lista = listaTareasTmp

                        vista(listaTareasTmp)

                    }
                }
            }
    }


    fun iniciaRecyclerView() {
        binding.btnSlEditTareas.isVisible = false
        binding.addLista.isVisible = true

        adapter = TareasAdapter(this)
        binding.LlistaFaqsView.layoutManager = LinearLayoutManager(context)
        binding.LlistaFaqsView.adapter = adapter

        dbElemento.recuperar(args.listaID.toString(), ::recuperaElementos)
    }

    fun vista(list: ListaTareas) {

        binding.idTvTitulo.text = list.categoria

        binding.addLista.setOnClickListener {
            popPupEmergente()
        }

        binding.btnSlEditTareas.setOnClickListener {
            iniciaRecyclerView()
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
            if (comprovaciones.contieneTexto(popUp.editNombreTarea.text.toString())) {
                tarea = ElementoTarea(0, popUp.editNombreTarea.toString(), 0, identificador)

                tarea.tarea = popUp.editNombreTarea.text.toString()

                dbElemento.actualizaUltimoNumero(::numeroUltimo)
            } else {
                Toast.makeText(context, "El nombre no puede estar en blnco", Toast.LENGTH_SHORT)
                    .show()
            }

            dialogo.dismiss()
        }
    }

    fun numeroUltimo(numero: Int) {
        tarea.idElemento = numero + 1
        tarea.posicion = dbElemento.ultimoNumero + 1

        dbElemento.insertar(tarea)

        dbElemento.recuperar(args.listaID.toString(), ::recuperaElementos)

    }

    fun recuperaElementos(list: ArrayList<ElementoTarea>) {
        list.sort()
        elementosGenerales = list
        adapter.setListData(list)
        adapter.notifyDataSetChanged()

    }

    fun recuperaElementosEdit(list: ArrayList<ElementoTarea>) {
        list.sort()
        elementosGenerales = list
        editAdapter.setListData(list)
        editAdapter.notifyDataSetChanged()

    }

    /**
     * Metodos del menu
     */
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
        return when (item.itemId) {
            R.id.opcLista -> {
                findNavController().navigate(R.id.action_tareas_to_fragmentCompartirLista)
                true
            }
            R.id.gestionaTareas -> {
                binding.btnSlEditTareas.isVisible = true
                binding.addLista.isVisible = false

                editAdapter = EditaTareasAdapter(this)
                binding.LlistaFaqsView.layoutManager = LinearLayoutManager(context)
                binding.LlistaFaqsView.adapter = editAdapter

                dbElemento.recuperar(args.listaID.toString(), ::recuperaElementosEdit)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Metodos para alterar listas
     */
    fun subirElemento(posicion: Int): Boolean {
        //Si hay elementos, la posicion es mayor de 0 (que no este en el prier cuadro)
        //y que lal posicion no sea la ultima casilla
        if (elementosGenerales.size > 0 && posicion > 0 && posicion < elementosGenerales.size) {
            cambiarPosicion(elementosGenerales.get(posicion), elementosGenerales.get(posicion - 1))
            return true
        } else
            return false
    }

    fun bajarElemento(posicion: Int): Boolean {
        //Si hay elementos, la posicion es mayor de 0 (que no este en el prier cuadro)
        //y que lal posicion no sea la ultima casilla
        if (elementosGenerales.size > 0 && posicion >= 0 && posicion < elementosGenerales.size - 1) {
            cambiarPosicion(elementosGenerales.get(posicion), elementosGenerales.get(posicion + 1))
            return true
        } else
            return false
    }


    // Metodos Heredados


    /**
     * cambia la posicion de dos elementos
     */
    fun cambiarPosicion(primero: ElementoTarea, segundo: ElementoTarea) {
        var tmp: Int = 0
        if (primero != null && segundo != null) {
            tmp = primero.posicion
            primero.posicion = segundo.posicion
            segundo.posicion = tmp
        }
    }

    override fun onUserClickAction(elemento: ElementoTarea) {
        elemento.hecho = !elemento.hecho
        dbElemento.insertar(elemento)
        adapter.notifyDataSetChanged()
    }

    override fun onButtonUpClic(elemento: ElementoTarea) {
        val indice = elementosGenerales.indexOf(elemento)
        val continua = subirElemento(indice)

        if (continua) {
            dbElemento.insertar(elementosGenerales.get(indice))
            dbElemento.insertar(elementosGenerales.get(indice - 1))

            dbElemento.recuperar(args.listaID.toString(), ::recuperaElementosEdit)
        }
    }

    override fun onButtonDownClic(elemento: ElementoTarea) {
        val indice = elementosGenerales.indexOf(elemento)
        val continua = bajarElemento(indice)

        if (continua) {
            dbElemento.insertar(elementosGenerales.get(indice))
            dbElemento.insertar(elementosGenerales.get(indice + 1))

            dbElemento.recuperar(args.listaID.toString(), ::recuperaElementosEdit)
        }
    }

    override fun onButtonDelClic(elemento: ElementoTarea) {
        db.collection("elemento").document(elemento.idElemento.toString()).delete()
        dbElemento.recuperar(args.listaID.toString(), ::recuperaElementosEdit)
    }

}