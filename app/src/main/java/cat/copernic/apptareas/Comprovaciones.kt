package cat.copernic.apptareas

import java.util.regex.Pattern

class Comprovaciones {

    fun validaClave(key: String): Boolean {
        return key.length >= 6
    }

    fun validaCorreo(correo: String): Boolean {
        val pattern: Pattern = Pattern.compile("^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+.[A-Za-z0-9]+")
        return pattern.matcher(correo).matches()
    }

    fun contieneTexto(txt: String): Boolean {
        var resultado = false
        if (txt != null) {
            if (!txt.equals("")) {
                resultado = true
            } else {
                resultado = false
            }
        } else {
            resultado = false
        }
        return resultado
    }
}