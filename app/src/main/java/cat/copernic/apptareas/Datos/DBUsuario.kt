package cat.copernic.apptareas.Datos

import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.reflect.KFunction2

class DBUsuario {
    lateinit var listaUsuarios: ArrayList<Usuario>
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("usuarios")

    /**
     * Inserta un usuario dentro de FireBase
     */
    fun insertar(usuario: Usuario) {
        db.collection("usuarios").document(usuario.email).set(usuario.toMap())
    }

    /**
     * Recuperar usuarios
     * Para poder utilizarlo hay que pasar un arrayList de usuario y
     * la referencia a una funcion que recibe parametro ArrayList<Usuario>
     */
    fun recuperar(
        listaUsuarios: ArrayList<Usuario>,
        dostuff: (users: ArrayList<Usuario>) -> Unit
    ) {
        //Recupera los datos del cache por si no tiene conexión
        coleccion.get().addOnSuccessListener {
            for (document in it) {
                val usrTmp = Usuario(
                    document.data.get("email") as String,
                    document.data.get("nombre") as String
                )
                listaUsuarios.add(usrTmp)
            }
            dostuff(listaUsuarios)
        }

    }


}

