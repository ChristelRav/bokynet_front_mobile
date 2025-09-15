package com.example.bokynet_front_mobile.network

data class LivreSimplifie(
    val idlivre: Int,
    val titre: String,
    val auteur: AuteurSimplifie,
    val categorie: CategorieSimplifie,
    val couverture: String,
    val resume: String,
    val fichier: String
)

data class AuteurSimplifie(
    val idauteur: Int,
    val auteur: String
)

data class CategorieSimplifie(
    val idcategorie: Int,
    val categorie: String
)
