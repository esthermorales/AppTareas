package cat.copernic.apptareas.Datos

import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source

class DBListaTarea {
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("listaTareas")
    private val usuarios = ArrayList<Usuario>()
    //public var ultimoNumero = 0
    private lateinit var usuariosTmp : ArrayList<Usuario>


    /**
     * Inserta una lista dentro de FireBase
     */
    init {
        //Actualiza el ultimo numero
        //actualizaUltimoNumero(::ulNum)
    }

    fun insertar(lista: ListaTareas) {
        db.collection("listaTareas").document(lista.idLista.toString()).set(lista.toMap())
        //actualizaUltimoNumero(::ulNum)
    }

    fun recuperar(lista: ArrayList<ListaTareas>,
                  dostuff: (users: ArrayList<ListaTareas>) -> Unit) {
        //Para buscar el usuario
        val usuariosDB = db.collection("usuarios")
        val dbuser = DBUsuario()
        var otro = ArrayList<Usuario>()
        //Pasa referencia de recuperarUsuarios
        dbuser.recuperar(usuarios, ::recuperarUsuarios)

        coleccion.get(Source.CACHE).addOnSuccessListener {
            for (document in it) {
                val listaTareasTmp = ListaTareas(
                    (document.data.get("idLista") as String).toInt(),
                    document.data.get("nombre") as String, document.data.get("categoria") as String
                )
                //BUSCAR PROPIETARIO EN LA DB
                for (usuario in usuariosTmp) {
                    if (usuario.email.equals(document.data.get("propietario" as String))) {
                        listaTareasTmp.propietario = usuario
                    }
                }

                lista.add(listaTareasTmp)
            }
            //Funcion que trabajara con el contenido
            dostuff(lista)
        }
    }

    /**
     * Recupera el último numero id
     */
    fun actualizaUltimoNumero(dostuff: (numero: Int) -> Unit) {
        var numero = 0
        val doc = db.collection("listaTareas").orderBy("idLista", Query.Direction.DESCENDING)
            .limit(1).get().addOnSuccessListener {
                it.forEach {
                    if (it != null)
                        numero = (it.data.get("idLista") as String).toInt()
                }
                //Esta funcion recibe el número final
                dostuff(numero)
            }
    }

    fun recuperarUsuarios(usu: ArrayList<Usuario>){
        usuariosTmp = usu
    }
    //Para utilizar con el metodo anterior

    //posiblemente hay que eliminar esta función
    fun ulNum(ult: Int){
            //ultimoNumero = ult
    }

}