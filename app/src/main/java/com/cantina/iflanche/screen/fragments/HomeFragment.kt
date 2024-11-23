package com.cantina.iflanche.screen.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cantina.iflanche.CategoriaAdapter
import com.cantina.iflanche.R
import com.cantina.iflanche.SubcategoriaAdapter
import com.cantina.iflanche.baseclasses.Item
import com.cantina.iflanche.databinding.FragmentCategoryClickItemBinding
import com.cantina.iflanche.databinding.FragmentHomeBinding
import com.cantina.iflanche.firebase.LoadCategories
import com.cantina.iflanche.firebase.LoadProducts
import com.cantina.iflanche.screen.fragments.admin.ClickProductItemFragment
import com.cantina.iflanche.utils.SpacingItemDecoration

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var subcategoryRecyclerView: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var subcategoriaAdapter: SubcategoriaAdapter
    private val categoriasList = mutableListOf<String>()
    private val subcategoriasList = mutableListOf<String>()
    private val produtosList = mutableListOf<Item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewCategories
        subcategoryRecyclerView = binding.recyclerViewSubcategories

        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val spacing = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView.addItemDecoration(SpacingItemDecoration(spacing))

        categoriaAdapter = CategoriaAdapter(categoriasList) { category ->
            onCategoryClick(category)
        }
        recyclerView.adapter = categoriaAdapter

        subcategoryRecyclerView.layoutManager = LinearLayoutManager(context)
        subcategoriaAdapter = SubcategoriaAdapter(subcategoriasList, produtosList) { item ->
            onProductClick(item)
        }
        subcategoryRecyclerView.adapter = subcategoriaAdapter

        carregarCategorias()
        carregarProdutos()

        return binding.root
    }

    private fun carregarCategorias() {
        LoadCategories.loadCategories(
            callback = { categories ->
                categoriasList.clear()
                categoriasList.addAll(categories)
                categoriaAdapter.notifyDataSetChanged()
            },
            onError = { errorMessage ->
                Log.e("HomeFragment", errorMessage)
            }
        )
    }

    private fun carregarProdutos() {
        LoadProducts.loadProdutos(
            callback = { subcategoryMap ->
                val filteredSubcategories = subcategoryMap.filter { it.value.isNotEmpty() }
                val subcategories = filteredSubcategories.keys.toList()
                val produtos = filteredSubcategories.values.flatten()
                if (subcategories.isNotEmpty()) {
                    produtosList.clear()
                    produtosList.addAll(produtos)
                    subcategoriasList.clear()
                    subcategoriasList.addAll(subcategories)
                    subcategoriaAdapter.notifyDataSetChanged()
                    subcategoryRecyclerView.visibility = View.VISIBLE
                } else {
                    subcategoryRecyclerView.visibility = View.GONE
                }
            },
            onError = { errorMessage ->
                Log.e("HomeFragment", errorMessage)
            }
        )
    }

    private fun onProductClick(item: Item) {
        val fragment = ClickProductItemFragment()
        val bundle = Bundle()
        bundle.putString("productId", item.id)
        bundle.putString("productName", item.name)
        bundle.putString("productPrice", item.price)
        bundle.putString("productImageUrl", item.imageUrl)
        bundle.putString("productCategory", item.category)
        bundle.putString("productSubCategory", item.subCategory)
        bundle.putString("productDescription", item.description)
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun onCategoryClick(category: String) {
        val fragment = CategoryClickItemFragment()
        val bundle = Bundle()
        bundle.putString("categoryName", category)
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}