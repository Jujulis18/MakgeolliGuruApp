package com.myapp.makgeolliguru.profile

data class Favorite(
    val name: String,
    val imageUrl: String,
    val localisation: String,
    val data: Array<String>?
)
