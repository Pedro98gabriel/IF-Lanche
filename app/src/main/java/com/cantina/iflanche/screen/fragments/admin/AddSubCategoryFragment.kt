package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cantina.iflanche.databinding.FragmentAddSubCategoryBinding


class AddSubCategoryFragment : Fragment() {
    private var _binding: FragmentAddSubCategoryBinding? = null
    private val binding get() = _binding!!
    private var subCategoryName: String? = null // Nome da categoria a ser editada


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subCategoryName = arguments?.getString("subCategoryName")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddSubCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tfAddSubCategoryContent.setText(subCategoryName)  // Preenche o campo com o nome da categoria

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}