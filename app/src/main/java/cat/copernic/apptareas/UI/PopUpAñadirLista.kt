package cat.copernic.apptareas.UI

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import cat.copernic.apptareas.Datos.DBListaTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.databinding.FragmentAnadirListaBinding

class PopUpAñadirLista: DialogFragment() {

    private  var _binding: FragmentAnadirListaBinding?=null
    private val binding get() = _binding!!
    lateinit  var listaTareas : ListaTareas
    var dbLista= DBListaTarea()
    var cont=0
    //private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding =FragmentAnadirListaBinding.inflate(inflater, container, false)


        listaTareas=ListaTareas(dbLista.ultimoNumero)



        binding.idBtnAnadirL.setOnClickListener {


            if (binding.editTextTextPersonName2.equals("") || binding.editTextTextPersonName3.equals(
                    ""
                )
            ) {
                //validacion()

                if (binding.editTextTextPersonName2.equals("")) {
                    binding.editTextTextPersonName2.setError("Required")
                } else if (binding.editTextTextPersonName3.equals("")) {
                    binding.editTextTextPersonName3.setError("Required")
                }

            } else {


                Toast.makeText(activity,"${dbLista.ultimoNumero}",Toast.LENGTH_LONG).show();

               listaTareas.idLista=++dbLista.ultimoNumero
                listaTareas.categoria = binding.editTextTextPersonName2.text.toString()
                listaTareas.nombre = binding.editTextTextPersonName3.text.toString()

                binding.editTextTextPersonName2.setText("")
                binding.editTextTextPersonName3.setText("")

            }

            dbLista.insertar(listaTareas)

            dismiss()
        }

        val view = binding.root
        return view
    }

   /* override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

   

        binding.idButonCancelar .setOnClickListener{
            dismiss()
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}