package com.example.project03.model

import com.google.firebase.Timestamp

data class Ranking (
    var puntuacion: Double = 0.0,
    var userId: String = "",
    var timestamp: Timestamp = Timestamp.now()

)