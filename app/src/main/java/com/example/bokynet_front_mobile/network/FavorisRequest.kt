package com.example.bokynet_front_mobile.network

data class FavorisRequest(
    val livre_id: Int
)

data class FavorisResponse(
    val success: Boolean,
    val message: String
)