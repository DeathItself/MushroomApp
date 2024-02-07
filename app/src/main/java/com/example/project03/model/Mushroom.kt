package com.example.project03.model


class Mushroom(
    val commonName: String,
    val description: String,
    val habitat: String,
    val isEdible: Boolean,
    val photo: String,
    val scientificName: String,
    val seasons: String
){
    constructor() : this("","","", false,"","","")
}

