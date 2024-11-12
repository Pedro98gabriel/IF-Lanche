package com.cantina.iflanche.screen

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.ActivityHomeBinding
import com.cantina.iflanche.screen.fragments.CategoryFragment
import com.cantina.iflanche.screen.fragments.HomeFragment
import com.cantina.iflanche.screen.fragments.ProductFragment
import com.cantina.iflanche.screen.fragments.SubCaregoryFragment
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppBarTitle("IF-Lanche")

        val toolbar: Toolbar = binding.toolbar
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    setAppBarTitle("IF-Lanche")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }

                R.id.nav_product -> {
                    setAppBarTitle("Cadastrar Produto")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProductFragment())
                        .commit()
                }

                R.id.nav_category -> {
                    setAppBarTitle("Cadastrar Categoria")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CategoryFragment())
                        .commit()
                }

                R.id.nav_subcategory -> {
                    setAppBarTitle("Cadastrar Subcategoria")

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            SubCaregoryFragment()
                        )
                        .commit()
                }

                R.id.nav_exit -> {
                    Toast.makeText(this, "Não implementado", Toast.LENGTH_SHORT).show()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

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


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }


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