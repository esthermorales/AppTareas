package cat.copernic.apptareas.UI

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.R
import cat.copernic.apptareas.UI.ViewListas.ListaTareasAdapter
import cat.copernic.apptareas.UI.ViewListas.ListasViewModel
import cat.copernic.apptareas.databinding.FragmentHomeBinding


class home : Fragment(), ListaTareasAdapter.OnUserClic {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ListaTareasAdapter
    private val viewModel by lazy { ViewModelProviders.of(this).get(ListasViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListaTareasAdapter(this)
        binding.recyclerListas.layoutManager = LinearLayoutManager(context)
        binding.recyclerListas.adapter = adapter

        observeData()

        boton()

        binding.idImActualizar.setOnClickListener {
            observeData()
        }

    }

    fun boton(){
        binding.addLista.setOnClickListener{
            var dialog=PopUpAñadirLista()

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
        return when(item.itemId){
            R.id.editUser -> {
                findNavController().navigate(R.id.action_home2_to_verUsuario)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun observeData(){
        viewModel.fetchUsersData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onUserClickAction(listas: ListaTareas) {

        Toast.makeText(context, "Hola, que tal?", Toast.LENGTH_SHORT).show()
        //val arg = homeUserDirections.actionHomeUserToFragmentEditarMascota(mascota.chip)
       // findNavController().navigate(arg)
    }
}