package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cantina.iflanche.databinding.FragmentClickProductItemBinding

class ClickProductItemFragment : Fragment() {
    private var _binding: FragmentClickProductItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClickProductItemBinding.inflate(inflater, container, false)
        val view = binding.root

        val productId = arguments?.getString("productId")
        val productName = arguments?.getString("productName")
        val productPrice = arguments?.getString("productPrice")
        val productImageUrl = arguments?.getString("productImageUrl")
        val productDescription = arguments?.getString("productDescription")

        binding.textViewProductItemClickName.text = productName
        binding.textViewProductItemClickPrice.text = "R$ $productPrice"
        binding.textViewProductItemClickDescription.text = productDescription
        Glide.with(this).load(productImageUrl).into(binding.imageViewProductItemClick)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}