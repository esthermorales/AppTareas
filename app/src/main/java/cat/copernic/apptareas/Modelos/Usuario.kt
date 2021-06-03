package cat.copernic.apptareas.Modelos

class Usuario(var email: String, var nombre: String? = null) : Comparable<Usuario> {
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

    /**
     * Metodo para comparar esta clase por el email.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Usuario

        if (email != other.email) return false

        return true
    }


    /**
     * Metodo sobreescrito para ordenar
     */
    override fun compareTo(other: Usuario): Int {
        if (this.email > other.email) {
            return 1
        } else if (this.email < other.email) {
            return -1
        } else {
            return 0
        }
    }
}