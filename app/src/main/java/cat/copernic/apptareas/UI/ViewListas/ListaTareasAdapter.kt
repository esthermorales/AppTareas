package cat.copernic.apptareas.UI.ViewListas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.R
import com.google.firebase.firestore.FirebaseFirestore


class ListaTareasAdapter(private val clickListener: ListaTareasAdapter.OnUserClic) :
    RecyclerView.Adapter<ListaTareasAdapter.ListaTareasViewHolder>() {

    private var dataList = mutableListOf<ListaTareas>()
    private val db = FirebaseFirestore.getInstance()


    fun setListData(data: MutableList<ListaTareas>) {
        dataList = data
    }

    interface OnUserClic {
        fun onUserClickAction(listas: ListaTareas)
        fun onUserListClickAction(listas: ListaTareas)
        fun onUserDeleteListClickAction(listas: ListaTareas)
    }

    inner class ListaTareasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(element: ListaTareas) {

            val editar: ImageView = itemView.findViewById(R.id.idEdit)
            val delete: ImageView = itemView.findViewById(R.id.idDelete)
            val categoria: TextView = itemView.findViewById(R.id.idListaTarea)

            categoria.text = element.categoria
            itemView.setOnClickListener { clickListener.onUserClickAction(element) }

            editar.setOnClickListener { clickListener.onUserListClickAction(element) }

            delete.setOnClickListener { clickListener.onUserDeleteListClickAction(element) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaTareasViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item_listas, parent, false)
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


