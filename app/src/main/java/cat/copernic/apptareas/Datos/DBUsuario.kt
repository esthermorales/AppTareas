package cat.copernic.apptareas.Datos

import cat.copernic.apptareas.Modelos.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class DBUsuario {
    private val db = FirebaseFirestore.getInstance()
    /**
     * Inserta un usuario dentro de FireBase
     */
    fun insertar(usuario: Usuario) {
        db.collection("usuarios").document(usuario.email).set(usuario.toMap())
    }
}