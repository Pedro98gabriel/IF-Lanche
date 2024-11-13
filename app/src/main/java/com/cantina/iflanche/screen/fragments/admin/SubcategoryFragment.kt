package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.cantina.iflanche.databinding.FragmentRegisterSubCategoryBinding


class SubcategoryFragment : Fragment() {
    private var _binding: FragmentRegisterSubCategoryBinding? = null
    private val binding get() = _binding!!
    private var selectedSubCategory: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterSubCategoryBinding.inflate(inflater, container, false)
        val view = binding.root


            loadDropdownItems()


            binding.btnEditSubcategory.setOnClickListener {
                val fragment = AddSubCategoryFragment()

                selectedSubCategory = binding.tfOptionsSubcategorySelect.text.toString()

                // Cria um bundle com o nome da categoria
                val bundle = Bundle()
                bundle.putString("subCategoryName", selectedSubCategory)
                fragment.arguments = bundle

                // Substitui o fragmento atual pelo fragmento de edição
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(com.cantina.iflanche.R.id.fragment_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .addToBackStack(null)
                    .commit()

            }

            return view
        }

        private fun loadDropdownItems() {
            val items = listOf("Item 1", "Item 2", "Item 3") // Substitua com seus itens
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
            binding.tfOptionsSubcategorySelect.setAdapter(adapter)
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


    }