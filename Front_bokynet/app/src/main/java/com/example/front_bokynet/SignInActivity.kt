package com.example.front_bokynet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sigin)

        val tvConnexion = findViewById<TextView>(R.id.tvConnexion)

        // Texte avec un lien
        val text = "Ici : <font color='#0000FF'><a href='signup'>Accéder à mon espace</a></font>"
        tvConnexion.text = Html.fromHtml(text)
        tvConnexion.movementMethod = LinkMovementMethod.getInstance()

        // Gérer le clic sur le lien
        tvConnexion.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
