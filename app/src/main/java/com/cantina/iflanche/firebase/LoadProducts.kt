package com.cantina.iflanche.firebase

import android.util.Log
import com.cantina.iflanche.baseclasses.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object LoadProducts {

    private val database = FirebaseDatabase.getInstance().getReference("products")

    fun loadProdutos(callback: (List<Item>) -> Unit, onError: (String) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val produtosList = mutableListOf<Item>()
                for (produtoSnapshot in snapshot.children) {
                    val produto = produtoSnapshot.getValue(Item::class.java)
                    if (produto != null) {
                        produtosList.add(produto)
                    }
                }
                callback(produtosList)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
                Log.e("LoadProducts", "Error loading products: ${error.message}")
            }
        })
    }
}