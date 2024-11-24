package com.cantina.iflanche.screen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.databinding.ActivityMainBinding
import com.cantina.iflanche.firebase.UserLoginRepository
import com.cantina.iflanche.utils.CommonFunctions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userLoginRepository: UserLoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verifica se o usuário está logado
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Redireciona para a tela principal
            val intent = Intent(this, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } else {
            // Exibe a tela de login
            setContentView(binding.root) // Ajuste para o layout da tela de login
        }

        userLoginRepository = UserLoginRepository(this)

        redirectToRegisterScreen(binding)

        CommonFunctions.addTextWatcherAndFocusListener(
            listOf(
                binding.tfEmailLoginContent to binding.tfEmailLogin,
                binding.tfPasswordLoginContent to binding.tfPasswordLogin
            )
        )

        loginAndRedirect()
        forgotPassword()
    }

    private fun forgotPassword() {
        binding.txtForgotPasswordLogin.setOnClickListener {
            Toast.makeText(this, "Não implementado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginAndRedirect() {
        binding.btnEnterLogin.setOnClickListener {
            val email = binding.tfEmailLoginContent.text.toString().trim()
            val password = binding.tfPasswordLoginContent.text.toString().trim()

            userLoginRepository.loginUser(email, password) { success, message ->
                if (!success) {
                    binding.tfEmailLogin.error = " "
                    binding.tfPasswordLogin.error = "Email ou senha incorretos"
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