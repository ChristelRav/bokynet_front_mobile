package com.example.bokynet_front_mobile

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class PdfActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var currentPage = 0
    private var totalPages = 0
    private lateinit var pdfRenderer: android.graphics.pdf.PdfRenderer
    private lateinit var fileDescriptor: ParcelFileDescriptor

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        imageView = findViewById(R.id.imageViewPdf)

        // Récupérer le nom du fichier depuis l'intent
        var fichier = intent.getStringExtra("fichier") ?: "defaut.pdf"

        // Vérifier si le fichier existe dans assets
        val assetsList = assets.list("") ?: arrayOf()
        if (!assetsList.contains(fichier)) {
            fichier = "defaut.pdf"
        }

        // Copier le PDF depuis assets vers fichiers internes
        val file = File(filesDir, fichier)
        if (!file.exists()) {
            val inputStream: InputStream = assets.open(fichier)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
        }

        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = android.graphics.pdf.PdfRenderer(fileDescriptor)
        totalPages = pdfRenderer.pageCount

        renderPage(currentPage)

        // Boutons suivant / précédent
        findViewById<Button>(R.id.btnPrev).setOnClickListener {
            if (currentPage > 0) {
                currentPage--
                renderPage(currentPage)
            }
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            if (currentPage < totalPages - 1) {
                currentPage++
                renderPage(currentPage)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun renderPage(pageIndex: Int) {
        val page = pdfRenderer.openPage(pageIndex)
        // Créer un bitmap avec fond blanc
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.WHITE) // <-- fond blanc forcé
        page.render(bitmap, null, null, android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        imageView.setImageBitmap(bitmap)
        page.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        pdfRenderer.close()
        fileDescriptor.close()
    }
}
