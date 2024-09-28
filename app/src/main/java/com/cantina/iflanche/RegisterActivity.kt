package com.cantina.iflanche

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.database.UserRepository
import com.cantina.iflanche.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userType: Array<String> = arrayOf("Aluno", "Funcionário")
    private var userTypeSelected: String? = null

    private lateinit var userRepository: UserRepository

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userRepository = UserRepository(this)

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

        if (password.isEmpty() || password.length < 6) {
            binding.tfPasswordRegister.error = "A senha deve ter pelo menos 6 caracteres"
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

        //Register in firebase
        userRepository.registerUser(name, email, userTypeSelected!!, password) { success, message ->
            if (success) {
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cadastro falhou: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}