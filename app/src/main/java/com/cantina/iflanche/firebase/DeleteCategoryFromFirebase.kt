package com.cantina.iflanche.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object DeleteCategory {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("categories")

    private val databaseSubCategory: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("subcategories")

    fun deleteCategory(categorySelected: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        database.orderByValue().equalTo(categorySelected.lowercase()).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (categorySnapshot in snapshot.children) {
                        categorySnapshot.ref.removeValue().addOnSuccessListener {
                            onSuccess()
                        }.addOnFailureListener { error ->
                            // Falha na remoção
                            onError("Erro ao remover a categoria: ${error.message}")
                        }
                    }
                } else {
                    onError("Categoria \"$categorySelected\" não encontrada.")
                }
            }.addOnFailureListener { error ->
                onError("Erro ao consultar o Firebase: ${error.message}")
            }
    }

    fun deleteSubCategory(
        subCategorySelected: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        databaseSubCategory.orderByValue().equalTo(subCategorySelected.lowercase()).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (subCategorySnapshot in snapshot.children) {
                        subCategorySnapshot.ref.removeValue().addOnSuccessListener {
                            onSuccess()
                        }.addOnFailureListener { error ->
                            // Falha na remoção
                            onError("Erro ao remover a Subcategoria: ${error.message}")
                        }
                    }
                } else {
                    onError("Subcategoria \"$subCategorySelected\" não encontrada.")
                }
            }.addOnFailureListener { error ->
                onError("Erro ao consultar o Firebase: ${error.message}")
            }
    }
}