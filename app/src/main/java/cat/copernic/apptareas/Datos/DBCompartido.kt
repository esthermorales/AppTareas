package cat.copernic.apptareas.Datos

import android.util.Log
import cat.copernic.apptareas.Modelos.ListaTareas
import com.google.firebase.firestore.FirebaseFirestore

class DBCompartido {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Guarda la instancia de los datos compartidos que se incluye en lista tareas
     *
     */
    fun insertar(lista: ListaTareas){
        //Si esta compartido con alguien
        if(lista.compartido != null){
            lista.compartido!!.forEach { n ->
                Log.e("Test",n.email)
                //Por cada elemento se añade a la db especifica y genera el id auto
                db.collection("compartido").document(n.email.toString()+"-"+lista.idLista)
                    .set(hashMapOf("email" to n.email.toString(), "listaTareas" to lista.idLista.toString()))
            }
        }
    }


}