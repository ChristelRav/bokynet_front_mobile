package com.example.bokynet_front_mobile

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bokynet_front_mobile.network.LoginRequest
import com.example.bokynet_front_mobile.network.LoginResponse
import com.example.bokynet_front_mobile.network.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // lien vers inscription
        val tvInscription = findViewById<TextView>(R.id.tvInscription)
        val text = "Première visite ? <font color='#0000FF'><a href='signup'>S'inscrire au registre des lecteurs</a></font>"
        tvInscription.text = Html.fromHtml(text)
        tvInscription.movementMethod = LinkMovementMethod.getInstance()
        tvInscription.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        // Traitement connexion
        val btnConnexion = findViewById<Button>(R.id.btnconnexion)
        btnConnexion.setOnClickListener {
            val username = findViewById<TextInputEditText>(R.id.iptmail).text.toString()
            val password = findViewById<TextInputEditText>(R.id.iptmdp).text.toString()

            val request = LoginRequest(username, password)

            RetrofitClient.getInstance(this).loginUser(request)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            val accessToken = response.body()?.access
                            val refreshToken = response.body()?.refresh

                            val sharedPref = getSharedPreferences("auth_prefs", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("access_token", accessToken)
                                putString("refresh_token", refreshToken)
                                apply()
                            }
                            Toast.makeText(this@LoginActivity, "Access: ${accessToken ?: "null"}", Toast.LENGTH_LONG).show()
                            Toast.makeText(this@LoginActivity, "Refresh: ${refreshToken ?: "null"}", Toast.LENGTH_LONG).show()

                            Toast.makeText(this@LoginActivity, "Connexion réussie !", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this@LoginActivity, AccueilActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Erreur: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Échec de connexion : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
