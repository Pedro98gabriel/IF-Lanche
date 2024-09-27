package com.cantina.iflanche

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userType: Array<String> = arrayOf("Aluno", "Funcionário")
    private var userTypeSelected: String? = null


    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setupUserTypeDropdown()
        setupListeners()
    }

    private fun setupUserTypeDropdown() {
        val adapter = ArrayAdapter(this, R.layout.list_item_dropdowm, userType)
        binding.tfOptionsUserTypeRegister.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            CommonFunctions.clearFocusFromAllFields(
                listOf(
                    binding.tfNameRegisterContent,
                    binding.tfEmailRegisterContent,
                    binding.tfPasswordRegisterContent,
                    binding.tfPasswordConfirmRegisterContent,
                    binding.tfOptionsUserTypeRegister
                )
            )
            createUserAccount()
        }

        binding.root.setOnTouchListener { _, _ ->
            CommonFunctions.clearFocusFromAllFields(
                listOf(
                    binding.tfNameRegisterContent,
                    binding.tfEmailRegisterContent,
                    binding.tfPasswordRegisterContent,
                    binding.tfPasswordConfirmRegisterContent,
                    binding.tfOptionsUserTypeRegister
                )
            )
            false
        }

        CommonFunctions.addTextWatcherAndFocusListener(
            binding.tfNameRegisterContent,
            binding.tfNameRegister
        )
        CommonFunctions.addTextWatcherAndFocusListener(
            binding.tfEmailRegisterContent,
            binding.tfEmailRegister
        )
        CommonFunctions.addTextWatcherAndFocusListener(
            binding.tfPasswordRegisterContent,
            binding.tfPasswordRegister
        )
        CommonFunctions.addTextWatcherAndFocusListener(
            binding.tfPasswordConfirmRegisterContent,
            binding.tfPasswordConfirmRegister
        )
        CommonFunctions.addTextWatcherAndFocusListener(
            binding.tfOptionsUserTypeRegister,
            binding.tfDropdownUserTypeRegister
        )

        binding.tfOptionsUserTypeRegister.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                userTypeSelected = adapterView.getItemAtPosition(position).toString()
            }

        binding.imbBack.setOnClickListener {
            finish()
        }
    }

    private fun createUserAccount() {
        val name = binding.tfNameRegisterContent.text.toString().trim()
        val email = binding.tfEmailRegisterContent.text.toString().trim()
        val password = binding.tfPasswordRegisterContent.text.toString().trim()
        val confirmPassword = binding.tfPasswordConfirmRegisterContent.text.toString().trim()

        var isValid = true

        if (name.isEmpty()) {
            binding.tfNameRegister.error = "Por favor, preencha o nome"
            isValid = false
        }

        if (email.isEmpty() || !CommonFunctions.isValidEmail(email)) {
            binding.tfEmailRegister.error = "Email inválido"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.tfPasswordRegister.error = "Por favor, preencha a senha"
            isValid = false
        }

        if (confirmPassword.isEmpty() || password != confirmPassword) {
            binding.tfPasswordConfirmRegister.error = "As senhas não coincidem"
            isValid = false
        }

        if (userTypeSelected == null) {
            binding.tfDropdownUserTypeRegister.error = "Selecione um tipo de usuário"
            isValid = false
        }

        if (!isValid) {
            return
        }

        registerUser(name, email, userTypeSelected!!, password)

    }

    private fun registerUser(name: String, email: String, userType: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Salvar informações do usuário no Realtime Database
                    user?.let {
                        saveUserInDatabase(it.uid, name, email, userType, password)
                    }
                    Toast.makeText(
                        this,
                        "Cadastro realizado com sucesso: ${user?.email}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Cadastro falhou: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun saveUserInDatabase(
        userId: String,
        name: String,
        email: String,
        userType: String,
        password: String
    ) {
        val userMap = mapOf(
            "id" to userId,
            "name" to name,
            "email" to email,
            "userType" to userType,
            "password" to password,
        )

        val databaseReference = database.getReference("users").child(userId)
        databaseReference.setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Dados salvos no Realtime Database", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        this,
                        "Falha ao salvar dados: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

