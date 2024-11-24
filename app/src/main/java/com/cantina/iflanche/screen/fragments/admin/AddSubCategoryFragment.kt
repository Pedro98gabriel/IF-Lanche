package com.cantina.iflanche.screen.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cantina.iflanche.databinding.FragmentAddSubCategoryBinding
import com.cantina.iflanche.screen.HomeActivity
import com.cantina.iflanche.utils.CommonFunctions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddSubCategoryFragment : Fragment() {
    private var _binding: FragmentAddSubCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private var subCategoryName: String? = null // Nome da categoria a ser editada


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference.child("subcategories")
        subCategoryName = arguments?.getString("subCategoryName")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddSubCategoryBinding.inflate(inflater, container, false)
        val view = binding.root
        CommonFunctions.addTextWatcherAndFocusListener(
            listOf(
                binding.tfAddSubCategoryContent to binding.tfAddSubCategory,
            )
        )

        if (subCategoryName != null) {
            (activity as? HomeActivity)?.setAppBarTitle("Editar Subcategoria")
            binding.tfAddSubCategoryContent.setText(subCategoryName)  // Preenche o campo com o nome da categoria
            binding.btnSaveSubCategory.text = "Atualizar"
        } else {
            (activity as? HomeActivity)?.setAppBarTitle("Adicionar Subcategoria")
        }

        binding.btnSaveSubCategory.setOnClickListener {
            binding.progressBarAddSubCategory.visibility = ProgressBar.VISIBLE
            val newCategoryName = binding.tfAddSubCategoryContent.text.toString().trim()

            if (newCategoryName.isNotEmpty()) {
                if (subCategoryName != null) {
                    // Se estamos editando, atualiza a categoria no Firebase
                    updateSubCategoryInFirebase(newCategoryName)
                } else {
                    // Se estamos criando, verifica se já existe e salva a nova categoria
                    checkSubCategoryExists(newCategoryName)
                }
            } else {
                binding.tfAddSubCategory.error = "Por favor, insira um nome de Subcategoria"
                binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
            }
        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkSubCategoryExists(subCategoryName: String) {
        database.orderByValue().equalTo(subCategoryName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        binding.tfAddSubCategory.error = "Subcategoria já existe"
                        binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
                    } else {
                        saveSubCategoryToFirebase(subCategoryName)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        activity,
                        "Erro ao verificar Subcategoria. Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
                }
            })
    }


    private fun saveSubCategoryToFirebase(subCategoryName: String) {
        val lowerCaseSubCategoryName = subCategoryName.lowercase()
        val subCategoryId = database.push().key
        if (subCategoryId != null) {
            database.child(subCategoryId).setValue(lowerCaseSubCategoryName)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            activity,
                            "Subcategoria salva com sucesso",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        binding.tfAddSubCategoryContent.text?.clear()
                    } else {
                        // Falha ao salvar
                        Toast.makeText(
                            activity,
                            "Erro ao salvar Subcategoria. Tente novamente.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
                }
                .addOnFailureListener { exception ->
                    // Caso haja falha em qualquer parte da operação
                    Toast.makeText(
                        activity,
                        "Falha na conexão. Erro: ${exception.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
                }
        } else {
            Toast.makeText(activity, "Erro ao gerar ID para Subcategoria", Toast.LENGTH_SHORT)
                .show()
            binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
        }
    }

    private fun updateSubCategoryInFirebase(newSubCategoryName: String) {
        val lowerCaseSubCategoryName = subCategoryName?.lowercase()
        val lowerCaseNewSubCategoryName = newSubCategoryName.lowercase()

        database.orderByValue().equalTo(lowerCaseSubCategoryName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (subCategorySnapshot in snapshot.children) {
                            subCategorySnapshot.ref.setValue(lowerCaseNewSubCategoryName)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            activity,
                                            "Subcategoria atualizada com sucesso",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.tfAddSubCategoryContent.text?.clear()
                                        requireActivity().supportFragmentManager.popBackStack()
                                    } else {
                                        Toast.makeText(
                                            activity,
                                            "Erro ao atualizar Subcategoria. Tente novamente.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(
                                        activity,
                                        "Falha na conexão. Erro: ${exception.localizedMessage}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
                                }
                        }
                    } else {
                        Toast.makeText(activity, "Subcategoria não encontrada", Toast.LENGTH_SHORT)
                            .show()
                        binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        activity,
                        "Erro ao atualizar Subcategoria. Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBarAddSubCategory.visibility = ProgressBar.GONE
                }
            })
    }


}