package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cantina.iflanche.R
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
    private var categoryName: String? = null // Nome da categoria a ser editada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference.child("categories")
        categoryName = arguments?.getString("categoryName")
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

        // Modificar o título da AppBar conforme a operação
        if (categoryName != null) {
            (activity as? HomeActivity)?.setAppBarTitle("Editar Categoria")
            binding.tfAddCategoryContent.setText(categoryName)  // Preenche o campo com o nome da categoria
            binding.btnSaveCategory.text = "Atualizar"
        } else {
            (activity as? HomeActivity)?.setAppBarTitle("Adicionar Categoria")
        }

        binding.btnSaveCategory.setOnClickListener {
            binding.progressBarAddCategory.visibility = ProgressBar.VISIBLE
            val newCategoryName = binding.tfAddCategoryContent.text.toString().trim()

            if (newCategoryName.isNotEmpty()) {
                if (categoryName != null) {
                    // Se estamos editando, atualiza a categoria no Firebase
                    updateCategoryInFirebase(newCategoryName)
                } else {
                    // Se estamos criando, verifica se já existe e salva a nova categoria
                    checkCategoryExists(newCategoryName)
                }
            } else {
                binding.tfAddCategory.error = "Por favor, insira um nome de categoria"
                binding.progressBarAddCategory.visibility = ProgressBar.GONE
            }
        }

        return view
    }

    private fun checkCategoryExists(categoryName: String) {
        database.orderByValue().equalTo(categoryName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.tfAddCategory.error = "Categoria já existe"
                        binding.progressBarAddCategory.visibility = ProgressBar.GONE
                    } else {
                        saveCategoryToFirebase(categoryName)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        activity,
                        "Erro ao verificar categoria. Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
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
                        // Categoria salva com sucesso
                        Toast.makeText(activity, "Categoria salva com sucesso", Toast.LENGTH_SHORT)
                            .show()
                        binding.tfAddCategoryContent.text?.clear()
                    } else {
                        // Falha ao salvar
                        Toast.makeText(
                            activity,
                            "Erro ao salvar categoria. Tente novamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.progressBarAddCategory.visibility = ProgressBar.GONE
                }
                .addOnFailureListener { exception ->
                    // Caso haja falha em qualquer parte da operação
                    Toast.makeText(
                        activity,
                        "Falha na conexão. Erro: ${exception.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarAddCategory.visibility = ProgressBar.GONE
                }
        } else {
            // Caso não seja possível gerar um ID para a categoria
            Toast.makeText(activity, "Erro ao gerar ID para categoria", Toast.LENGTH_SHORT).show()
            binding.progressBarAddCategory.visibility = ProgressBar.GONE
        }
    }

    private fun updateCategoryInFirebase(newCategoryName: String) {
        val lowerCaseCategoryName = categoryName?.lowercase()
        val lowerCaseNewCategoryName = newCategoryName.lowercase()

        // Encontra a categoria pelo nome antigo em minúsculas
        database.orderByValue().equalTo(lowerCaseCategoryName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Atualiza o valor da categoria
                        for (categorySnapshot in snapshot.children) {
                            categorySnapshot.ref.setValue(lowerCaseNewCategoryName)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            activity,
                                            "Categoria atualizada com sucesso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.tfAddCategoryContent.text?.clear()
                                        requireActivity().supportFragmentManager.popBackStack()
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "Erro ao atualizar categoria. Tente novamente.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    binding.progressBarAddCategory.visibility = ProgressBar.GONE
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(
                                        activity,
                                        "Falha na conexão. Erro: ${exception.localizedMessage}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressBarAddCategory.visibility = ProgressBar.GONE
                                }
                        }
                    } else {
                        Toast.makeText(activity, "Categoria não encontrada", Toast.LENGTH_SHORT)
                            .show()
                        binding.progressBarAddCategory.visibility = ProgressBar.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        activity,
                        "Erro ao atualizar categoria. Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarAddCategory.visibility = ProgressBar.GONE
                }
            })
    }
}
