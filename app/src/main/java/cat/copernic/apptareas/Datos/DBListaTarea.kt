package cat.copernic.apptareas.Datos

import cat.copernic.apptareas.Modelos.ListaTareas
import com.google.firebase.firestore.FirebaseFirestore

class DBListaTarea {
    private val db = FirebaseFirestore.getInstance()
    /**
     * Inserta una lista dentro de FireBase
     */
    fun insertar(lista: ListaTareas){
        db.collection("listaTareas").document(lista.idLista.toString()).set(lista.toMap())
    }
}