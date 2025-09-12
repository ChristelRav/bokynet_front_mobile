package com.example.bokynet_front_mobile.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("register/")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
    @POST("login/")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @POST("token/refresh/")
    fun refreshToken(@Body request: RefreshRequest): Call<RefreshResponse>
    @GET("livre/")
    fun getLivres(): Call<List<LivreSimplifie>>
    @GET("categorie")
    fun getCategories(): Call<List<CategorieSimplifie>>


}
