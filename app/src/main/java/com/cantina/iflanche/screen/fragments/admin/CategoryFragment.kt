package com.cantina.iflanche.screen.fragments.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.FragmentRegisterCategoryBinding
import com.cantina.iflanche.firebase.DeleteCategory
import com.cantina.iflanche.firebase.LoadCategories
import com.cantina.iflanche.screen.HomeActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CategoryFragment : Fragment() {

    private var _binding: FragmentRegisterCategoryBinding? = null
    private val binding get() = _binding!!
    private var selectedCategory: String? = null
    private var itemID: String? = null // Nome da categoria a ser editada


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemID = arguments?.getString("categoryName")


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tfOptionsCategorySelect.setOnClickListener {
            binding.tfDropdownCategorySelect.error = null
        }

        val btnAddNewCategory = binding.fabAddCategory
        val btnDeleteCategory = binding.btnDeleteCategory

        goToAddCategoryScreen(btnAddNewCategory)

        deleteSelectedItem(btnDeleteCategory)

        loadCategories()

        return view
    }

    private fun deleteSelectedItem(btnDeleteCategory: Button) {
        btnDeleteCategory.setOnClickListener {
            selectedCategory = binding.tfOptionsCategorySelect.text.toString()
            if (selectedCategory!!.isNotEmpty()) {
                showDeleteAlertDialog(selectedCategory!!)
            } else {

                binding.tfDropdownCategorySelect.error = "Selecione uma categoria para apagar"

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        loadCategories()
        binding.tfOptionsCategorySelect.setText("")
        binding.tfDropdownCategorySelect.error = null
        binding.tfOptionsCategorySelect.clearFocus()
        (activity as? HomeActivity)?.setAppBarTitle("Gerenciar Categoria")

        binding.root.setOnTouchListener { _, _ ->
            binding.tfOptionsCategorySelect.clearFocus()
            binding.tfDropdownCategorySelect.error = null
            false
        }
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

        binding.btnEditCategory.setOnClickListener {
            selectedCategory = binding.tfOptionsCategorySelect.text.toString()
            if (selectedCategory!!.isNotEmpty()) {
                goToEditCategoryScreen(selectedCategory!!)
            } else {

                binding.tfDropdownCategorySelect.error = "Selecione uma categoria para editar"

            }
        }
    }

    private fun goToEditCategoryScreen(categoryName: String) {
        val fragment = AddCategoryFragment()

        // Cria um bundle com o nome da categoria
        val bundle = Bundle()
        bundle.putString("categoryName", categoryName)
        fragment.arguments = bundle

        // Substitui o fragmento atual pelo fragmento de edição
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showDeleteAlertDialog(categorySelected: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Apagar")
            .setMessage("A categoria \"$categorySelected\" será apagada, deseja continuar?")
            .setNegativeButton("Não") { dialog, which ->
            }
            .setPositiveButton("Sim") { dialog, which ->
                DeleteCategory.deleteCategory(
                    categorySelected,
                    onSuccess = {
                        Toast.makeText(
                            requireContext(),
                            "Categoria removida com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.tfOptionsCategorySelect.setText("")
                        binding.tfOptionsCategorySelect.clearFocus()


                    },
                    onError = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .show()
    }

}