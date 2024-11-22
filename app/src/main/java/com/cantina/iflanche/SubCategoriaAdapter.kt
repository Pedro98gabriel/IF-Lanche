package com.cantina.iflanche

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cantina.iflanche.baseclasses.Item

class SubcategoriaAdapter(
    private val subcategorias: List<String>,
    private val produtos: List<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<SubcategoriaAdapter.SubcategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubcategoriaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_subcategory, parent, false)
        return SubcategoriaViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: SubcategoriaViewHolder, position: Int) {
        holder.bind(subcategorias[position],
            produtos.filter { it.subCategory == subcategorias[position] })
    }

    override fun getItemCount(): Int = subcategorias.size

    class SubcategoriaViewHolder(itemView: View, private val onItemClick: (Item) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val textViewSubcategoria: TextView =
            itemView.findViewById(R.id.itemTextSubcategoryName)
        private val produtosContainer: RecyclerView = itemView.findViewById(R.id.produtosContainer)

        fun bind(subcategoria: String, produtos: List<Item>) {
            textViewSubcategoria.text = subcategoria
            produtosContainer.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            produtosContainer.adapter = ProdutoAdapter(produtos, onItemClick)
        }
    }
}