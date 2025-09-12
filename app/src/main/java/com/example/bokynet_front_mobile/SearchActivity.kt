package com.example.bokynet_front_mobile

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bokynet_front_mobile.network.CategorieSimplifie
import com.example.bokynet_front_mobile.network.RetrofitClient
import com.example.front_bokynet.helper.NavbarHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var btnAccueil: ImageButton
    private lateinit var btnRecherche: ImageButton
    private lateinit var btnListeLivres: ImageButton
    private lateinit var btnMonEspace: ImageButton
    private lateinit var etSearch: EditText
    private lateinit var gridLayout3: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Boutons
        btnAccueil = findViewById(R.id.btnAccueil)
        btnRecherche = findViewById(R.id.btnRecherche)
        btnListeLivres = findViewById(R.id.btnListeLivres)
        btnMonEspace = findViewById(R.id.btnMonEspace)

        // NavbarHelper
        val navbarHelper = NavbarHelper(this, btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)
        navbarHelper.selectButton(btnRecherche) // Sélection du bouton Recherche

        // Barre de recherche
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

        // GridLayout pour les catégories
        gridLayout3 = findViewById(R.id.gridLayout3)

        // Charger les catégories depuis l’API
        fetchCategories()
    }

    private fun fetchCategories() {
        RetrofitClient.getInstance(this).getCategories()
            .enqueue(object : Callback<List<CategorieSimplifie>> {
                override fun onResponse(
                    call: Call<List<CategorieSimplifie>>,
                    response: Response<List<CategorieSimplifie>>
                ) {
                    if (response.isSuccessful) {
                        val categories = response.body()
                        categories?.let { afficherCategories(it) }
                    } else {
                        Toast.makeText(this@SearchActivity, "Erreur API: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<CategorieSimplifie>>, t: Throwable) {
                    Toast.makeText(this@SearchActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun afficherCategories(categories: List<CategorieSimplifie>) {
        gridLayout3.removeAllViews()

        for (categorie in categories) {
            // Carte LinearLayout
            val card = LinearLayout(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 250 // adapte la hauteur si nécessaire
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(8, 8, 10, 30)
                }
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(8, 8, 8, 8)
                setBackgroundColor(android.graphics.Color.WHITE)
                elevation = 4f
                isClickable = true

                // Ripple effect pour le clic
                val typedValue = android.util.TypedValue()
                theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
                foreground = ContextCompat.getDrawable(context, typedValue.resourceId)
            }

            // Texte de la catégorie
            val textView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = categorie.categorie
                setTextColor(android.graphics.Color.parseColor("#528AC5"))
                textSize = 16f
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            card.addView(textView)

            // Clic sur la carte → ouvrir ResultActivity
            card.setOnClickListener {
                val intent = Intent(this, CategorieActivity::class.java)
                intent.putExtra("idcategorie", categorie.idcategorie)
                intent.putExtra("nomcategorie", categorie.categorie)
                startActivity(intent)
            }

            gridLayout3.addView(card)
        }
    }
}
