package cat.copernic.apptareas.UI.RecyclerTareas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.apptareas.Modelos.ElementoTarea
import cat.copernic.apptareas.R

class EditaTareasAdapter(private val clickListener: OnTareaClic) :
    RecyclerView.Adapter<EditaTareasAdapter.EditaTareasViewHolder>() {

    private var dataList = ArrayList<ElementoTarea>()

    fun setListData(data: ArrayList<ElementoTarea>) {
        dataList = data
    }

    interface OnTareaClic {
        fun onButtonUpClic(elemento: ElementoTarea)
        fun onButtonDownClic(elemento: ElementoTarea)
        fun onButtonDelClic(elemento: ElementoTarea)
    }

    inner class EditaTareasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(element: ElementoTarea) {
            val nombre: TextView = itemView.findViewById(R.id.txtEditElemento)
            val botonUp: ImageButton = itemView.findViewById(R.id.imageButton3)
            val botondown: ImageButton = itemView.findViewById(R.id.btnBajarElemento)
            val eliminar: ImageButton = itemView.findViewById(R.id.btnDeleteElemento)

            nombre.text = element.tarea

            botonUp.setOnClickListener { clickListener.onButtonUpClic(element) }
            botondown.setOnClickListener { clickListener.onButtonDownClic(element) }
            eliminar.setOnClickListener { clickListener.onButtonDelClic(element) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditaTareasViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_elemento_editar, parent, false)
        return EditaTareasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: EditaTareasAdapter.EditaTareasViewHolder, position: Int) {
        val user = dataList[position]
        holder.bindView(user)
    }
}
