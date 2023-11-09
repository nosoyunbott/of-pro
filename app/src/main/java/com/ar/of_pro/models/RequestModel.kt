package com.ar.of_pro.models

data class RequestModel(
    val categoryOcupation: String,
    val categoryService: String,
    val clientId: String,
    val date: String,
    val description: String,
    val imageUrlArray: MutableList<String>,
    val maxCost: Int,
    val providerId: String,
    val requestBidAmount: Int,
    val requestTitle: String,
    val state: String,
    val id: String
) {
    constructor() : this(
        "", "", "", "", "", mutableListOf(), 0, "", 0, "", "", ""
    )
}
