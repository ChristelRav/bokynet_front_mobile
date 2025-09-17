package com.example.bokynet_front_mobile.network

data class LoginResponse(
    val access: String,   // token JWT
    val refresh: String   // token refresh JWT
)
