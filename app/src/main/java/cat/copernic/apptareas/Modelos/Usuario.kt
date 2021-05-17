package cat.copernic.apptareas.Modelos

class Usuario(val email: String, var nombre: String? = null) {
    //El nombre puede ser null, porque al iniciar solo pedimos el mail.


    /**
     * Metodo toString para mostrar datos.
     */
    override fun toString(): String {
        return "Usuario[email: $email, nombre: $nombre]"
    }

    /**
     * Devuelve un Map<String, String?> de la instancia de la clase
     */
    fun toMap(): Map<String, String?> {
        val ret: Map<String, String?> = mapOf("email" to email, "nombre" to nombre)
        return ret
    }
}