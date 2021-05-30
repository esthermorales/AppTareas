package cat.copernic.apptareas.Modelos


    class ListaTareas(
    var idLista: Int,
    var nombre: String="",
    var categoria: String="",
    var elementos: ArrayList<ElementoTarea>? = null,
    var propietario: Usuario? = null,
    var compartido: ArrayList<Usuario>? = null
) : Comparable<ListaTareas> {
    init {
        //Al iniciar si elementos no esta en null ordena la lista.
        if (elementos != null) {
            elementos!!.sort()
        }

    }

        //constructor() : this(idLista)

        /**
     * Metodo para comparar la lista por el id
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        /*
        other as Usuario
        if (this.nombre.equals(other.nombre)) return true
        else return false*/
        other as ListaTareas
        if(this.idLista == other.idLista) return true
        else return false
    }


    /**
     * Método toString para mostrar los datos
     */
    override fun toString(): String {
        return "Lista[$idLista, $nombre, $categoria, ${propietario.toString()}]"
    }

    /**
     * CompareTo para poder ordenar por id
     */
    override fun compareTo(other: ListaTareas): Int {
        if (this.idLista > other.idLista) return 1
        else return -1
    }

    fun subirElemento(posicion: Int) {
        //Si hay elementos, la posicion es mayor de 0 (que no este en el prier cuadro)
        //y que lal posicion no sea la ultima casilla
        if (elementos!!.size > 0 && posicion > 0 && posicion < elementos!!.size) {
            cambiarPosicion(elementos!!.get(posicion), elementos!!.get(posicion - 1))
            elementos!!.sort()
        }
    }

    fun bajarElemento(posicion: Int) {
        //Si hay elementos, la posicion es mayor de 0 (que no este en el prier cuadro)
        //y que lal posicion no sea la ultima casilla
        if (elementos!!.size > 0 && posicion >= 0 && posicion < elementos!!.size - 1) {
            cambiarPosicion(elementos!!.get(posicion), elementos!!.get(posicion + 1))
            elementos!!.sort()
        }
    }

    /**
     * cambia la posicion de dos elementos
     */
    fun cambiarPosicion(primero: ElementoTarea, segundo: ElementoTarea) {
        var tmp: Int = 0
        if (primero != null && segundo != null) {
            tmp = primero.posicion
            primero.posicion = segundo.posicion
            segundo.posicion = tmp
        }
    }

    /**
     * Elimina un elemento de una determinada posicion
     */
    fun eliminarElemento(posicion: Int) {
        if (elementos!!.size > 0 && posicion <= elementos!!.size && posicion >= 0)
            elementos!!.removeAt(posicion)
    }

    //Añade un elemento
    fun anyadirElemento(elemento: ElementoTarea) {
        elementos!!.add(elemento)
        elementos!!.sort()
    }

    /**
     * Añade un usuario
     * Siempre que no sea el mismo y no este ya en la lista
     */
    fun addUserCompartido(usuario: Usuario) {
        var encontrado = false
        //Si el propietario existe
        if (propietario != null) {
            //que no intente compartirlo consigo mismo
            if (usuario.equals(propietario)) {
                encontrado = true
            } else if (compartido != null) {
                //que el usuario con quien lo comparte no este en la lista
                for (num in 0..compartido!!.size - 1) {
                    if (compartido!!.get(num).equals(usuario))
                        encontrado = true
                }
            }
        }
        if (!encontrado) {
            //Si no se ha inicializado se inicia
            if (compartido == null) {
                compartido = ArrayList<Usuario>()
            }
            //se añade el usuario
            compartido?.add(usuario)
        }


    }


    /**
     * Pasa la instancia de esta clase a Map<String, Any?>
     * no se pasan las listas de elemntos ni la lista de compartidos.
     */
    fun toMap(): Map<String, String?> {
        val ret: Map<String, String?> = mapOf(
            "idLista" to idLista.toString(), "nombre" to nombre,
            "categoria" to categoria, "propietario" to propietario?.email.toString()
        )
        return ret
    }
}