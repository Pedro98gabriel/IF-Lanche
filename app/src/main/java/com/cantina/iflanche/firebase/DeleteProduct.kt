package com.cantina.iflanche.firebase

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object DeleteProduct {
    fun deleteProduct(productId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val productRef = database.getReference("products").child(productId)

        // Primeiro, recupera os dados do produto para obter a URL da imagem
        productRef.get()
            .addOnSuccessListener { snapshot ->
                val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                // Se a URL da imagem existir, remova a imagem no Storage
                if (!imageUrl.isNullOrEmpty()) {
                    val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                    storageRef.delete()
                        .addOnSuccessListener {
                            // Depois de remover a imagem, apaga o registro do produto no banco
                            productRef.removeValue()
                                .addOnSuccessListener {
                                    onSuccess()
                                }
                                .addOnFailureListener { exception ->
                                    onError("Erro ao remover o produto do banco: ${exception.message}")
                                }
                        }
                        .addOnFailureListener { exception ->
                            onError("Erro ao remover a imagem: ${exception.message}")
                        }
                } else {
                    // Caso não haja imagem, apaga apenas os dados do produto
                    productRef.removeValue()
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            onError("Erro ao remover o produto do banco: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                onError("Erro ao acessar os dados do produto: ${exception.message}")
            }
    }
}
