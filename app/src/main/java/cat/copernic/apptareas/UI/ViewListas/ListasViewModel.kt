package cat.copernic.apptareas.UI.ViewListas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.copernic.apptareas.Modelos.ListaTareas

class ListasViewModel : ViewModel() {
    private val repo = ReposListasData()

    fun fetchUsersData(): LiveData<MutableList<ListaTareas>> {
        val mutabledata = MutableLiveData<MutableList<ListaTareas>>()
        repo.getListasData().observeForever {
            mutabledata.value = it
        }
        return mutabledata
    }

}