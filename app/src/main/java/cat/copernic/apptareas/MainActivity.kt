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
import cat.copernic.apptareas.Datos.DBElementoTarea
import cat.copernic.apptareas.Datos.DBListaTarea
import cat.copernic.apptareas.Datos.DBUsuario
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.UI.VistaUI

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    //-- Esta variable se le tiene que pasar a DBUsuario para recuperar los datos de la db
    //En el mismo instante no esta llena
    var usuarios = ArrayList<Usuario>()
    var listtar = ArrayList<ListaTareas>()
    var elemnts = ArrayList<ElementoTarea>()
    //--
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
        binding.idTvRegistrarse.setOnClickListener{
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

    }

    /* ja hem inicialitzat el layout però no és visible */
    override fun onStart() {
        super.onStart()

        //************************************************
        //****** Zona para testeos de clases *************
        //************************************************
        /*val useeer = DBUsuario()
        val pepito = Usuario("pepito@falso.es","Pepito ")
        useeer.insertar(pepito)

        var lista = ListaTareas(2,"Test", "Cat",null,null,null)

        lista.categoria = "hola"

        Log.e("Jose",lista.toString())

        var elemento1 = ElementoTarea(2, "Nombre 2", lista,2)
        var elemento2 = ElementoTarea(6, "Nombre 6", lista,6)
        var elemento3 = ElementoTarea(3, "Nombre 3", lista,3)
        var elemento4 = ElementoTarea(7, "Nombre 7", lista,7)
        var elemento5 = ElementoTarea(1, "Nombre 1", lista,1)

        var elementos =  ArrayList<ElementoTarea>()
        elementos.add(elemento1)
        elementos.add(elemento2)
        elementos.add(elemento3)
        elementos.add(elemento4)
        elementos.add(elemento5)*/

        //elementos.sort()
        /*for (elemento in elementos) {
            Log.e("Jose",elemento.toString())

        }*/
       /* elementos.sort()
        lista.elementos = elementos
        for(elemento in lista.elementos as ArrayList<ElementoTarea>){
            Log.e("Jose", "-->" + elemento.toString())
        }
        elementos.sort()
        lista.subirElemento(5)*/
        /*
        Log.e("Jose","Subir elemento")
        for(elemento in lista.elementos as ArrayList<ElementoTarea>){
            Log.e("Jose", "-->" + elemento.toString())
        }
        */
       /* Log.e("Jose","Bajar elemento")
        lista.bajarElemento(0)

        for(elemento in lista.elementos as ArrayList<ElementoTarea>){
            Log.e("Jose", "-->" + elemento.toString())
        }

        //Eliminar elemento
        Log.e("Jose","Eliminar elemento")

        lista.eliminarElemento(1)

        for(elemento in lista.elementos as ArrayList<ElementoTarea>){
            Log.e("Jose", "-->" + elemento.toString())
        }*/
        /*val usrdb = DBUsuario()
        usrdb.recuperar(usuarios)*/
    /*
        val recuperar = DBUsuario()
        recuperar.recuperar(usuarios, ::mostrar)

        val ultnum = DBListaTarea()
        ultnum.actualizaUltimoNumero(::mostrarUltimoNum)
*/
        /*
        val dbelt = DBElementoTarea()
        dbelt.recuperar(elemnts, ::mostrarElementos)

*/
        //*************************************************
        //*************************************************
        //*************************************************

        // la variable currentUser tindrà l'usuari actual si està loginat, sinò serà null.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // obrim l'activity principal
            val intent = Intent(this, VistaUI::class.java)
            startActivity(intent)
        }
    }
    fun mostrar(usuarios: ArrayList<Usuario>) {
        println("Size: " + usuarios.size)
    }
    fun mostrarUltimoNum(num :Int){
        println("---------------------> " + num)
    }

    fun mostrarElementos(elem: ArrayList<ElementoTarea>){
        for (elemento in elem){
            println("<--> "+ elemento.toString() +" <-->")
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
                    //seHaLogueado(user)
                    //---*--*------

                    //var operaciones: OperacionesDBFirebase_Perfil = OperacionesDBFirebase_Perfil()
                    //var perfil: Perfil = Perfil()
                    //perfil.usuario = user?.email.toString()
                    //operaciones.buscar(perfil, this.baseContext)

                    //---*--*------
                    guardarPreferencias(user)
                    val intent = Intent(this, VistaUI::class.java)
                    startActivity(intent)
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

