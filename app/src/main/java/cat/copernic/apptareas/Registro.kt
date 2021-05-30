package cat.copernic.apptareas



import android.content.Intent
import android.graphics.Color.parseColor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import cat.copernic.apptareas.UI.VistaUI
import cat.copernic.apptareas.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.type.Color
import java.util.*
import kotlin.concurrent.timerTask

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.idBtnRegistro.setEnabled(false)
        binding.idBtnRegistro.setTextColor(parseColor("#9E9E9E"))//texto del botón en gris


        binding.idCheck.setOnClickListener {
            if (binding.idCheck.isChecked) {
                binding.idBtnRegistro.setEnabled(true)
                binding.idBtnRegistro.setTextColor(parseColor("#ffffff"))


            } else {
                binding.idBtnRegistro.setEnabled(false)
                binding.idBtnRegistro.setTextColor(parseColor("#9E9E9E"))
            }
        }

        binding.btnLey.setOnClickListener{
            val url: String = "https://www.boe.es/buscar/pdf/2018/BOE-A-2018-16673-consolidado.pdf"
            val uri: Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }



        registreUsuari()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        Log.i("user:", "" + currentUser)
        binding.idEtEmail.setText("")
        binding.idEtPassword.setText("")
        binding.idEtConfPass.setText("")


    }

    fun createUserWithEmailAndPassword(email: String, passwordd: String) {

        mAuth!!.createUserWithEmailAndPassword(email, passwordd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                    updateUI(user)
                    mensajeEmergente("Información", "Te has registrado correctamente.")

                    val timer = Timer()
                    timer.schedule(timerTask { nextScreen() }, 2000)



                   // val intent = Intent(this, VistaUI::class.java)
                   // startActivity(intent)

                } else {
                    mensajeEmergente("Error", "Error en la autentificación")
                    updateUI(null)
                }
            }
    }
    private fun registreUsuari() {

     binding.idBtnRegistro.setOnClickListener() {
      if (!binding.idEtEmail.text.isEmpty() && !binding.idEtPassword.text.isEmpty() && !binding.idEtConfPass.text.isEmpty()) {
                if (binding.idEtPassword.text.toString()
                        .equals(binding.idEtConfPass.text.toString())
                ) {
                    if (binding.idEtPassword.text.toString().length > 5) {
                        createUserWithEmailAndPassword(
                            binding.idEtEmail.text.toString(),
                            binding.idEtPassword.text.toString()


                        )

                    } else {
                        mensajeEmergente(
                            "Error",
                            "La contraseña tiene que contener mínimo 6 caracteres"
                        )
                    }
                } else {
                    mensajeEmergente("Error", "Las contraseñas no son iguales")
                }
            } else {
                mensajeEmergente("Error", "Se tienen que llenar todos los campos")
            }
        }
    }

    private fun nextScreen(){
        val intent = Intent(this, VistaUI::class.java)
        startActivity(intent)
    }

    private fun mensajeEmergente(titulo: String, mensage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensage)
        builder.setPositiveButton("Aceptar") { dialog, which ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}