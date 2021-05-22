package cat.copernic.apptareas.Datos

import android.util.Log
import cat.copernic.apptareas.Modelos.Usuario
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*
import java.lang.Thread.sleep
import kotlin.coroutines.CoroutineContext

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

    fun recuperar(listaUsuarios: ArrayList<Usuario>) {
        //Recupera los datos del cache por si no tiene conexión
        coleccion.get(Source.CACHE).addOnSuccessListener {
            for (document in it){
                val usrTmp = Usuario(document.data.get("email") as String,
                    document.data.get("nombre") as String
                )
                listaUsuarios.add(usrTmp)
            }
        }
    }


}

