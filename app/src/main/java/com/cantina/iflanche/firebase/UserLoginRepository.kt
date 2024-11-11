package com.cantina.iflanche.firebase

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.cantina.iflanche.screen.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserLoginRepository(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun loginUser(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT)
                .show()
            callback(false, "Campos vazios")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userRef = database.getReference("users").child(userId)
                        userRef.get().addOnSuccessListener { dataSnapshot ->
                            val userType =
                                dataSnapshot.child("userType").getValue(String::class.java)
                            val intent = Intent(context, HomeActivity::class.java)
                            intent.putExtra("userType", userType)
                            context.startActivity(intent)
                            callback(true, null)
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Falha ao recuperar tipo de usuário",
                                Toast.LENGTH_SHORT
                            ).show()
                            callback(false, "Falha ao recuperar tipo de usuário")
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Falha no login: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(false, task.exception?.message)
                }
            }
    }
}