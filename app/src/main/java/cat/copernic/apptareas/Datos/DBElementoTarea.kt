package cat.copernic.apptareas.Datos

import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import com.google.firebase.firestore.FirebaseFirestore


class DBElementoTarea {
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("elemento")
    private var listatarTmp = ArrayList<ListaTareas>()
    private lateinit var listatar: ArrayList<ListaTareas>
    var ultimoNumero: Int = 0

    init {

    }

    /**
     * Inserta un elemento dentro de FireBase
     */


    fun insertar(elemento: ElementoTarea) {
        db.collection("elemento").document(elemento.idElemento.toString()).set(elemento.toMap())

    }

    /**
     * Recupera los dotos referentes a ElementoTarea de Firebase
     */
    fun recuperar(id: String, dostuff: (users: ArrayList<ElementoTarea>) -> Unit) {

        var tmp: ListaTareas = ListaTareas(0, "", "")
        coleccion.whereEqualTo("padre", id).get().addOnSuccessListener {
            var list = ArrayList<ElementoTarea>()
            for (document in it) {
                var sub: String = ""
                //SI esxiste subtarea
                if (document.data.get("subTarea") != null) {
                    sub = document.data.get("subTarea") as String
                }

                val elementoTmp = ElementoTarea(
                    (document.data.get("idElemento") as String).toInt(),
                    document.data.get("tarea") as String,
                    (document.data.get("posicion") as String).toInt(),
                    (document.data.get("padre") as String).toInt(),
                    tmp,
                    (document.data.get("hecho") as Boolean),
                    (document.data.get("editable") as Boolean),
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
    fun actualizaUltimoNumero(dostuff: (numero: Int) -> Unit) {
        var numero: Int = 0
        val doc = db.collection("elemento").get().addOnSuccessListener {
            var numeros = ArrayList<Int>()
            it.forEach {
                if (it != null)
                    numeros.add((it.data.get("idElemento") as String).toInt())

                if (ultimoNumero < (it.data.get("posicion") as String).toInt())
                    ultimoNumero = (it.data.get("posicion") as String).toInt()
            }

            //una vez estan recuperados todos los números de la db, se ordenan
            numeros.sort()
            //si hay contenido se coge el último elemento, que es el número más alto
            if (numeros.size > 0) {
                numero = numeros.get(numeros.size - 1)
            }
            //Esta funcion obtiene el número más alto
            dostuff(numero)
        }
    }

    fun recuperarListaTareas(lista: ArrayList<ListaTareas>) {
        listatar = ArrayList<ListaTareas>()
        listatar = lista
    }

    /**
     * Metodo para recuperar una vez recuperada la lista
     */
    fun conListaRecuperada(listaTareas: ArrayList<ListaTareas>) {
        //var listaTareas = ArrayList<ListaTareas>()

        var list = ArrayList<ElementoTarea>()
        //Inicializo sin contenido
        var tmpLista = ListaTareas(0, "")

        coleccion.get().addOnSuccessListener {
            for (document in it) {
                var sub: String = "0"
                //SI esxiste subtarea
                if (document.data.get("subTarea") != null) {
                    sub = document.data.get("subTarea") as String
                    //Si hay contenido en listaTareas
                    if (listaTareas.size > 0) {
                        for (elemLista in listaTareas) {
                            //Si es igual a la id de lista
                            if (elemLista.idLista.toInt() == sub.toInt()) {
                                tmpLista = elemLista
                            }
                        }
                    }
                }

                val elementoTmp = ElementoTarea(
                    (document.data.get("idElemento") as String).toInt(),
                    document.data.get("tarea") as String,
                    (document.data.get("posicion") as String).toInt(),
                    (document.data.get("padre") as String).toInt(),
                    tmpLista,
                    document.data.get("hecho") as Boolean,
                    document.data.get("editable") as Boolean,
                )
                list.add(elementoTmp)
            }
            //code here
            //EL codigo que se ejecuta
            codigoEjecutarConDatos(list)
        }
    }

    /**
     * Aqui introducir los metodos a ejecutar
     */
    fun codigoEjecutarConDatos(listaElementos: ArrayList<ElementoTarea>) {
        //ejemplo
        mostrar(listaElementos)
    }

    /**
     * ejemplo
     */
    fun mostrar(elm: ArrayList<ElementoTarea>) {
        for (elemento in elm)
            println(elemento.toString())
    }


}