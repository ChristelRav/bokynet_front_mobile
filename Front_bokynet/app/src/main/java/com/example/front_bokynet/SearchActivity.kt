package com.example.front_bokynet

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.front_bokynet.helper.NavbarHelper

class SearchActivity : AppCompatActivity() {

    private lateinit var btnAccueil: ImageButton
    private lateinit var btnRecherche: ImageButton
    private lateinit var btnListeLivres: ImageButton
    private lateinit var btnMonEspace: ImageButton
    private lateinit var etSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Récupérer les boutons depuis le layout
        btnAccueil = findViewById(R.id.btnAccueil)
        btnRecherche = findViewById(R.id.btnRecherche)
        btnListeLivres = findViewById(R.id.btnListeLivres)
        btnMonEspace = findViewById(R.id.btnMonEspace)

        // Initialiser le helper
        val navbarHelper = NavbarHelper(this, btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)
        navbarHelper.selectButton(btnRecherche) // Sélectionne le bouton Recherche

        etSearch = findViewById(R.id.etSearch)

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = etSearch.text.toString()
                if (query.isNotEmpty()) {
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("query", query)
                    startActivity(intent)
                }
                true
            } else {
                false
            }
        }

        //Recherche catégorie
        val cardCategorie1 = findViewById<LinearLayout>(R.id.cardCategorie1)
        cardCategorie1.setOnClickListener {
            val intent = Intent(this, CategorieActivity::class.java)
            intent.putExtra("categorie", "catégorie 1") // tu peux passer des infos
            startActivity(intent)
        }
    }
}
