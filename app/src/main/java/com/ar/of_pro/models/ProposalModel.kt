package com.ar.of_pro.models

data class ProposalModel(
    val bid: Int,
    val commentary: String,
    val disabled: Boolean,
    val providerId: String,
    val requestId: String,
    val stability: Int
) {
    constructor() : this(0, "", false, "", "", 0)
}
