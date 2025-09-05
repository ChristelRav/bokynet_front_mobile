package com.example.front_bokynet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvInscription = findViewById<TextView>(R.id.tvInscription)
        val btnconnexion =  findViewById<Button>(R.id.btnconnexion)

        //vers accueil
        btnconnexion.setOnClickListener {
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
        }

        // lien vers inscription
        val text = "Première visite ? <font color='#0000FF'><a href='signup'>S'inscrire au registre des lecteurs</a></font>"
        tvInscription.text = Html.fromHtml(text)
        tvInscription.movementMethod = LinkMovementMethod.getInstance()
        tvInscription.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}
