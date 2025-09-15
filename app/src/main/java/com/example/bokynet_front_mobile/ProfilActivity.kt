package com.example.bokynet_front_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.front_bokynet.helper.NavbarHelper

class ProfilActivity : AppCompatActivity() {

    private lateinit var btnAccueil: ImageButton
    private lateinit var btnRecherche: ImageButton
    private lateinit var btnListeLivres: ImageButton
    private lateinit var btnMonEspace: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        // Récupérer les boutons depuis le layout
        btnAccueil = findViewById(R.id.btnAccueil)
        btnRecherche = findViewById(R.id.btnRecherche)
        btnListeLivres = findViewById(R.id.btnListeLivres)
        btnMonEspace = findViewById(R.id.btnMonEspace)

        // Initialiser le helper
        val navbarHelper = NavbarHelper(this, btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)
        navbarHelper.selectButton(btnMonEspace)

    }
}
