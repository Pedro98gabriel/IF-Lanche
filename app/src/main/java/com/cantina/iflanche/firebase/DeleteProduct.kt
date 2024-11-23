package com.cantina.iflanche.firebase

import com.google.firebase.database.FirebaseDatabase

object DeleteProduct {
    fun deleteProduct(productId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val productRef = database.getReference("products").child(productId)

        productRef.removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception.message ?: "Error deleting product")
            }
    }
}