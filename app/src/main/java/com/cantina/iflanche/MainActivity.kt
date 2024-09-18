package com.cantina.iflanche

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        redirectToRegisterScreen(binding)

        loginAndRedirect(binding)
    }

    private fun loginAndRedirect(binding: ActivityMainBinding) {
        binding.btnEnterLogin.setOnClickListener() {
            val intent = Intent(this, HomeActivity::class.java)
            Log.d("MainActivity", "Starting HomeActivity")
            startActivity(intent)
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