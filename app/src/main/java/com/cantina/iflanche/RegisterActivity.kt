package com.cantina.iflanche

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.databinding.ActivityRegisterBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dropdownUserTypeConfiguration(binding)
        closeActivityOnClick(binding)
        createUserAccount(binding)

    }

    private fun createUserAccount(binding: ActivityRegisterBinding) {
        binding.btnRegister.setOnClickListener {
            Toast.makeText(this, "Clicou no botão", Toast.LENGTH_SHORT).show()

            //test to save to firebase
            val database = Firebase.database
            val myRef = database.getReference("message")
            myRef.setValue("Hello, World!")

        }
    }

    private fun closeActivityOnClick(binding: ActivityRegisterBinding) {
        binding.imbBack.setOnClickListener {
            finish()
        }
    }

    private fun dropdownUserTypeConfiguration(binding: ActivityRegisterBinding) {
        val userType: Array<String> = arrayOf("Aluno", "Funcionário")
        val autoComplete: AutoCompleteTextView = binding.tfOptionsUserTypeRegister
        val adapter = ArrayAdapter(this, R.layout.list_item_dropdowm, userType)

        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, position, _ ->

                val itemSelected = adapterView.getItemAtPosition(position).toString()
                Toast.makeText(this, "Item: $itemSelected", Toast.LENGTH_SHORT).show()
            }
    }

}