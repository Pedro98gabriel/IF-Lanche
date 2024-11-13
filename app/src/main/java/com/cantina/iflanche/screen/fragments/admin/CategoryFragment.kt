package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.FragmentRegisterCategoryBinding
import com.cantina.iflanche.firebase.LoadCategories
import com.cantina.iflanche.screen.HomeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoryFragment : Fragment() {

    private var _binding: FragmentRegisterCategoryBinding? = null
    private val binding get() = _binding!!
    private var categories: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categories = (activity as? HomeActivity)?.categories ?: emptyList()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        val btnAddNewCategory = binding.fabAddCategory
        goToAddCategoryScreen(btnAddNewCategory)

        if (categories.isNotEmpty()) {
            setupCategoryAdapter(categories)
        } else {
            loadCategories()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goToAddCategoryScreen(btnAddNewCategory: FloatingActionButton) {
        btnAddNewCategory.setOnClickListener {
            val addCategoryFragment = AddCategoryFragment()

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, addCategoryFragment)
            transaction.addToBackStack(AddCategoryFragment::class.java.simpleName)
            transaction.commit()
        }
    }


    private fun loadCategories() {
        LoadCategories.loadCategories(
            callback = { loadedCategories ->
                // Verifica se o fragmento está ainda adicionado e visível
                if (isAdded && isResumed) {
                    // Atualiza a UI com a lista de categorias
                    setupCategoryAdapter(loadedCategories)
                }
            },
            onError = { errorMessage ->
                // Exibe o erro em caso de falha ao carregar as categorias
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }


    private fun setupCategoryAdapter(categories: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        )
        binding.tfOptionsCategorySelect.setAdapter(adapter)
    }

}