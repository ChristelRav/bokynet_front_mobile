package com.example.front_bokynet.helper

import android.content.Intent
import android.content.Context
import android.widget.ImageButton
import com.example.front_bokynet.AccueilActivity
import com.example.front_bokynet.CatalogueActivity
import com.example.front_bokynet.ProfilActivity
import com.example.front_bokynet.SearchActivity

class NavbarHelper(
    private val context: Context,
    private val btnAccueil: ImageButton,
    private val btnRecherche: ImageButton,
    private val btnListeLivres: ImageButton,
    private val btnMonEspace: ImageButton
) {
    init {
        selectButton(btnAccueil)

        btnAccueil.setOnClickListener {
            selectButton(btnAccueil)
            context.startActivity(Intent(context, AccueilActivity::class.java))
        }
        btnRecherche.setOnClickListener {
            selectButton(btnRecherche)
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
        btnListeLivres.setOnClickListener {
            selectButton(btnListeLivres)
            context.startActivity(Intent(context, CatalogueActivity::class.java))
        }
        btnMonEspace.setOnClickListener {
            selectButton(btnMonEspace)
            context.startActivity(Intent(context, ProfilActivity::class.java))
        }
    }

     fun selectButton(selectedButton: ImageButton) {
        val buttons = listOf(btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)
        buttons.forEach { it.isSelected = it == selectedButton }
    }
}
