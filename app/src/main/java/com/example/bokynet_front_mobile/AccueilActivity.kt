package com.example.bokynet_front_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bokynet_front_mobile.network.LivreSimplifie
import com.example.bokynet_front_mobile.network.RetrofitClient
import com.example.front_bokynet.helper.NavbarHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccueilActivity : AppCompatActivity() {

    private lateinit var btnAccueil: ImageButton
    private lateinit var btnRecherche: ImageButton
    private lateinit var btnListeLivres: ImageButton
    private lateinit var btnMonEspace: ImageButton

    private lateinit var gridLivres: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        // Boutons
        btnAccueil = findViewById(R.id.btnAccueil)
        btnRecherche = findViewById(R.id.btnRecherche)
        btnListeLivres = findViewById(R.id.btnListeLivres)
        btnMonEspace = findViewById(R.id.btnMonEspace)

        NavbarHelper(this, btnAccueil, btnRecherche, btnListeLivres, btnMonEspace)

        // GridLayout
        gridLivres = findViewById(R.id.gridLivres)

        // Charger les livres
        fetchLivres()
    }

    private fun fetchLivres() {
        RetrofitClient.getInstance(this).getLivres()
            .enqueue(object : Callback<List<LivreSimplifie>> {
                override fun onResponse(
                    call: Call<List<LivreSimplifie>>,
                    response: Response<List<LivreSimplifie>>
                ) {
                    if (response.isSuccessful) {
                        val livres = response.body()
                        livres?.let { afficherLivres(it) }
                    } else {
                        Toast.makeText(this@AccueilActivity, "Erreur API: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<LivreSimplifie>>, t: Throwable) {
                    Toast.makeText(this@AccueilActivity, "Échec: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun afficherLivres(livres: List<LivreSimplifie>) {
        gridLivres.removeAllViews() // vider avant d’ajouter

        for (livre in livres) {
            val imageView = ImageView(this)

            // Taille
            val layoutParams = GridLayout.LayoutParams().apply {
                width = 300 // ou dp converti en px
                height = 420
                setMargins(16, 20, 20, 30)
            }
            imageView.layoutParams = layoutParams

            // Image à partir de drawable selon la colonne "couverture"
            // On enlève l’extension .jpg pour getIdentifier
            val nomCouverture = livre.couverture.substringBeforeLast(".")
            val resId = resources.getIdentifier(nomCouverture, "drawable", packageName)
            if (resId != 0) {
                imageView.setImageResource(resId)
            } else {
                imageView.setImageResource(R.drawable.cv_3) // image par défaut
            }

            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.contentDescription = livre.titre

            // Optionnel: clic
            imageView.setOnClickListener {
                val intent = Intent(this, InfoActivity::class.java).apply {
                    putExtra("idlivre", livre.idlivre)
                    putExtra("titre", livre.titre)
                    putExtra("auteur", livre.auteur.auteur)
                    putExtra("categorie", livre.categorie.categorie)
                    putExtra("couverture", livre.couverture)
                    putExtra("resume", livre.resume)
                }
                startActivity(intent)
            }

            // Ajouter à GridLayout
            gridLivres.addView(imageView)
        }
    }
}
