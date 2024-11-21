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
import com.cantina.iflanche.databinding.FragmentHomeBinding
import com.cantina.iflanche.firebase.LoadCategories
import com.cantina.iflanche.utils.SpacingItemDecoration

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var subcategoryRecyclerView: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var subcategoriaAdapter: SubcategoriaAdapter
    private val categoriasList = mutableListOf<String>()
    private val subcategoriasList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewCategories
        subcategoryRecyclerView = binding.recyclerViewSubcategories

        // Configurar GridLayoutManager com orientação horizontal
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // Adicionar espaçamento entre os itens
        val spacing = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView.addItemDecoration(SpacingItemDecoration(spacing))

        categoriaAdapter = CategoriaAdapter(categoriasList)
        recyclerView.adapter = categoriaAdapter

        // Configurar LinearLayoutManager para subcategorias
        subcategoryRecyclerView.layoutManager = LinearLayoutManager(context)
        subcategoriaAdapter = SubcategoriaAdapter(subcategoriasList)
        subcategoryRecyclerView.adapter = subcategoriaAdapter

        carregarCategorias()
        carregarSubcategorias()

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

    private fun carregarSubcategorias() {
        LoadCategories.loadSubCategories(
            callback = { subcategories ->
                subcategoriasList.clear()
                subcategoriasList.addAll(subcategories)
                subcategoriaAdapter.notifyDataSetChanged()
            },
            onError = { errorMessage ->
                Log.e("HomeFragment", errorMessage)
            }
        )
    }
}