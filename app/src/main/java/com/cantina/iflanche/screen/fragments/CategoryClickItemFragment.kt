package com.cantina.iflanche.screen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.FragmentCategoryClickItemBinding
import com.cantina.iflanche.databinding.FragmentClickProductItemBinding

class CategoryClickItemFragment : Fragment() {
    private var _binding: FragmentCategoryClickItemBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryClickItemBinding.inflate(inflater, container, false)
        val view = binding.root

        val categoryName = arguments?.getString("categoryName")
        binding.itemTextCategoryClick.text = categoryName


        return view
    }


}