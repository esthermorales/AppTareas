package cat.copernic.apptareas.UI.ViewListas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.apptareas.Modelos.ListaTareas
import cat.copernic.apptareas.R

class ListaTareasAdapter(private val clickListener: ListaTareasAdapter.OnUserClic):
    RecyclerView.Adapter<ListaTareasAdapter.ListaTareasViewHolder>() {

    private var dataList = mutableListOf<ListaTareas>()


    fun setListData(data: MutableList<ListaTareas>) {
        dataList = data
    }

    interface OnUserClic {
        fun onUserClickAction(listas: ListaTareas)
    }

    inner class ListaTareasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(element: ListaTareas) {
            val categoria: TextView = itemView.findViewById(R.id.idListaTarea)

            categoria.text = element.categoria
            itemView.setOnClickListener { clickListener.onUserClickAction(element) }
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


