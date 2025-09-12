package com.example.bokynet_front_mobile

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.front_bokynet.helper.NavbarHelper

class InfoActivity : AppCompatActivity() {

    private lateinit var btnAccueil: ImageButton
    private lateinit var btnRecherche: ImageButton
    private lateinit var btnListeLivres: ImageButton
    private lateinit var btnMonEspace: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // Navbar
        btnAccueil = findViewById(R.id.btnAccueil)
        btnRecherche = findViewById(R.id.btnRecherche)
        btnListeLivres = findViewById(R.id.btnListeLivres)
        btnMonEspace = findViewById(R.id.btnMonEspace)
        NavbarHelper(this, btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)

        // Récupérer les extras
        val titre = intent.getStringExtra("titre")
        val auteur = intent.getStringExtra("auteur")
        val couverture = intent.getStringExtra("couverture")
        val resume = intent.getStringExtra("resume")

        // Lier aux vues
        val tvTitre = findViewById<TextView>(R.id.tvTitreLivre)
        val tvAuteur = findViewById<TextView>(R.id.tvAuteurLivre)
        val tvSynopsis = findViewById<TextView>(R.id.tvSynopsis)
        val imgCouverture = findViewById<ImageView>(R.id.imgCouverture)

        tvTitre.text = titre
        tvAuteur.text = auteur
        tvSynopsis.text = resume

        // Charger image locale (drawable)
        couverture?.let {
            val resId = resources.getIdentifier(it.substringBeforeLast("."), "drawable", packageName)
            if (resId != 0) {
                imgCouverture.setImageResource(resId)
            } else {
                imgCouverture.setImageResource(R.drawable.cv_3)
            }
        }
    }
}
