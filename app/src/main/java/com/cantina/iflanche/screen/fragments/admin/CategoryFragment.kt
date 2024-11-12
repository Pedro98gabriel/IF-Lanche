package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.FragmentRegisterCategoryBinding
import com.cantina.iflanche.firebase.LoadCategories
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CategoryFragment : Fragment() {

    private var _binding: FragmentRegisterCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference.child("categories")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        val btnAddNewCategory = binding.fabAddCategory
        goToAddCategoryScreen(btnAddNewCategory)

        loadCategories()


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

    private fun loadCategories() {
        LoadCategories.loadCategories { categories ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categories
            )
            binding.tfOptionsCategorySelect.setAdapter(adapter)
        }
    }

}