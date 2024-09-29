package com.cantina.iflanche

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.utils.UserRepository
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
                ),
                this
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
        binding.tfOptionsUserTypeRegister.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    CommonFunctions.hideKeyboard(view, this)
                }
            }

        binding.tfOptionsUserTypeRegister.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                userTypeSelected = adapterView.getItemAtPosition(position).toString()
            }

        binding.tfOptionsUserTypeRegister.setOnClickListener {
            binding.tfDropdownUserTypeRegister.error = null
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

        binding.progressBarRegister.visibility = ProgressBar.VISIBLE

        //Register in firebase
        userRepository.registerUser(name, email, userTypeSelected!!, password) { success, message ->
            binding.progressBarRegister.visibility = ProgressBar.GONE

            if (success) {
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                clearInputFields()
            } else {
                if (message?.contains("email address is already in use") == true) {
                    binding.tfEmailRegister.error = "Email já cadastrado"
                } else {
                    Toast.makeText(this, "Cadastro falhou: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearInputFields() {
        binding.tfNameRegisterContent.text?.clear()
        binding.tfEmailRegisterContent.text?.clear()
        binding.tfPasswordRegisterContent.text?.clear()
        binding.tfPasswordConfirmRegisterContent.text?.clear()
        binding.tfOptionsUserTypeRegister.text?.clear()
        userTypeSelected = null
    }
}