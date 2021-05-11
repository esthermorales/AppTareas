package cat.copernic.apptareas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cat.copernic.apptareas.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.text.TextUtils
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Botones
        //binding.btnLogIn.setOnClickListener(this)
        binding.btnLogIn.setOnClickListener {
            //Llamada a metodos de los botones
            login(binding.txtEmail.text.toString(), binding.txtPass.text.toString())
        }
        auth = Firebase.auth
/*
        binding.btnRegistro.setOnClickListener(){
            val intent = Intent(this, RegistreActivity::class.java)
            startActivity(intent)
        }
*/

    }

    /* ja hem inicialitzat el layout però no és visible */
    override fun onStart() {
        super.onStart()
        // la variable currentUser tindrà l'usuari actual si està loginat, sinò serà null.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // obrim l'activity admin
            //val toHome = Intent(this, ActivityCliente::class.java)

            //startActivity(toHome)
        }
    }

    /**
     * Comprueba que el usuario no ha dejado los campos en blanco
     * @return boleano si es correcto o no
     */
    private fun analizarDatosUsuario(): Boolean {
        var valid = true

        val email = binding.txtEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            //mensaje email vacio, cambiar tost por alertDialog
            //Toast.makeText(this, "email no valido", Toast.LENGTH_LONG).show()
            mensajeEmergente("Atencion", getString(R.string.msgVacio))
            valid = false
        } else {
            binding.txtEmail.error = null
        }

        val password = binding.txtPass.text.toString()
        if (TextUtils.isEmpty(password)) {
            //mensaje pass vacio. cambiar toast por alertDialog
            //Toast.makeText(this, "Password no valido", Toast.LENGTH_LONG).show()
            mensajeEmergente("Atenció", "Omple el camp de contrasenya.")
            valid = false
        } else {
            binding.txtPass.error = null
        }

        return valid
    }

    private fun seHaLogueado(user: FirebaseUser?) {
        //Si se ha logueado cargar la siguiente pagina, user contiene el usuario FirebaseUser
        if (user != null) {
            //Pasar a la siguiente activity.
            //Toast.makeText(this, "Se ha logueado con exito -> Siguiente activity", Toast.LENGTH_LONG).show()
            mensajeEmergente("Mensaje", "Login correcto.")

            //val toHome = Intent(this, ActivityCliente::class.java)
            //startActivity(toHome)
        } else {
            //El usuario esta vació, mensaje
            //Toast.makeText(this, "no se ha logueado", Toast.LENGTH_LONG).show()
            mensajeEmergente("Mensaje", "no se ha logueado")
        }
    }

    private fun login(email: String, password: String) {
        Log.d("TAG", "signIn:$email")
        if (!analizarDatosUsuario()) {
            return
        }

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    seHaLogueado(user)
                    //---*--*------

                    //var operaciones: OperacionesDBFirebase_Perfil = OperacionesDBFirebase_Perfil()
                    //var perfil: Perfil = Perfil()
                    //perfil.usuario = user?.email.toString()
                    //operaciones.buscar(perfil, this.baseContext)

                    //---*--*------
                    guardarPreferencias(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, task.exception.toString(), task.exception)
                    //Toast.makeText(baseContext, "Authentication failed.",
                    //Toast.LENGTH_SHORT).show()
                    mensajeEmergente("Error", "Error en l'autentificació")
                    seHaLogueado(null)
                    // [START_EXCLUDE]
                    checkForMultiFactorFailure(task.exception!!)
                    // [END_EXCLUDE]
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    //binding.status.setText(R.string.auth_failed)
                    //BToast.makeText(this, "Autentificación ha fallado", Toast.LENGTH_LONG).show()
                }
                // [END_EXCLUDE]
            }
    }

    /**
     * Guarda las preferencias en el terminal movil
     * @user los datos del user logeado
     */
    private fun guardarPreferencias(user: FirebaseUser?) {
        val preferencias: SharedPreferences = getSharedPreferences(
            "credenciales",
            Context.MODE_PRIVATE
        ) //Nombre del archivo de preferencias
        val email = user?.email.toString() //Obtengo el mail del usuario
        val editor: SharedPreferences.Editor = preferencias.edit() //Creo el editor
        editor.putString("email", email) //Almaceno el mail en el campo email
        editor.commit() //y almaceno
    }

    /**
     * Trozo de codigo del ejemplo que creo necesario
     */
    private fun checkForMultiFactorFailure(e: Exception) {
        // Multi-factor authentication with SMS is currently only available for
        // Google Cloud Identity Platform projects. For more information:
        // https://cloud.google.com/identity-platform/docs/android/mfa
        if (e is FirebaseAuthMultiFactorException) {
            Log.w(TAG, "multiFactorFailure", e)
            val intent = Intent()
            val resolver = e.resolver
            intent.putExtra("EXTRA_MFA_RESOLVER", resolver)
            setResult(42, intent)
            finish()
        }
    }

    /**
     * Muestra un alert dialog con el titulo y el mensaje
     *
     * @titulo El titutolo del dialog
     * @mensaje El mensaje del dialogo
     */
    private fun mensajeEmergente(titulo: String, mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(titulo)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar") { dialog, which -> //De momento nada que hacer
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun onClick(v: View) {
        when (v.id) {
            //Llamada a metodos de los botones
            R.id.btnLogIn -> login(
                binding.txtEmail.text.toString(),
                binding.txtPass.text.toString()
            )

        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}

