package com.ar.of_pro.models

data class UserModel(
    val name: String,
    val lastName: String,
    val address: String,
    val location: String,
    val mail: String,
    val password: String,
    val phone: Int,
    val rating: Double,
    val ratingQuantity: Int,
    val userType: String,
    val bio: String,
    val imageUrl: String
) {
    constructor() : this("", "", "", "", "", "", 0, 0.0, 0, "", "", "")
}
