package cat.copernic.apptareas.UI

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import cat.copernic.apptareas.Comprovaciones
import cat.copernic.apptareas.R
import cat.copernic.apptareas.databinding.FragmentVerUsuarioBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VerUsuario : Fragment() {

    private lateinit var binding: FragmentVerUsuarioBinding
    var user = Firebase.auth.currentUser
    private var comprovaciones = Comprovaciones()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var continuar = true
        binding.guardarUsuario.setOnClickListener{
            if (binding.editNombre.text.toString() == ""){
                continuar = false
            }

            if (!comprovaciones.validaCorreo(binding.editCorreo.text.toString())){
                continuar = false
            }


            if(!comprovaciones.validaClave(binding.editPassword.text.toString())){
                continuar = false
            }

            if (continuar){
                user!!.updatePassword(binding.editPassword.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(ContentValues.TAG, "User password updated.")
                        }
                    }


                user!!.updateEmail(binding.editCorreo.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(ContentValues.TAG, "User email address updated.")
                        }
                    }
            }
        }
    }
}