package cat.copernic.apptareas.UI

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import cat.copernic.apptareas.Datos.DBListaTarea
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.Modelos.Usuario
import cat.copernic.apptareas.databinding.FragmentAnadirListaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PopUpAñadirLista: DialogFragment() {

    private  var _binding: FragmentAnadirListaBinding?=null
    private val binding get() = _binding!!
    lateinit  var listaTareas : ListaTareas
    var dbLista= DBListaTarea()



    private lateinit var auth: FirebaseAuth
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


        listaTareas=ListaTareas(0)


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
                var fire=  Firebase.auth.currentUser
                var usuario=Usuario(fire.email)
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + usuario.email)

                //Toast.makeText(activity,"${dbLista.ultimoNumero}",Toast.LENGTH_LONG).show();

               //listaTareas.idLista=7
                listaTareas.categoria = binding.editTextTextPersonName2.text.toString()
                listaTareas.nombre = binding.editTextTextPersonName3.text.toString()
                listaTareas.propietario=usuario

                dbLista.actualizaUltimoNumero (::ultimo)

                binding.editTextTextPersonName2.setText("")
                binding.editTextTextPersonName3.setText("")

            }

            dismiss()

        }

        binding.idButonCancelar .setOnClickListener{
            dismiss()
        }

        var view= binding.root
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun ultimo(num:Int){
        listaTareas.idLista = num + 1
        dbLista.insertar(listaTareas)

    }
}