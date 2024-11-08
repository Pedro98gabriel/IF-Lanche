package com.cantina.iflanche

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.databinding.ActivityMainBinding
import com.cantina.iflanche.utils.UserLoginRepository


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userLoginRepository: UserLoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //start the activity automaticcaly for test purposes
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userType", "Aluno") // Pass any required data
        startActivity(intent)
        finish() // Close MainActivity
        //finish test activity

        userLoginRepository = UserLoginRepository(this)

        redirectToRegisterScreen(binding)

        CommonFunctions.addTextWatcherAndFocusListener(
            listOf(
                binding.tfEmailLoginContent to binding.tfEmailLogin,
                binding.tfPasswordLoginContent to binding.tfPasswordLogin
            )
        )

        loginAndRedirect()
    }

    private fun loginAndRedirect() {
        binding.btnEnterLogin.setOnClickListener {
            val email = binding.tfEmailLoginContent.text.toString().trim()
            val password = binding.tfPasswordLoginContent.text.toString().trim()

            if (email.isEmpty() || !CommonFunctions.isValidEmail(email)) {
                binding.tfEmailLogin.error = "Email inválido"
            }

            if (password.isEmpty() || password.length < 6) {
                binding.tfPasswordLogin.error = "Senha inválida"
            }

            userLoginRepository.loginUser(email, password) { success, message ->
                if (!success) {
                    // Handle login failure if needed
                }
            }
        }
    }

    private fun redirectToRegisterScreen(binding: ActivityMainBinding) {
        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            Log.d("MainActivity", "Starting RegisterActivity")
            startActivity(intent)
        }
    }
}