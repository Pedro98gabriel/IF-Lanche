package com.cantina.iflanche.screen

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.ActivityHomeBinding
import com.cantina.iflanche.screen.fragments.admin.CategoryFragment
import com.cantina.iflanche.screen.fragments.HomeFragment
import com.cantina.iflanche.screen.fragments.admin.ProductFragment
import com.cantina.iflanche.screen.fragments.admin.SubCaregoryFragment
import com.cantina.iflanche.screen.fragments.student.ConfigStudentFragment
import com.cantina.iflanche.screen.fragments.student.ProfileStudentFragment
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userType = intent.getStringExtra("userType") ?: "Aluno"
        if (userType == "Aluno") {
            Toast.makeText(this, "Logado como aluno", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Logado como Administrador", Toast.LENGTH_SHORT).show()
        }

        val toolbar: Toolbar = binding.toolbar

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (userType == "Aluno") {
            navigationView.inflateMenu(R.menu.nav_menu_student)
        } else {
            navigationView.inflateMenu(R.menu.nav_menu_admin)
        }

        navigationView.setNavigationItemSelectedListener { item ->

            if (userType == "Aluno") {
                drawerMenuItemStudent(item)
            } else {
                drawerMenuItemAdmin(item)
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
            if (userType == "Aluno") {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                navigationView.setCheckedItem(R.id.nav_home_student)
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                navigationView.setCheckedItem(R.id.nav_home_admin)
            }
        }

    }

    private fun drawerMenuItemAdmin(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_home_admin -> {
                setAppBarTitle("IF-Lanche")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            }

            R.id.nav_product_admin -> {
                setAppBarTitle("Cadastrar Produto")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProductFragment())
                    .commit()
            }

            R.id.nav_category_admin -> {
                setAppBarTitle("Cadastrar Categoria")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CategoryFragment())
                    .commit()
            }

            R.id.nav_subcategory_admin -> {
                setAppBarTitle("Cadastrar Subcategoria")

                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        SubCaregoryFragment()
                    )
                    .commit()
            }

            R.id.nav_exit_admin -> {
                Toast.makeText(this, "Não implementado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun drawerMenuItemStudent(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_home_student -> {
                setAppBarTitle("IF-Lanche")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .commit()
            }

            R.id.nav_profile_student -> {
                setAppBarTitle("Perfil de Usuário")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileStudentFragment())
                    .commit()
            }

            R.id.nav_config_student -> {
                setAppBarTitle("Configurações")
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ConfigStudentFragment())
                    .commit()
            }

            R.id.nav_exit_student -> {
                Toast.makeText(this, "Não implementado", Toast.LENGTH_SHORT).show()
            }
        }
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