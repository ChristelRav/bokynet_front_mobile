package com.example.bokynet_front_mobile.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("register/")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>
    @POST("login/")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>
    @POST("token/refresh/")
    fun refreshToken(@Body refreshRequest: RefreshRequest): Call<LoginResponse>
    @GET("livre/")
    fun getLivres(): Call<List<LivreSimplifie>>
    @GET("livre/")
    fun getLivresSearch(@Query("search") query: String): Call<List<LivreSimplifie>>
    @GET("livre/")
    fun getLivresByCategorie(@Query("categorie_id") categorieId: Int): Call<List<LivreSimplifie>>
    @GET("categorie")
    fun getCategories(): Call<List<CategorieSimplifie>>
    @POST("favoris/")
    fun addFavoris(@Body favorisRequest: FavorisRequest): Call<FavorisResponse>

}
