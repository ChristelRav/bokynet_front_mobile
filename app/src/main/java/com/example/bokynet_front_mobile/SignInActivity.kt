package com.example.bokynet_front_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bokynet_front_mobile.network.RegisterRequest
import com.example.bokynet_front_mobile.network.RegisterResponse
import com.example.bokynet_front_mobile.network.RetrofitClient
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val tvConnexion = findViewById<TextView>(R.id.tvConnexion)

        // Texte avec un lien
        val text = "Ici : <font color='#0000FF'><a href='signup'>Accéder à mon espace</a></font>"
        tvConnexion.text = android.text.Html.fromHtml(text)
        tvConnexion.movementMethod = android.text.method.LinkMovementMethod.getInstance()

        // Gérer le clic sur le lien
        tvConnexion.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Bouton d'inscription
        val btnInscription = findViewById<Button>(R.id.btninscritpion)

        btnInscription.setOnClickListener {
            // Récupérer les champs
            val username = findViewById<TextInputEditText>(R.id.iptNomPrenom).text.toString()
            val email = findViewById<TextInputEditText>(R.id.iptmail).text.toString()
            val password = findViewById<TextInputEditText>(R.id.iptmdp).text.toString()

            // Construire la requête
            val request = RegisterRequest(username = username, email = email, password = password, role_id = 2 )

            // Appel API via Retrofit
            RetrofitClient.getInstance(this).registerUser(request)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@SignInActivity, "Inscription réussie !", Toast.LENGTH_SHORT).show()

                            // Rediriger vers la page login
                            startActivity(Intent(this@SignInActivity, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@SignInActivity, "Erreur: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@SignInActivity, "Échec de connexion : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
