package com.ar.of_pro.models

data class UserModel(
    val name: String,
    val lastName: String,
    val location: String,
    val phone: Int,
    val bio: String,
) {
    constructor() : this("", "", "", 0, "")
}
