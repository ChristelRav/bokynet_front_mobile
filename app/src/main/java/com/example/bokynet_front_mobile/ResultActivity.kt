package com.example.bokynet_front_mobile

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bokynet_front_mobile.network.LivreSimplifie
import com.example.bokynet_front_mobile.network.RetrofitClient
import com.example.front_bokynet.helper.NavbarHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultActivity : AppCompatActivity() {

    private lateinit var btnAccueil: ImageButton
    private lateinit var btnRecherche: ImageButton
    private lateinit var btnListeLivres: ImageButton
    private lateinit var btnMonEspace: ImageButton
    private lateinit var linearResultContainer: LinearLayout
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Navbar
        btnAccueil = findViewById(R.id.btnAccueil)
        btnRecherche = findViewById(R.id.btnRecherche)
        btnListeLivres = findViewById(R.id.btnListeLivres)
        btnMonEspace = findViewById(R.id.btnMonEspace)
        val navbarHelper = NavbarHelper(this, btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)
        navbarHelper.selectButton(btnRecherche)

        // TextView résultats
        tvResult = findViewById(R.id.tvResult)
        linearResultContainer = findViewById(R.id.linearContainerResult)

        // Récupérer la query
        val query = intent.getStringExtra("query") ?: ""
        tvResult.text = "Résultats pour : $query"

        // Appeler l'API
        fetchLivres(query)
    }

    private fun fetchLivres(query: String) {
        RetrofitClient.getInstance(this).getLivresSearch(query)
            .enqueue(object : Callback<List<LivreSimplifie>> {
                override fun onResponse(
                    call: Call<List<LivreSimplifie>>,
                    response: Response<List<LivreSimplifie>>
                ) {
                    if (response.isSuccessful) {
                        val livres = response.body()
                        livres?.let { afficherResultats(it) }
                    } else {
                        Toast.makeText(this@ResultActivity, "Erreur API: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<LivreSimplifie>>, t: Throwable) {
                    Toast.makeText(this@ResultActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun afficherResultats(livres: List<LivreSimplifie>) {
        linearResultContainer.removeAllViews()

        for (livre in livres) {
            val itemLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(16, 16, 16, 16) }
                orientation = LinearLayout.HORIZONTAL
                setPadding(12, 12, 12, 12)
                setBackgroundColor(android.graphics.Color.WHITE)
                elevation = 4f
                isClickable = true
            }

            // Image couverture plus grande
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
                imgCover.setImageResource(R.drawable.ic_launcher_foreground)
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

            itemLayout.addView(imgCover)
            itemLayout.addView(infoLayout)

            // Clic → InfoActivity avec id du livre
            itemLayout.setOnClickListener {
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

            linearResultContainer.addView(itemLayout)
        }
    }
}
