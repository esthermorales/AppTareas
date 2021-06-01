package cat.copernic.apptareas.UI.ViewListas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.R
import cat.copernic.apptareas.databinding.FragmentAnadirListaBinding
import cat.copernic.apptareas.databinding.FragmentHomeBinding
import cat.copernic.apptareas.databinding.FragmentItemListasBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_item_listas.view.*

class ListaTareasAdapter(private val clickListener: ListaTareasAdapter.OnUserClic):
    RecyclerView.Adapter<ListaTareasAdapter.ListaTareasViewHolder>() {

    private var dataList = mutableListOf<ListaTareas>()
     private val db = FirebaseFirestore.getInstance()


    fun setListData(data: MutableList<ListaTareas>) {
        dataList = data
    }

    interface OnUserClic {
        fun onUserClickAction(listas: ListaTareas)
        fun onUserListClickAction(listas: ListaTareas)
    }

    inner class ListaTareasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(element: ListaTareas) {

            val editar: ImageView = itemView.findViewById(R.id.idEdit)
            val categoria: TextView = itemView.findViewById(R.id.idListaTarea)

            categoria.text = element.categoria
            itemView.setOnClickListener { clickListener.onUserClickAction(element) }

            itemView.idDelete.setOnClickListener{

                db.collection("listaTareas").document(element.idLista.toString()).delete()

            }

            editar.setOnClickListener { clickListener.onUserListClickAction(element) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaTareasViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_listas, parent, false)
        return ListaTareasViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListaTareasViewHolder, position: Int) {
        val user = dataList[position]
        holder.bindView(user)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}


