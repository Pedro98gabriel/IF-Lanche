package com.cantina.iflanche.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object LoadCategories {
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("categories")

    fun loadCategories(callback: (List<String>) -> Unit, onError: (String) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = mutableListOf<String>()

                for (categorySnapshot in snapshot.children) {
                    val category = categorySnapshot.getValue(String::class.java)
                    category?.let {
                        categories.add(it.replaceFirstChar { it.uppercaseChar() })
                    }
                }
                callback(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                val errorMessage = "Erro ao carregar categorias: ${error.message}"
                onError(errorMessage)
            }
        })
    }
}
