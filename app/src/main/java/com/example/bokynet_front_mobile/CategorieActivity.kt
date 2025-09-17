package com.example.bokynet_front_mobile

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.bokynet_front_mobile.network.LivreSimplifie
import com.example.bokynet_front_mobile.network.RetrofitClient
import com.example.front_bokynet.helper.NavbarHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategorieActivity : AppCompatActivity() {

    private lateinit var tvCategorie: TextView
    private lateinit var linearContainer: LinearLayout
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

        // TextView catégorie
        tvCategorie = findViewById(R.id.tvCategorie)

        // Container pour les livres dynamiques
        linearContainer = findViewById(R.id.linearContainer)

        // Récupérer l'id et le nom de la catégorie
        val idCategorie = intent.getIntExtra("idcategorie", -1)
        val nomCategorie = intent.getStringExtra("nomcategorie") ?: "Inconnue"
        tvCategorie.text = "Catégorie : $nomCategorie"

        if (idCategorie != -1) {
            fetchLivresByCategorie(idCategorie)
        } else {
            Toast.makeText(this, "Erreur: id de catégorie invalide", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchLivresByCategorie(idCategorie: Int) {
        RetrofitClient.getInstance(this).getLivresByCategorie(idCategorie)
            .enqueue(object : Callback<List<LivreSimplifie>> {
                override fun onResponse(
                    call: Call<List<LivreSimplifie>>,
                    response: Response<List<LivreSimplifie>>
                ) {
                    if (response.isSuccessful) {
                        val livres = response.body()
                        livres?.let { afficherLivres(it) }
                    } else {
                        Toast.makeText(this@CategorieActivity, "Erreur API: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<LivreSimplifie>>, t: Throwable) {
                    Toast.makeText(this@CategorieActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun afficherLivres(livres: List<LivreSimplifie>) {
        linearContainer.removeAllViews()

        for (livre in livres) {
            val bookLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(16, 16, 16, 16) }
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
                setBackgroundColor(android.graphics.Color.WHITE)
                elevation = 4f
                isClickable = true
            }

            // Image couverture locale
            val imgCover = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(200, 280)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            val resourceId = resources.getIdentifier(
                livre.couverture.substringBeforeLast("."), // supprime l'extension .jpg
                "drawable",
                packageName
            )
            if (resourceId != 0) {
                imgCover.setImageResource(resourceId)
            } else {
                imgCover.setImageResource(R.drawable.ic_launcher_foreground) // image par défaut
            }

            // Infos livre
            val infoLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                    .apply { setMargins(12, 0, 0, 0) }
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_VERTICAL
            }

            val tvTitre = TextView(this).apply {
                text = livre.titre
                textSize = 18f
                setTextColor(android.graphics.Color.parseColor("#1c2a50"))
                setTypeface(null, android.graphics.Typeface.BOLD)
            }

            val tvAuteur = TextView(this).apply {
                text = livre.auteur.auteur
                textSize = 14f
                setTextColor(android.graphics.Color.parseColor("#528AC5"))
            }

            infoLayout.addView(tvTitre)
            infoLayout.addView(tvAuteur)

            bookLayout.addView(imgCover)
            bookLayout.addView(infoLayout)

            // Clic → MainActivity avec id du livre
            bookLayout.setOnClickListener {
                val intent = Intent(this, InfoActivity::class.java).apply {
                    putExtra("idlivre", livre.idlivre)
                    putExtra("titre", livre.titre)
                    putExtra("auteur", livre.auteur.auteur)
                    putExtra("categorie", livre.categorie.categorie)
                    putExtra("couverture", livre.couverture)
                    putExtra("resume", livre.resume)
                    putExtra("fichier", livre.fichier)
                }
                startActivity(intent)
            }

            linearContainer.addView(bookLayout)
        }
    }
}
