package com.example.bokynet_front_mobile

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bokynet_front_mobile.network.FavorisRequest
import com.example.bokynet_front_mobile.network.FavorisResponse
import com.example.bokynet_front_mobile.network.RetrofitClient
import com.example.front_bokynet.helper.NavbarHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream

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

        // Lecture PDF
        val btnLirePdf = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btnLirePdf)

        // Récupérer le fichier PDF depuis les extras
        val fichier = intent.getStringExtra("fichier") ?: "Feuillet_pauvre.pdf"
        btnLirePdf.setOnClickListener {
            // Ouvrir PdfActivity
            val intent = Intent(this, PdfActivity::class.java)
            intent.putExtra("fichier", fichier)
            startActivity(intent)
        }

        // Ajout Favoris
        val livreId = intent.getIntExtra("idlivre", -1)
        val btnFavoris = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.btnLirePdfJaune)

        btnFavoris.setOnClickListener {
            if (livreId == -1) {
                Toast.makeText(this, "ID du livre manquant", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val favorisRequest = FavorisRequest(livreId)

            RetrofitClient.getInstance(this).addFavoris(favorisRequest)
                .enqueue(object : retrofit2.Callback<FavorisResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<FavorisResponse>,
                        response: retrofit2.Response<FavorisResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@InfoActivity, "Ajouté aux favoris ✅", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@InfoActivity, "Erreur ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<FavorisResponse>, t: Throwable) {
                        Toast.makeText(this@InfoActivity, "Échec : ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
