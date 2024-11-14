package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.FragmentRegisterProductBinding
import com.cantina.iflanche.databinding.FragmentRegisterSubCategoryBinding

class ProductFragment : Fragment() {
    private var _binding: FragmentRegisterProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterProductBinding.inflate(inflater, container, false)
        val view = binding.root







        return view
    }

}