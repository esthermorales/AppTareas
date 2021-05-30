package cat.copernic.apptareas.Datos

import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.UI.tareas
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

class DBListaTarea {
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("listaTareas")
    private val usuarios = ArrayList<Usuario>()

    //public var ultimoNumero = 0
    private lateinit var usuariosTmp: ArrayList<Usuario>
    var ultimoNumero  = 0


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

    fun recuperar(
        lista: ArrayList<ListaTareas>,
        dostuff: (users: ArrayList<ListaTareas>) -> Unit
    ) {
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
     * Versión 2 de recuperar para no alterar la funcion recuperar en caso de que se este utilizando la otra
     */
    fun recuperar_v2(lista: ArrayList<ListaTareas>, dostuff: (users: ArrayList<ListaTareas>) -> Unit){
        coleccion.get(Source.CACHE).addOnSuccessListener {
            for (document in it) {
                val listaTareasTmp = ListaTareas(
                    (document.data.get("idLista") as String).toInt(),
                    document.data.get("nombre") as String, document.data.get("categoria") as String
                )
                //Añadir el usuario propietari .o

                // ---
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
        val doc = db.collection("listaTareas").get().addOnSuccessListener {
            var numeros = ArrayList<Int>()
            it.forEach {
                if (it != null)
                    numeros.add((it.data.get("idLista") as String).toInt())
            }

            //recupero todos los números y selecciono el ultimo, que es el mas alto

            numeros.sort()
            if (numeros.size > 0) {
                numero = numeros.get(numeros.size - 1)
            }
            //Esta funcion recibe el número final
            dostuff(numero)
        }
    }

    fun recuperarUsuarios(usu: ArrayList<Usuario>) {
        usuariosTmp = usu
    }
    //Para utilizar con el metodo anterior

    //posiblemente hay que eliminar esta función
    fun ulNum(ult: Int) {
        //ultimoNumero = ult
    }

    /**
     * Utilizar esta función para llamar la dbusuarios
     * en esta funcion ya se deberian haber recuperado el ArrayList de usuarios
     */
    fun conUsuariosRecuperados(usuarios :ArrayList<Usuario>){
        var lista = ArrayList<ListaTareas>()
        coleccion.get().addOnSuccessListener {
            for (document in it) {
                val listaTareasTmp = ListaTareas(
                    (document.data.get("idLista") as String).toInt(),
                    document.data.get("nombre") as String, document.data.get("categoria") as String
                )
                //BUSCAR PROPIETARIO EN LA DB
                for (usuario in usuarios) {
                    var emai = document.data.get("propietario") as String
                    if (usuario.email.equals(document.data.get("propietario" as String))) {
                        listaTareasTmp.propietario = usuario
                    }
                }
                lista.add(listaTareasTmp)
            }
            //******
            ejecutarConDatosRecuperados(lista)
            //******
        }
    }

    /**
     * !!
     * !!
     * Metodo recuperar usuario
     * Este es el metodo que hay que llamar para recuperar!!!
     */
    fun recuperarContenido(){
        var usu = DBUsuario()
        usu.recuperar(usuarios, ::conUsuariosRecuperados)
    }

    /**
     * Ejemplo para testear, eliminar despues
     */
    fun mostrar(mues : ArrayList<ListaTareas>){
        for (lista in mues)
            println(lista.toString())
    }

    /**
     * !!!!!!
     * Aqui se pueden lanzar los metodos, lista contiene los elementos
     */
    fun ejecutarConDatosRecuperados(lista: ArrayList<ListaTareas>){
        //Ejemplo para probar el funcionamiento
        //mostrar(lista)
        //-- Recupera los datos de la db Elementos
        var recuperarDBElementos = DBElementoTarea()
        recuperarDBElementos.conListaRecuperada(lista)
        //--
        //Recupera compartido
        var compartido = DBCompartido()
        println("----------------------------- test ---------------------------")
        compartido.recuperar(lista)

    }

    fun enviaDatos(lista: ArrayList<ListaTareas>) : ArrayList<ListaTareas> {
        var ret = lista

        return ret
    }

}