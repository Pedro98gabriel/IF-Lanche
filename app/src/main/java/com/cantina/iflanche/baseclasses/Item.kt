package com.cantina.iflanche.baseclasses

data class Item(
    var id: String? = "",
    val imageUrl: String = "",
    val name: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val subCategory: String = "",
    val availability: String = "",
)
