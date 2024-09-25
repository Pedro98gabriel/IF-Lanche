package com.cantina.iflanche

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userType: Array<String> = arrayOf("Aluno", "Funcionário")
    private var userTypeSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        val adapter = ArrayAdapter(this, R.layout.list_item_dropdowm, userType)
        binding.tfOptionsUserTypeRegister.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            clearFocusFromAllFields()
            createUserAccount()
        }

        binding.root.setOnTouchListener { _, _ ->
            clearFocusFromAllFields()
            false
        }

        addTextWatcherAndFocusListener(binding.tfNameRegisterContent, binding.tfNameRegister)
        addTextWatcherAndFocusListener(binding.tfEmailRegisterContent, binding.tfEmailRegister)
        addTextWatcherAndFocusListener(
            binding.tfPasswordRegisterContent,
            binding.tfPasswordRegister
        )
        addTextWatcherAndFocusListener(
            binding.tfPasswordConfirmRegisterContent,
            binding.tfPasswordConfirmRegister
        )
        addTextWatcherAndFocusListener(
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

        if (email.isEmpty() || !isValidEmail(email)) {
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

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun addTextWatcherAndFocusListener(view: View, textInputLayout: TextInputLayout) {
        when (view) {
            is TextInputEditText -> {
                view.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        textInputLayout.error = null
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                view.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        textInputLayout.error = null
                    }
                }
            }

            is AutoCompleteTextView -> {
                view.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        textInputLayout.error = null
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })

                view.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        textInputLayout.error = null
                    }
                }
            }
        }
    }

    private fun clearFocusFromAllFields() {
        val fields = listOf(
            binding.tfNameRegisterContent,
            binding.tfEmailRegisterContent,
            binding.tfPasswordRegisterContent,
            binding.tfPasswordConfirmRegisterContent,
            binding.tfOptionsUserTypeRegister
        )
        fields.forEach { it.clearFocus() }
    }
}