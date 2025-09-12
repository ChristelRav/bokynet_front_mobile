package com.example.bokynet_front_mobile

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.front_bokynet.helper.NavbarHelper
import android.widget.ImageButton

class CategorieActivity : AppCompatActivity() {

    private lateinit var tvCategorie: TextView
    private lateinit var btnAccueil: ImageButton
    private lateinit var btnRecherche: ImageButton
    private lateinit var btnListeLivres: ImageButton
    private lateinit var btnMonEspace: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorie)

        // Navbar
        btnAccueil = findViewById(R.id.btnAccueil)
        btnRecherche = findViewById(R.id.btnRecherche)
        btnListeLivres = findViewById(R.id.btnListeLivres)
        btnMonEspace = findViewById(R.id.btnMonEspace)
        val navbarHelper = NavbarHelper(this, btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)
        navbarHelper.selectButton(btnRecherche)

        // Récupérer le TextView
        tvCategorie = findViewById(R.id.tvCategorie)

        // Récupérer les extras
        val nomCategorie = intent.getStringExtra("nomcategorie") ?: "Inconnue"

        // Afficher le nom de la catégorie
        tvCategorie.text = "Catégorie : $nomCategorie"

        // Si tu veux utiliser l'id pour récupérer des livres ou autre
        val idCategorie = intent.getIntExtra("idcategorie", -1)
        // TODO: charger les livres de cette catégorie via API
    }
}
