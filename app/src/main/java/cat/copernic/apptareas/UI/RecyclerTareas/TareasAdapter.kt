package cat.copernic.apptareas.UI.RecyclerTareas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.R

class TareasAdapter(private val clickListener: OnTareaClic) :
    RecyclerView.Adapter<TareasAdapter.TareasViewHolder>() {

    private var dataList = ArrayList<ElementoTarea>()


    fun setListData(data: ArrayList<ElementoTarea>) {
        dataList = data
    }

    interface OnTareaClic {
        fun onUserClickAction(elemento: ElementoTarea)
    }

    inner class TareasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(element: ElementoTarea) {
            val categoria: TextView = itemView.findViewById(R.id.lblElemento)
            val check: CheckBox = itemView.findViewById(R.id.checkHecho)

            categoria.text = element.tarea
            check.isChecked = element.hecho

            itemView.setOnClickListener { clickListener.onUserClickAction(element) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_elemento, parent, false)
        return TareasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: TareasAdapter.TareasViewHolder, position: Int) {
        val user = dataList[position]
        holder.bindView(user)
    }
}
