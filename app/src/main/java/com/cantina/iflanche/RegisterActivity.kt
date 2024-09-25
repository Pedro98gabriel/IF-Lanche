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
    private lateinit var registerNameTextInput: TextInputLayout
    private lateinit var registerNameTextInputContent: TextInputEditText
    private lateinit var registerEmailTextInput: TextInputLayout
    private lateinit var registerEmailTextInputContent: TextInputEditText
    private lateinit var registerPassWordTextInput: TextInputLayout
    private lateinit var registerPassWordTextInputContent: TextInputEditText
    private lateinit var registerConfirmPassWordTextInput: TextInputLayout
    private lateinit var registerConfirmPassWordTextInputContent: TextInputEditText
    private lateinit var registerButton: Button
    private val userType: Array<String> = arrayOf("Aluno", "Funcionário")
    private lateinit var adapter: ArrayAdapter<String>
    private var userTypeSelected: String? = null

    private lateinit var registerUserType: TextInputLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerUserType = binding.tfDropdownUserTypeRegister

        registerNameTextInput = binding.tfNameRegister
        registerNameTextInputContent = binding.tfNameRegisterContent
        registerEmailTextInput = binding.tfEmailRegister
        registerEmailTextInputContent = binding.tfEmailRegisterContent
        registerPassWordTextInput = binding.tfPasswordRegister
        registerPassWordTextInputContent = binding.tfPasswordRegisterContent
        registerConfirmPassWordTextInput = binding.tfPasswordConfirmRegister
        registerConfirmPassWordTextInputContent = binding.tfPasswordConfirmRegisterContent

        registerButton = binding.btnRegister

        dropdownUserTypeConfiguration(binding)
        closeActivityOnClick(binding)

        registerButton.setOnClickListener {
            clearFocusFromAllFields()
            createUserAccount()
        }

        binding.root.setOnTouchListener { _, _ ->
            clearFocusFromAllFields()
            false
        }

        addTextWatcherAndFocusListener(registerNameTextInputContent, binding.tfNameRegister)
        addTextWatcherAndFocusListener(registerEmailTextInputContent, binding.tfEmailRegister)
        addTextWatcherAndFocusListener(registerPassWordTextInputContent, binding.tfPasswordRegister)
        addTextWatcherAndFocusListener(
            registerConfirmPassWordTextInputContent,
            binding.tfPasswordConfirmRegister
        )
        addTextWatcherAndFocusListenerToDropdown(
            binding.tfOptionsUserTypeRegister,
            binding.tfDropdownUserTypeRegister
        )

    }

    private fun createUserAccount() {
        val name = registerNameTextInputContent.text.toString().trim()
        val email = registerEmailTextInputContent.text.toString().trim()
        val password = registerPassWordTextInputContent.text.toString().trim()
        val confirmPassword = registerConfirmPassWordTextInputContent.text.toString().trim()

        var isValid = true

        if (name.isEmpty()) {
            registerNameTextInput.error = "Por favor, preencha o nome"
            isValid = false
        }

        if (email.isEmpty() || !isValidEmail(email)) {
            registerEmailTextInput.error = "Email inválido"
            isValid = false
        }

        if (password.isEmpty()) {
            registerPassWordTextInput.error = "Por favor, preencha a senha"
            isValid = false
        }

        if (confirmPassword.isEmpty() || password != confirmPassword) {
            registerConfirmPassWordTextInput.error = "As senhas não coincidem"
            isValid = false
        }

        if (userTypeSelected == null) {
            registerUserType.error = "Selecione um tipo de usuário"
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

    private fun closeActivityOnClick(binding: ActivityRegisterBinding) {
        binding.imbBack.setOnClickListener {
            finish()
        }
    }

    private fun dropdownUserTypeConfiguration(binding: ActivityRegisterBinding) {
        val autoComplete: AutoCompleteTextView = binding.tfOptionsUserTypeRegister
        adapter = ArrayAdapter(this, R.layout.list_item_dropdowm, userType)

        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->
                userTypeSelected = adapterView.getItemAtPosition(position).toString()
//                Toast.makeText(this, "Item: $itemSelected", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addTextWatcherAndFocusListener(
        editText: TextInputEditText,
        textInputLayout: TextInputLayout
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.error = null
            }
        }
    }

    private fun addTextWatcherAndFocusListenerToDropdown(
        autoCompleteTextView: AutoCompleteTextView,
        textInputLayout: TextInputLayout
    ) {
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.error = null
            }
        }
    }

    private fun clearFocusFromAllFields() {
        registerNameTextInput.clearFocus()
        registerEmailTextInputContent.clearFocus()
        registerPassWordTextInputContent.clearFocus()
        registerConfirmPassWordTextInputContent.clearFocus()
        registerUserType.clearFocus()
    }
}