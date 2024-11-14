package com.cantina.iflanche.screen.fragments.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.FragmentRegisterSubCategoryBinding
import com.cantina.iflanche.firebase.LoadCategories
import com.cantina.iflanche.screen.HomeActivity


class SubcategoryFragment : Fragment() {
    private var _binding: FragmentRegisterSubCategoryBinding? = null
    private val binding get() = _binding!!
    private var subCategories: List<String> = emptyList()

    private var selectedSubCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subCategories = (activity as? HomeActivity)?.categories ?: emptyList()


    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        loadSubCategories()
        setupSubCategoryAdapters(subCategories)
        binding.tfOptionsSubcategorySelect.setText("")
        binding.tfDropdownSubcategorySelect.error = null
        binding.tfOptionsSubcategorySelect.clearFocus()
        (activity as? HomeActivity)?.setAppBarTitle("Gerenciar Subcategoria")

        binding.root.setOnTouchListener { _, _ ->
            binding.tfOptionsSubcategorySelect.clearFocus()
            binding.tfDropdownSubcategorySelect.error = null
            false
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterSubCategoryBinding.inflate(inflater, container, false)
        val view = binding.root
        
        binding.tfOptionsSubcategorySelect.setOnClickListener {
            binding.tfDropdownSubcategorySelect.error = null
        }

        loadSubCategories()
        editSubCategory()
        goToAddSubCategory()

        return view
    }

    private fun goToAddSubCategory() {
        binding.fabAddSubCategory.setOnClickListener {
            val addCategoryFragment = AddSubCategoryFragment()

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, addCategoryFragment)
            transaction.addToBackStack(AddCategoryFragment::class.java.simpleName)
            transaction.commit()
        }
    }

    private fun editSubCategory() {
        binding.btnEditSubcategory.setOnClickListener {
            val fragment = AddSubCategoryFragment()

            selectedSubCategory = binding.tfOptionsSubcategorySelect.text.toString()

            // Cria um bundle com o nome da categoria
            val bundle = Bundle()
            bundle.putString("subCategoryName", selectedSubCategory)
            fragment.arguments = bundle

            // Substitui o fragmento atual pelo fragmento de edição
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .addToBackStack(null)
                .commit()

        }
    }

    private fun loadSubCategories() {
        LoadCategories.loadSubCategories(
            callback = { loadedSubCategories ->
                // Verifica se o fragmento está ainda adicionado e visível
                if (isAdded && isResumed) {
                    // Atualiza a UI com a lista de categorias
                    setupSubCategoryAdapters(loadedSubCategories)
                }
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        )

    }

    private fun setupSubCategoryAdapters(subcategories: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            subcategories
        )
        binding.tfOptionsSubcategorySelect.setAdapter(adapter)

        binding.btnEditSubcategory.setOnClickListener {
            selectedSubCategory = binding.tfOptionsSubcategorySelect.text.toString()
            if (selectedSubCategory!!.isNotEmpty()) {
                goToEditSubCategoryScreen(selectedSubCategory!!)
            } else {

                binding.tfDropdownSubcategorySelect.error = "Selecione uma categoria para editar"

            }
        }
    }

    private fun goToEditSubCategoryScreen(categoryName: String) {
        val fragment = AddSubCategoryFragment()

        // Cria um bundle com o nome da categoria
        val bundle = Bundle()
        bundle.putString("subCategoryName", categoryName)
        fragment.arguments = bundle

        // Substitui o fragmento atual pelo fragmento de edição
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}