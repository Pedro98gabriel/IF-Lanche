package com.cantina.iflanche.screen

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.ActivityHomeBinding
import com.cantina.iflanche.utils.configureAppBar

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userType = intent.getStringExtra("userType") ?: "Aluno"
        setSupportActionBar(binding.toolbar)

        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        //configureAppBar(userType)

//        if (userType == "Administrador") {
//            // Mostrar elementos específicos para administradores
//            binding.testUserType.text = "Sou um administrador"
//        } else {
//            // Ocultar elementos específicos para administradores
//
//            binding.testUserType.text = "Sou um usuario comum"
//        }

    }
}