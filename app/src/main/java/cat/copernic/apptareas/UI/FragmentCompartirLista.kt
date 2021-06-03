package cat.copernic.apptareas.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.copernic.apptareas.Datos.DBCompartido
import cat.copernic.apptareas.databinding.FragmentCompartirListaBinding
import com.google.firebase.firestore.FirebaseFirestore

class FragmentCompartirLista : Fragment() {
    var idDeLaLista = 1
    private lateinit var binding: FragmentCompartirListaBinding
    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("compartido")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val dbcompartido = DBCompartido()

        Log.i("Jose", "Ahora aqui")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this frag
        binding = FragmentCompartirListaBinding.inflate(inflater, container, false)
        val dbcompartido = DBCompartido()
        //Al hacer clic al añadir usuario
        binding.addUser.setOnClickListener {
            //Si tiene contenido
            if (!binding.editTextTextEmailAddress.text.equals("")) {
                //Inserta al usuario en la db
                dbcompartido.insertarMinimalista(
                    idDeLaLista,
                    binding.editTextTextEmailAddress.text.toString()
                )
                //Limpia el txt
                var limpia = ""
                binding.editTextTextEmailAddress.text = null
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("Jose", "Estoy en este punto")
        coleccion.whereEqualTo("listaTareas", idDeLaLista.toString()).get().addOnSuccessListener {
            for (document in it) {
                if (it != null) {
                    Log.i("Jose", document.data.get("email") as String)
                    if (binding.multilineaUsuers.text.equals("")) {
                        binding.multilineaUsuers.setText(document.data.get("email") as String)
                    } else {
                        binding.multilineaUsuers.setText(
                            binding.multilineaUsuers.text.toString()
                                    + "\n" + document.data.get("email") as String
                        )
                    }

                }
            }
        }

    }
}