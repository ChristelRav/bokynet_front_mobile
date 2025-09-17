package com.example.bokynet_front_mobile.network

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val role_id: Int
)
