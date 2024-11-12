package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.FragmentRegisterCategoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CategoryFragment : Fragment() {

    private var _binding: FragmentRegisterCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        val btnAddNewCategory = binding.fabAddCategory
        goToAddCategoryScreen(btnAddNewCategory)

        return view
    }

    private fun goToAddCategoryScreen(btnAddNewCategory: FloatingActionButton) {
        btnAddNewCategory.setOnClickListener {
            val addCategoryFragment = AddCategoryFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, addCategoryFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


}