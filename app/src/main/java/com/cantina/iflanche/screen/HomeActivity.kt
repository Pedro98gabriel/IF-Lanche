package com.cantina.iflanche.screen

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppBarTitle("IF-Lanche")

        val toolbar: Toolbar = binding.toolbar

        appBarNotificationIcon()

        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val userType = intent.getStringExtra("userType") ?: "Aluno"

//        if (userType == "Administrador") {
//            // Mostrar elementos específicos para administradores
//            binding.testUserType.text = "Sou um administrador"
//        } else {
//            // Ocultar elementos específicos para administradores
//
//            binding.testUserType.text = "Sou um usuario comum"
//        }

    }

    fun setAppBarTitle(title: String) {
        val appBarText = binding.toolbarTitle
        appBarText.text = title
    }

    private fun appBarNotificationIcon() {
        val notificationIconAppBar: ImageView = binding.endIconNotification
        notificationIconAppBar.setOnClickListener {
            Toast.makeText(this, "Não implementado", Toast.LENGTH_SHORT).show()
        }
    }
}