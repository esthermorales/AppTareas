package cat.copernic.apptareas.UI

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.copernic.apptareas.Comprovaciones
import cat.copernic.apptareas.Datos.DBUsuario
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.databinding.FragmentVerUsuarioBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VerUsuario : Fragment() {

    private lateinit var binding: FragmentVerUsuarioBinding
    var user = Firebase.auth.currentUser
    lateinit var correo: String
    private var comprovaciones = Comprovaciones()
    var subir = DBUsuario()

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
        var contraseña = false
        var texto = ""
        correo = user!!.email.toString()

        updateUI()
        binding.guardarUsuario.setOnClickListener {
            if (!comprovaciones.validaCorreo(binding.editCorreo.text.toString())) {
                texto += "El correo electronico no es valido"
            }

            if (!comprovaciones.validaClave(binding.editPassword.text.toString()) && comprovaciones.contieneTexto(
                    binding.editPassword.text.toString()
                )
            ) {
                if (!texto.equals(""))
                    texto += System.getProperty("line.separator")

                texto += "La contraseña introducida no es valida"
            } else if (comprovaciones.contieneTexto(binding.editPassword.text.toString())) {
                contraseña = true
            }

            if (!texto.equals("")) {
                missatgeEmergent(texto)
            } else {
                texto = "Tu nuevo correo es " + binding.editCorreo.text.toString()
                missatgeEmergent(texto)
                correo = binding.editCorreo.text.toString()

                user!!.updateEmail(binding.editCorreo.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            texto = "Tu nuevo correo es " + binding.editCorreo.text.toString()
                        }
                    }

                if (contraseña) {
                    user!!.updatePassword(binding.editPassword.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(ContentValues.TAG, "User password updated.")
                            }
                        }
                }

                val usuario = Usuario(correo, binding.editNombre.text.toString())

                subir.insertar(usuario)
                updateUI()
            }
        }
    }

    fun updateUI() {
        binding.editCorreo.setText(correo)
        binding.editPassword.setText("")
    }

    fun missatgeEmergent(missatge: String) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage(missatge)
        builder.setPositiveButton("Aceptar") { dialog, which -> }
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }
}