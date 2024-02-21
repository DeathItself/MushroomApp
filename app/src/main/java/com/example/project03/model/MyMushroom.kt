package com.example.project03.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName


data class MyMushroom(
    var commonName: String = "",
    var description: String = "",
    var dificulty: String = "",
    var habitat: String = "",
    @get:PropertyName("isEdible") @set:PropertyName("isEdible") var isEdible: Boolean? = null,
    var photo: String = "",
    var scientificName: String = "",
    var seasons: String = "",
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var myMushID: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    var userId: String = ""
)
