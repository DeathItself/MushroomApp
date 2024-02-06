package com.example.project03.util.db

import com.example.project03.model.Mushroom
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun FirebaseFirestore.getMushrooms(): List<Mushroom> {
    return try {
        val documents = collection("setas").get().await()
        documents.toObjects(Mushroom::class.java)
    } catch (e: Exception) {
        println("Error getting mushrooms from Firebase: ${e.message}")
        emptyList()
    }
}