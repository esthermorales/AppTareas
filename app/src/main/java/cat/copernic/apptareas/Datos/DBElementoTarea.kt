package cat.copernic.apptareas.Datos

import android.util.Log
import cat.copernic.apptareas.Modelos.ElementoTarea
import com.google.firebase.firestore.FirebaseFirestore

class DBElementoTarea {
    private val db = FirebaseFirestore.getInstance()
    /**
     * Inserta un elemento dentro de FireBase
     */
    fun insertar(elemento: ElementoTarea){
        db.collection("elemento").document(elemento.idElemento.toString()).set(elemento.toMap())
    }

    /**
     * Recupera los dotos referentes a ElementoTarea de Firebase
     */
    fun recuperar(){

    }
}