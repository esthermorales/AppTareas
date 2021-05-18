package cat.copernic.apptareas.UI

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cat.copernic.apptareas.R
import cat.copernic.apptareas.databinding.FragmentHomeBinding

class home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        setHasOptionsMenu(true)

        return binding.root
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
}