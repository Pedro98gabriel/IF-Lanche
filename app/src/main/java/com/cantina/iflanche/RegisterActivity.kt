package com.cantina.iflanche

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userType: Array<String> = arrayOf("Aluno", "Funcionário")
    private var userTypeSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUserTypeDropdown()
        setupListeners()
    }

    private fun setupUserTypeDropdown() {
        val adapter = ArrayAdapter(this, R.layout.list_item_dropdowm, userType)
        binding.tfOptionsUserTypeRegister.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            HelperFunctions.clearFocusFromAllFields(
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
            HelperFunctions.clearFocusFromAllFields(
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

        HelperFunctions.addTextWatcherAndFocusListener(
            binding.tfNameRegisterContent,
            binding.tfNameRegister
        )
        HelperFunctions.addTextWatcherAndFocusListener(
            binding.tfEmailRegisterContent,
            binding.tfEmailRegister
        )
        HelperFunctions.addTextWatcherAndFocusListener(
            binding.tfPasswordRegisterContent,
            binding.tfPasswordRegister
        )
        HelperFunctions.addTextWatcherAndFocusListener(
            binding.tfPasswordConfirmRegisterContent,
            binding.tfPasswordConfirmRegister
        )
        HelperFunctions.addTextWatcherAndFocusListener(
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

        if (email.isEmpty() || !HelperFunctions.isValidEmail(email)) {
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

        // Proceed with account creation
        Toast.makeText(this, "Conta criada com sucesso", Toast.LENGTH_SHORT).show()
    }
}