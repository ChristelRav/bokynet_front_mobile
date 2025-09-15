package com.example.bokynet_front_mobile.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Base URL de ton API Django
    private const val BASE_URL = "http://192.168.88.11:8000/"

    // Retourne l'instance ApiService avec AuthInterceptor pour gérer access & refresh token
    fun getInstance(context: Context): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // Gestion automatique du token
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
