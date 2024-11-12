package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cantina.iflanche.databinding.FragmentAddCategoryBinding
import com.cantina.iflanche.screen.HomeActivity
import com.cantina.iflanche.utils.CommonFunctions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
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
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as? HomeActivity)?.setAppBarTitle("Adicionar Categoria")

        CommonFunctions.addTextWatcherAndFocusListener(
            listOf(
                binding.tfAddCategoryContent to binding.tfAddCategory,
            )
        )

        binding.btnSaveCategory.setOnClickListener {
            binding.progressBarAddCategory.visibility = ProgressBar.VISIBLE

            val categoryName = binding.tfAddCategoryContent.text.toString()
            if (categoryName.isNotEmpty()) {
                checkCategoryExists(categoryName)
            } else {
                binding.tfAddCategory.error = "Por favor, insira um nome de categoria"
                binding.progressBarAddCategory.visibility = ProgressBar.GONE
            }
        }

        return view
    }

    private fun checkCategoryExists(categoryName: String) {
        val lowerCaseCategoryName = categoryName.lowercase()
        database.orderByValue().equalTo(lowerCaseCategoryName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.tfAddCategory.error = "Categoria já existe"
                        binding.progressBarAddCategory.visibility = ProgressBar.GONE
                    } else {
                        saveCategoryToFirebase(lowerCaseCategoryName)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(activity, "Erro ao verificar categoria", Toast.LENGTH_SHORT)
                        .show()
                    binding.progressBarAddCategory.visibility = ProgressBar.GONE
                }
            })
    }

    private fun saveCategoryToFirebase(categoryName: String) {
        val lowerCaseCategoryName = categoryName.lowercase()
        val categoryId = database.push().key
        if (categoryId != null) {
            database.child(categoryId).setValue(lowerCaseCategoryName)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Categoria salva com sucesso", Toast.LENGTH_SHORT)
                            .show()
                        binding.tfAddCategoryContent.text?.clear()
                    } else {
                        Toast.makeText(activity, "Erro ao salvar categoria", Toast.LENGTH_SHORT)
                            .show()
                    }
                    binding.progressBarAddCategory.visibility = ProgressBar.GONE
                }
        }
    }
}