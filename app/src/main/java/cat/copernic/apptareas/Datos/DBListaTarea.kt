package cat.copernic.apptareas.Datos

import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class DBListaTarea {
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("listaTareas")
    private val usuarios = ArrayList<Usuario>()
    /**
     * Inserta una lista dentro de FireBase
     */
    fun insertar(lista: ListaTareas){
        db.collection("listaTareas").document(lista.idLista.toString()).set(lista.toMap())
    }

    fun recuperar(lista: ArrayList<ListaTareas>){
        //Para buscar el usuario
        //val usuariosDB = db.collection("usuarios")
        val dbuser = DBUsuario()
        dbuser.recuperar(usuarios)

        coleccion.get(Source.CACHE).addOnSuccessListener {
            for (document in it){
                val listaTareasTmp = ListaTareas((document.data.get("idLista") as String).toInt(),
                    document.data.get("nombre") as String, document.data.get("categoria") as String
                )
               //BUSCAR PROPIETARIO EN LA DB
                for (usuario in usuarios){
                    if (usuario.email.equals(document.data.get("propietario" as String))){
                        listaTareasTmp.propietario = usuario
                    }
                }

                lista.add(listaTareasTmp)
            }
        }
    }
}