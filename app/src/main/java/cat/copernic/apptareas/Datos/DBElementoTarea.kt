package cat.copernic.apptareas.Datos

import android.util.Log
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class DBElementoTarea {
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("elemento")
    val listtar = ArrayList<ListaTareas>()

    /**
     * Inserta un elemento dentro de FireBase
     */


    fun insertar(elemento: ElementoTarea) {
        db.collection("elemento").document(elemento.idElemento.toString()).set(elemento.toMap())
    }

    /**
     * Recupera los dotos referentes a ElementoTarea de Firebase
     */
    fun recuperar(list: ArrayList<ElementoTarea>) {
        //BUSCAR LISTA EN DB
        val dblis = DBListaTarea()
        dblis.recuperar(listtar)
        var tmp: ListaTareas = ListaTareas(0, "", "")
        coleccion.get(Source.CACHE).addOnSuccessListener {
            for (document in it) {
                val sub = document.data.get("subTarea") as String
                /* TODO recuperar subtarea
                for (subt in listtar) {
                    if(subt != null && sub != null)
                    if (sub.toInt() == subt.idLista) {
                        tmp = subt
                    }
                }
                */


                val elementoTmp = ElementoTarea(
                    (document.data.get("idElemento") as String).toInt(),
                    document.data.get("tarea") as String,
                    tmp,
                    (document.data.get("posicion") as String).toInt(),
                    (document.data.get("hecho") as String).toBoolean(),
                    (document.data.get("editable") as String).toBoolean(),
                )
                list.add(elementoTmp)
            }

        }

    }


}