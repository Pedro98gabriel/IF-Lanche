package com.cantina.iflanche

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cantina.iflanche.databinding.ActivityHomeBinding
import com.cantina.iflanche.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userType = intent.getStringExtra("userType")
        if (userType == "Administrador") {
            // Mostrar elementos específicos para administradores
            binding.testUserType.text = "Sou um administrador"
        } else {
            // Ocultar elementos específicos para administradores
            binding.testUserType.text = "Sou um usuario comum"
        }

    }
}