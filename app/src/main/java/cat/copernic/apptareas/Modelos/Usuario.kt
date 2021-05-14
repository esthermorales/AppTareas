package cat.copernic.apptareas.Modelos

class Usuario(val email: String, var nombre: String? = null) {
    //El nombre puede ser null, porque al iniciar solo pedimos el mail.


    /**
     * Metodo toString para mostrar datos.
     */
    override fun toString(): String {
        return "Usuario[email: $email, nombre: $nombre]"
    }
}