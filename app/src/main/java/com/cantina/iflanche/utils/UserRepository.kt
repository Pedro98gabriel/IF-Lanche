package com.cantina.iflanche.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepository(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun registerUser(
        name: String,
        email: String,
        userType: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserInDatabase(
                            it.uid,
                            name,
                            email,
                            userType,
                            password
                        ) { success, message ->
                            callback(success, message)
                        }
                    }
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    private fun saveUserInDatabase(
        userId: String,
        name: String,
        email: String,
        userType: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val userMap = mapOf(
            "name" to name,
            "email" to email,
            "userType" to userType,
            "password" to password,
        )

        val databaseReference = database.getReference("users").child(userId)
        databaseReference.setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "User data saved successfully")
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }
}