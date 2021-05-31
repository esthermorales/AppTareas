package cat.copernic.apptareas.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.copernic.apptareas.Datos.DBCompartido
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.R

class FragmentCompartirLista : Fragment() {
    var tareas = ArrayList<ListaTareas>()
    var emails = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbcompartido = DBCompartido()
        dbcompartido.recuperar(tareas, ::recuperarCopartido)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compartir_lista, container, false)



    }



    fun recuperarCopartido(lista: ArrayList<String>){
        for (mail in lista){
            emails.add(mail)
            println("--->"+mail)
        }
    }

}