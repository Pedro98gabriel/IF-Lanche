package com.cantina.iflanche.utils

import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cantina.iflanche.R

fun AppCompatActivity.configureAppBar(userType: String) {
    val startIcon = findViewById<ImageView>(R.id.start_icon)
    if (userType == "Administrador") {
        startIcon.visibility = View.VISIBLE
    } else {
        startIcon.visibility = View.GONE
    }
}