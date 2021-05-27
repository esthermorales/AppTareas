package cat.copernic.apptareas.Datos

import android.util.Log
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source

class DBElementoTarea {
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("elemento")
    private var listatarTmp = ArrayList<ListaTareas>()
    private lateinit var  listatar: ArrayList<ListaTareas>
    var ultimoNumero: Int = 0

    init {
        //actualizaUltimoNumero()
    }
    /**
     * Inserta un elemento dentro de FireBase
     */


    fun insertar(elemento: ElementoTarea) {
        db.collection("elemento").document(elemento.idElemento.toString()).set(elemento.toMap())
        //actualizaUltimoNumero()
    }

    /**
     * Recupera los dotos referentes a ElementoTarea de Firebase
     */
    fun recuperar(list: ArrayList<ElementoTarea>,
                  dostuff: (users: ArrayList<ElementoTarea>) -> Unit) {
        //BUSCAR LISTA EN DB
        val dblis = DBListaTarea()
        //Recupera la lista de tareas
        dblis.recuperar(listatarTmp, ::recuperarListaTareas)
        var tmp: ListaTareas = ListaTareas(0, "", "")
        coleccion.get(Source.CACHE).addOnSuccessListener {
            for (document in it) {
                var sub: String = ""
                //SI esxiste subtarea
                if (document.data.get("subTarea") != null){
                    sub = document.data.get("subTarea") as String
                }

                println(listatarTmp.size)
                for (subt in listatar) {
                    if(subt != null && sub != null)
                    if (sub.toInt() == subt.idLista) {
                        tmp = subt
                    }
                }

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
            //Realiza la acción con el ArrayList<ElementoTarea>
            dostuff(list)
        }

    }

    /**
     * Recupera el último numero id
     */
    fun actualizaUltimoNumero(dostuff: (numero: Int) -> Unit){
        var numero: Int = 0
        val doc = db.collection("elemento").orderBy("idElemento", Query.Direction.DESCENDING)
            .limit(1).get().addOnSuccessListener {
                it.forEach {
                    if (it != null)
                        numero = (it.data.get("idElemento") as String).toInt()
                }
                dostuff(numero)
            }
    }

    fun recuperarListaTareas(lista: ArrayList<ListaTareas>){
        listatar = ArrayList<ListaTareas>()
        listatar= lista
    }


}