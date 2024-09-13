package com.cantina.iflanche

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}