package cat.copernic.apptareas.UI

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cat.copernic.apptareas.Datos.DBElementoTarea
import cat.copernic.apptareas.Datos.DBListaTarea
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.R
import cat.copernic.apptareas.UI.ViewListas.ListaTareasAdapter
import cat.copernic.apptareas.UI.ViewListas.ListasViewModel
import cat.copernic.apptareas.databinding.FragmentAnadirListaBinding
import cat.copernic.apptareas.databinding.FragmentHomeBinding
import cat.copernic.apptareas.databinding.PopUpCrearTareaBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_anadir_lista.view.*
import kotlinx.android.synthetic.main.fragment_compartir_lista.view.*
import kotlinx.android.synthetic.main.fragment_item_listas.view.*
import kotlinx.android.synthetic.main.pop_up_crear_tarea.view.*


class home : Fragment(), ListaTareasAdapter.OnUserClic, SwipeRefreshLayout.OnRefreshListener {


    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapter: ListaTareasAdapter
    private val viewModel by lazy { ViewModelProviders.of(this).get(ListasViewModel::class.java) }
    private val db = FirebaseFirestore.getInstance()
    private lateinit var popUpLista: FragmentAnadirListaBinding
    private val dbElemento = DBElementoTarea()

    private var dbList = DBListaTarea()

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        //Indica quien refresca
        binding.refreshrecyclerlistas.setOnRefreshListener(this)
        //Cambia el color del circulo de carga
        binding.refreshrecyclerlistas.setColorScheme(R.color.azulApp, R.color.naranjaApp)
        setHasOptionsMenu(true)

        return binding.root
    }

    //Hace un refresco del recyclerView
    override fun onRefresh() {
        observeData()
        //Para la animación del actualizando
        binding.refreshrecyclerlistas.isRefreshing = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListaTareasAdapter(this)
        binding.recyclerListas.layoutManager = LinearLayoutManager(context)
        binding.recyclerListas.adapter = adapter

        observeData()
        boton()

    }

    fun boton() {
        binding.addLista.setOnClickListener {
            var dialog = PopUpAñadirLista()
            getFragmentManager()?.let { it1 -> dialog.show(it1, "custom dialog") }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val setings = menu.findItem(R.id.cierreSesion)
        setings.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editUser -> {
                findNavController().navigate(R.id.action_home2_to_verUsuario)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun observeData() {
        viewModel.fetchUsersData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onUserClickAction(listas: ListaTareas) {
        val arg = homeDirections.actionHome2ToTareas(listas.idLista)
        findNavController().navigate(arg)
    }

    override fun onUserListClickAction(listas: ListaTareas) {

        var binding: FragmentAnadirListaBinding
        binding = FragmentAnadirListaBinding.inflate(layoutInflater)




        popUpLista = FragmentAnadirListaBinding.inflate(layoutInflater)
        val popUp = popUpLista.root

        val builder = AlertDialog.Builder(context)
            .setView(popUp)
            .setTitle("Editar lista")

        val dialogo = builder.show()


        popUp.idBtnAnadirL.setOnClickListener {

            var fire = Firebase.auth.currentUser
            var usuario = Usuario(fire.email)

            Toast.makeText(context, "ffffff", Toast.LENGTH_LONG).show()

            listas.categoria = popUp.editTextTextPersonName2.text.toString()
            listas.nombre = popUp.editTextTextPersonName3.text.toString()
            listas.propietario = usuario

            val map: MutableMap<String, Any> = mutableMapOf()

            map.put("categoria", listas.categoria)
            map.put("nombre", listas.nombre)


            db.collection("listaTareas").document(listas.idLista.toString()).update(map)

            dialogo.dismiss()

        }

    }

    override fun onUserDeleteListClickAction(listas: ListaTareas) {
        // itemView.idDelete.setOnClickListener{

        val identificador = listas.idLista

        dbElemento.recuperar(identificador.toString(), ::eliminaElementos)

        db.collection("listaTareas").document(listas.idLista.toString()).delete()
        // }


    }

    fun eliminaElementos(elementos: ArrayList<ElementoTarea>){
        for (elemento in 0..(elementos.size - 1)){
            elementos.get(elemento)
            db.collection("elemento").document(elementos.get(elemento).idElemento.toString()).delete()
        }
    }
}
