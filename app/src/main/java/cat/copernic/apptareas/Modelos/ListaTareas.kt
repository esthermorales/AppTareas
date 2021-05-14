package cat.copernic.apptareas.Modelos


class ListaTareas (
    val idLista: Int,
    var nombre: String,
    var categoria: String,
    var elementos: ArrayList<ElementoTarea>? = null,
    var propietario: Usuario? = null,
    var compartido: ArrayList<Usuario>? = null
) : Comparable<ListaTareas>{
    init {
        //Al iniciar si elementos no esta en null ordena la lista.
        if (elementos != null){
            elementos!!.sort()
        }
    }
    /*
    idLista: Una id generada automaticamente con el número siguiente de la lista
    nombre: Nombre de la tarea
    categoria: Categoria de libre elección
    List<ElementoTarea>: Lista de elmentos, puede estar vacio
    propietario: Usuario propietario, no puede ser null
    compartido: List<Usuario>, List de usuarios con los que esta compartido, puede ser null
   */


    /**
     * Método toString para mostrar los datos
     */
    override fun toString(): String {
        return "Lista[$idLista, $nombre, $categoria]"
    }

    /**
     * CompareTo para poder ordenar por id
     */
    override fun compareTo(other: ListaTareas): Int {
        if (this.idLista > other.idLista)  return 1
        else return -1
    }

    fun subirElemento(posicion: Int){
        //Si hay elementos, la posicion es mayor de 0 (que no este en el prier cuadro)
        //y que lal posicion no sea la ultima casilla
        if(elementos!!.size > 0 && posicion > 0 && posicion < elementos!!.size){
            cambiarPosicion(elementos!!.get(posicion), elementos!!.get(posicion -1))
            elementos!!.sort()
        }
    }
    fun bajarElemento(posicion: Int){
        //Si hay elementos, la posicion es mayor de 0 (que no este en el prier cuadro)
        //y que lal posicion no sea la ultima casilla
        if(elementos!!.size > 0 && posicion >= 0 && posicion < elementos!!.size -1){
            cambiarPosicion(elementos!!.get(posicion), elementos!!.get(posicion +1))
            elementos!!.sort()
        }
    }

    /**
     * cambia la posicion de dos elementos
     */
    fun cambiarPosicion(primero: ElementoTarea, segundo: ElementoTarea){
        var tmp: Int = 0
        if(primero != null && segundo != null){
            tmp = primero.posicion
            primero.posicion = segundo.posicion
            segundo.posicion = tmp
        }
    }

    /**
     * Elimina un elemento de una determinada posicion
     */
    fun eliminarElemento(posicion: Int){
        if(elementos!!.size > 0 && posicion <= elementos!!.size && posicion >= 0)
            elementos!!.removeAt(posicion)
    }

    //Añade un elemento
    fun anyadirElemento(elemento: ElementoTarea){
        elementos!!.add(elemento)
        elementos!!.sort()
    }

    /**
     * Añade un usuario
     * Siempre que no sea el mismo y no este ya en la lista
     */
    fun addUserCompartido(usuario: Usuario){
        var encontrado = false
        //Si no esta intentando compartir consigo mismo
        if (!usuario.email.equals(propietario!!.email)){
           for (usuarioLista in compartido!!){
               if (usuarioLista.email.equals(usuario.email))
                   encontrado = true
           }
            if (!encontrado){
                compartido!!.add(usuario)
            }
        }
    }
}