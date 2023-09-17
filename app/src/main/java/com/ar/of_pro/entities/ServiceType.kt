package com.ar.of_pro.entities

class ServiceType {

    val serviceTypeList = listOf("Reparacion", "Instalacion", "Cambio", "Diagnostico")

    public fun getList(): List<String> {
        return serviceTypeList
    }
}