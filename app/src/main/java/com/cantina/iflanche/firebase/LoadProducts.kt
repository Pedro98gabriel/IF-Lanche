package com.cantina.iflanche.firebase

import android.util.Log
import com.cantina.iflanche.baseclasses.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object LoadProducts {

    private val database = FirebaseDatabase.getInstance().getReference("products")

    fun loadProdutos(callback: (Map<String, List<Item>>) -> Unit, onError: (String) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val subcategoryMap = mutableMapOf<String, MutableList<Item>>()
                for (produtoSnapshot in snapshot.children) {
                    val produto = produtoSnapshot.getValue(Item::class.java)
                    if (produto != null) {
                        produto.id = produtoSnapshot.key ?: ""
                        val subcategory = produto.subCategory
                        if (subcategoryMap.containsKey(subcategory)) {
                            subcategoryMap[subcategory]?.add(produto)
                        } else {
                            subcategoryMap[subcategory] = mutableListOf(produto)
                        }
                    }
                }
                val filteredSubcategoryMap = subcategoryMap.filter { it.value.isNotEmpty() }
                Log.d("LoadProducts", "Filtered Subcategory Map: $filteredSubcategoryMap")
                callback(filteredSubcategoryMap)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
                Log.e("LoadProducts", "Error loading products: ${error.message}")
            }
        })
    }
}