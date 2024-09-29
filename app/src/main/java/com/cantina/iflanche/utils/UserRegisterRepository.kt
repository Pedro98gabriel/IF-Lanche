package com.cantina.iflanche.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRegisterRepository(private val context: Context) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun registerUser(
        name: String,
        email: String,
        userType: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user
                user?.let {
                    val hashedPassword = PasswordUtils.hashPassword(password)
                    saveUserInDatabase(
                        it.uid,
                        name,
                        email,
                        userType,
                        hashedPassword
                    ) { success, message ->
                        callback(success, message)
                    }
                } ?: run {
                    callback(false, "User registration failed")
                }
            } catch (e: Exception) {
                callback(false, e.message)
            }
        }
    }


    private fun saveUserInDatabase(
        userId: String,
        name: String,
        email: String,
        userType: String,
        hashedPassword: String,
        callback: (Boolean, String?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userMap = mapOf(
                    "name" to name,
                    "email" to email,
                    "userType" to userType,
                    "password" to hashedPassword,
                )

                val databaseReference = database.getReference("users").child(userId)
                databaseReference.setValue(userMap).await()
                withContext(Dispatchers.Main) {
                    callback(true, "User data saved successfully")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(false, e.message)
                }
            }
        }
    }
}