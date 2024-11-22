package com.cantina.iflanche

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cantina.iflanche.baseclasses.Item

class ProdutoAdapter(private val produtos: List<Item>) :
    RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_product, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.bind(produtos[position])
    }

    override fun getItemCount(): Int = produtos.size

    class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNomeProduto: TextView = itemView.findViewById(R.id.itemNomeProduto)
        private val imageViewProduto: ImageView = itemView.findViewById(R.id.imageView_product)


        fun bind(produto: Item) {
            textViewNomeProduto.text = produto.name
            Glide.with(itemView.context)
                .load(produto.imageUrl)
                .into(imageViewProduto)
        }
    }
}