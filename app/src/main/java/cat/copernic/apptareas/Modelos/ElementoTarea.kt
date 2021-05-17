package cat.copernic.apptareas.Modelos

class ElementoTarea(
    val idElemento: Int,
    var tarea: String,
    var subTarea: ListaTareas,
    var posicion: Int,
    var hecho: Boolean = false,
    var editable: Boolean = true

) : Comparable<ElementoTarea> {


    override fun toString(): String {
        return "ElementoTarea [idElemento: $idElemento, tarea: $tarea, hecho: $hecho, editable: $editable, subtarea: ${subTarea.nombre}, posicion: $posicion]"
    }

    override fun compareTo(other: ElementoTarea): Int {
        if (this.posicion > other.posicion) return 1
        else return -1
    }

    /**
     * Pasa la instancia a Map<String, Any>
     */
    fun toMap(): Map<String, Any> {
        val ret: Map<String, Any> = mapOf(
            "idElemento" to idElemento,
            "tarea" to tarea, "subTarea" to subTarea, "posicion" to posicion,
            "hecho" to hecho, "editable" to editable
        )
        return ret
    }

}