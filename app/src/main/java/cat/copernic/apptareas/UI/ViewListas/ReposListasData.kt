package cat.copernic.apptareas.UI.ViewListas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cat.copernic.apptareas.Modelos.ListaTareas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ReposListasData {

    fun getListasData() : LiveData<MutableList<ListaTareas>> {
        val mutableData = MutableLiveData<MutableList<ListaTareas>>()
        val user = FirebaseAuth.getInstance().currentUser?.email
        Log.e("TestLog", user.toString())
        FirebaseFirestore.getInstance().collection("listaTareas").whereEqualTo("propietario",user.toString()).get().addOnSuccessListener { doc ->
            val listData = mutableListOf<ListaTareas>()
            for (document in doc){
                val idLista = (document.get("idLista") as String).toInt()
                val categoria = document.getString("categoria")
                val nombre = document.getString("nombre")

                val lista = ListaTareas(idLista!!, nombre!!, categoria!!)
                listData.add(lista)
            }
            mutableData.value = listData
        }
        return mutableData
    }



}