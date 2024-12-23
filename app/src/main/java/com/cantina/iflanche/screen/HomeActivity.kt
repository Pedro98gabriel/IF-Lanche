package com.cantina.iflanche.screen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cantina.iflanche.R
import com.cantina.iflanche.databinding.ActivityHomeBinding
import com.cantina.iflanche.firebase.LoadCategories
import com.cantina.iflanche.screen.fragments.HomeFragment
import com.cantina.iflanche.screen.fragments.admin.CategoryFragment
import com.cantina.iflanche.screen.fragments.admin.ProductFragment
import com.cantina.iflanche.screen.fragments.admin.SubcategoryFragment
import com.cantina.iflanche.screen.fragments.student.ConfigStudentFragment
import com.cantina.iflanche.screen.fragments.student.ProfileStudentFragment
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    var categories: List<String>? = null
    private var userType: String? = null
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        userType =
            sharedPreferences.getString("userType", null) ?: intent.getStringExtra("userType")

        loadCategories()
        appBarNotificationIcon()

        val toolbar: Toolbar = binding.toolbar

        // Configura o menu de navegação de acordo com o tipo de usuário
        val navigationView = binding.navView
        if (userType == "Aluno") {
            navigationView.inflateMenu(R.menu.nav_menu_student)

        } else {
            navigationView.inflateMenu(R.menu.nav_menu_admin)
        }

        // Configura o listener para o menu de navegação
        navigationView.setNavigationItemSelectedListener { item ->
            if (userType == "Aluno") {
                drawerMenuItemStudent(item)
            } else {
                drawerMenuItemAdmin(item)
            }

            binding.drawerLayout.closeDrawers() // Fecha o menu após a seleção
            true
        }

        // Configura o botão de navegação lateral (hamburguer)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Sempre carrega o HomeFragment para "Aluno" e "Admin"
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), "IF-Lanche")
            // Marca o item correto como selecionado com base no tipo de usuário
            if (userType == "Aluno") {
                navigationView.setCheckedItem(R.id.nav_home_student)  // Marca "Home" de Aluno como selecionado
            } else {
                navigationView.setCheckedItem(R.id.nav_home_admin)  // Marca "Home" de Admin como selecionado
            }
        }

    }

    private fun loadCategories() {
        LoadCategories.loadCategories(
            callback = { categories ->
                this.categories = categories
            },
            onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        setAppBarTitle(title)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun drawerMenuItemAdmin(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_home_admin -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadFragment(HomeFragment(), "IF-Lanche")
            }

            R.id.nav_product_admin -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadFragment(ProductFragment(), "Cadastrar Produto")
            }

            R.id.nav_category_admin -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadFragment(CategoryFragment(), "Gerenciar Categoria")
            }

            R.id.nav_subcategory_admin -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadFragment(SubcategoryFragment(), "Cadastrar Subcategoria")
            }

            R.id.nav_exit_admin -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun drawerMenuItemStudent(item: MenuItem) {
        when (item.itemId) {
            R.id.nav_home_student -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadFragment(HomeFragment(), "IF-Lanche")
            }

            R.id.nav_profile_student -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadFragment(ProfileStudentFragment(), "Perfil de Usuário")
            }

            R.id.nav_config_student -> {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                loadFragment(ConfigStudentFragment(), "Configurações")
            }

            R.id.nav_exit_student -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
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

    fun getUserType(): String? {
        return userType
    }

}