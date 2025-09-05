package com.example.front_bokynet

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.front_bokynet.helper.NavbarHelper

class CatalogueActivity : AppCompatActivity() {

    private lateinit var btnAccueil: ImageButton
    private lateinit var btnRecherche: ImageButton
    private lateinit var btnListeLivres: ImageButton
    private lateinit var btnMonEspace: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)

        // Récupérer les boutons depuis le layout
        btnAccueil = findViewById(R.id.btnAccueil)
        btnRecherche = findViewById(R.id.btnRecherche)
        btnListeLivres = findViewById(R.id.btnListeLivres)
        btnMonEspace = findViewById(R.id.btnMonEspace)

        // Initialiser le helper
        val navbarHelper = NavbarHelper(this, btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)
        navbarHelper.selectButton(btnListeLivres)

        //Info livre
        val imgLivre1 = findViewById<ImageView>(R.id.imgLivre1)
        imgLivre1.setOnClickListener {
            // Exemple : ouvrir une nouvelle activité avec le détail du livre
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

    }
}
