package cat.copernic.apptareas.Datos

import android.util.Log
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.UI.FragmentCompartirLista
import com.google.firebase.firestore.FirebaseFirestore

class DBCompartido {
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("compartido")

    /**
     * Guarda la instancia de los datos compartidos que se incluye en lista tareas
     *
     */
    fun insertar(lista: ListaTareas) {
        //Si esta compartido con alguien
        if (lista.compartido != null) {
            lista.compartido!!.forEach { n ->
                Log.e("Test", n.email)
                //Por cada elemento se añade a la db especifica y genera el id auto
                db.collection("compartido").document(n.email.toString() + "-" + lista.idLista)
                    .set(
                        hashMapOf(
                            "email" to n.email.toString(),
                            "listaTareas" to lista.idLista.toString()
                        )
                    )
            }
        }
    }

    /**
     * Para añadir un unico usuario
     */
    fun insertarMinimalista(idLista: Int, usuario: String) {
        db.collection("compartido").document(usuario + "-" + idLista.toString())
            .set(hashMapOf("email" to usuario, "listaTareas" to idLista.toString()))
    }



fun recuperar(lista: ArrayList<ListaTareas>, dostuff: (listaa: ArrayList<String>) -> Unit) {
    var listaEmailString = ArrayList<String>()
    coleccion.get().addOnSuccessListener {
        for (document in it) {
            for (lis in lista) {
                var numId: String = document.get("listaTareas") as String
                if (numId.toInt() == lis.idLista) {
                    //si el documento es igual a la lista
                    listaEmailString.add(document.get("email") as String)
                }
                ejecutarCodigo(lis.idLista, listaEmailString)
                //Para ejecutar de manera remota, recibe un arrayList con el mail
                dostuff(listaEmailString)
                listaEmailString.clear()
            }

        }
    }
}

/**
 * Codigo a ejecutar
 */
fun ejecutarCodigo(idLista: Int, emailsUsuarios: ArrayList<String>) {
    mostrar(idLista, emailsUsuarios)


}

//Ejemplo
fun mostrar(int: Int, texto: ArrayList<String>) {
    println("Compartido en " + int)
    for (tx in texto)
        println("-> " + texto)

}



}