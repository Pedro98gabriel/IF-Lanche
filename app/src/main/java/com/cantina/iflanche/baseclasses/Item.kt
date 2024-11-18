package com.cantina.iflanche.baseclasses

import com.google.android.gms.common.GoogleApiAvailability

data class Item(
    val imageUrl: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val subCategory: String = "",
    val availability: String = "",
)
