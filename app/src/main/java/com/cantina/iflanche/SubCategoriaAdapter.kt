package com.cantina.iflanche

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubcategoriaAdapter(private val subcategorias: List<String>) :
    RecyclerView.Adapter<SubcategoriaAdapter.SubcategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoriaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subcategory, parent, false)
        return SubcategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubcategoriaViewHolder, position: Int) {
        holder.bind(subcategorias[position])
    }

    override fun getItemCount(): Int = subcategorias.size

    class SubcategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewSubcategoria: TextView = itemView.findViewById(R.id.itemTextSubcategoryName)

        fun bind(subcategoria: String) {
            textViewSubcategoria.text = subcategoria
        }
    }
}