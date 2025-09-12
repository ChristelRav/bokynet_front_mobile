package com.example.bokynet_front_mobile.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url().toString()

        // ⚠️ On ne met pas le token pour /login et /register
        if (url.contains("/login") || url.contains("/register")) {
            return chain.proceed(originalRequest)
        }

        // Sinon, on met le token si disponible
        val accessToken = sharedPref.getString("access_token", null)
        val request = if (accessToken != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}
