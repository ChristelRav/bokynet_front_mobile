package com.example.bokynet_front_mobile.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        var accessToken = sharedPref.getString("access_token", null)
        val refreshToken = sharedPref.getString("refresh_token", null)

        // Ajouter Authorization header si accessToken existe
        var request = chain.request()
        if (accessToken != null) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        }

        var response = chain.proceed(request)

        // Si 401 Unauthorized et refresh token disponible
        if (response.code() == 401 && refreshToken != null) {
            response.close() // fermer la première réponse

            // Créer un Retrofit temporaire pour refresh token
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.88.11:8000/") // base URL de ton API
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)

            try {
                val refreshResponse = apiService.refreshToken(RefreshRequest(refreshToken)).execute()
                if (refreshResponse.isSuccessful) {
                    val newAccess = refreshResponse.body()?.access
                    val newRefresh = refreshResponse.body()?.refresh ?: refreshToken

                    if (newAccess != null) {
                        // Sauvegarder les nouveaux tokens
                        with(sharedPref.edit()) {
                            putString("access_token", newAccess)
                            putString("refresh_token", newRefresh)
                            apply()
                        }

                        // Refaire la requête originale avec le nouveau access
                        val newRequest = request.newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer $newAccess")
                            .build()

                        response = chain.proceed(newRequest)
                    }
                }
            } catch (e: Exception) {
                // Optionnel : logger l'erreur
                e.printStackTrace()
            }
        }

        return response
    }
}
