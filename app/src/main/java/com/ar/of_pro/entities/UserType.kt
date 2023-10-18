package com.ar.of_pro.entities

class UserType {

    val userTypeList = listOf("CLIENTE", "PROVEEDOR")

    public fun getList(): List<String> {
        return userTypeList
    }

}