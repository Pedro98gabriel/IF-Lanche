package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.FragmentRegisterProductBinding
import com.cantina.iflanche.databinding.FragmentRegisterSubCategoryBinding
import com.cantina.iflanche.firebase.LoadCategories
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProductFragment : Fragment() {
    private var _binding: FragmentRegisterProductBinding? = null
    private val binding get() = _binding!!

    private var productName: String? = null
    private var productDescription: String? = null
    private var productPrice: String? = null
    private var categories: List<String> = emptyList()
    private var subCategories: List<String> = emptyList()
    private var btnAddProduct: Button? = null
    private var selectedCategory: String? = null
    private var selectedSubCategory: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterProductBinding.inflate(inflater, container, false)
        val view = binding.root


        btnAddProduct = binding.btnAddProduct

        btnAddProduct?.setOnClickListener {
            productName = binding.tfProductNameContent.text.toString()
            productDescription = binding.tfProductDescriptionContent.text.toString()
            productPrice = binding.tfProductPriceContent.text.toString()
            selectedCategory = binding.tfOptionsProductCategory.text.toString()
            selectedSubCategory = binding.tfOptionsProductSubCategory.text.toString()

            Log.i("ProductFragment", "Name: $productName")
            Log.i("ProductFragment", "Description: $productDescription")
            Log.i("ProductFragment", "Price: $productPrice")
            Log.i("ProductFragment", "Category: $selectedCategory")
            Log.i("ProductFragment", "Subcategory: $selectedSubCategory")
        }

        loadCategoriesAndSubCategories()

        return view
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun loadCategoriesAndSubCategories() {
        loadItems(
            isCategory = true,
            onSuccess = { categories ->
                setupAdapter(
                    binding.tfOptionsProductCategory,
                    categories
                )
            },
            onError = { showError(it) }
        )

        loadItems(
            isCategory = false,
            onSuccess = { subCategories ->
                setupAdapter(
                    binding.tfOptionsProductSubCategory,
                    subCategories
                )
            },
            onError = { showError(it) }
        )
    }

    private fun loadItems(
        isCategory: Boolean,
        onSuccess: (List<String>) -> Unit,
        onError: (String) -> Unit
    ) {
        if (isCategory) {
            LoadCategories.loadCategories(
                callback = { loadedCategories ->
                    if (isAdded && isResumed) onSuccess(loadedCategories)
                },
                onError = { errorMessage ->
                    if (isAdded && isResumed) onError(errorMessage)
                }
            )
        } else {
            LoadCategories.loadSubCategories(
                callback = { loadedSubCategories ->
                    if (isAdded && isResumed) onSuccess(loadedSubCategories)
                },
                onError = { errorMessage ->
                    if (isAdded && isResumed) onError(errorMessage)
                }
            )
        }
    }

    private fun setupAdapter(view: AutoCompleteTextView, items: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items
        )
        view.setAdapter(adapter)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }


}