package cat.copernic.apptareas.Modelos

class ElementoTarea(
    var idElemento: Int,
    var tarea: String,
    var posicion: Int,
    var padre: Int,
    var subTarea: ListaTareas? = null,
    var hecho: Boolean = false,
    var editable: Boolean = true

) : Comparable<ElementoTarea> {


    override fun toString(): String {
        return "ElementoTarea [idElemento: $idElemento, tarea: $tarea, hecho: $hecho, editable: $editable, subtarea: ${subTarea?.nombre}, posicion: $posicion]"
    }

    override fun compareTo(other: ElementoTarea): Int {
        if (this.posicion > other.posicion) return 1
        else return -1
    }

    /**
     * Pasa la instancia a Map<String, Any>
     */
    fun toMap(): Map<String, Any> {
        var ret: Map<String, Any>
        if (subTarea != null){
            ret = mapOf(
                "idElemento" to idElemento.toString(),
                "tarea" to tarea, "subTarea" to subTarea?.idLista.toString(), "posicion" to posicion.toString(),
                "padre" to padre.toString(), "hecho" to hecho, "editable" to editable
            )
        }
        else{
            ret = mapOf(
                "idElemento" to idElemento.toString(),
                "tarea" to tarea, "posicion" to posicion.toString(),
                "padre" to padre.toString(), "hecho" to hecho, "editable" to editable
            )
        }

        return ret
    }

}