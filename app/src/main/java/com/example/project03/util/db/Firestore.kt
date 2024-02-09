package com.example.project03.util.db

import android.util.Log
import com.example.project03.model.Mushroom
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun FirebaseFirestore.getMushrooms(): List<Mushroom> {
    return try {
        val documents = collection("setas").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val mushroom = document.toObject(Mushroom::class.java)
                    Log.d("MushroomInfo", "Mushroom: ${document.id} => ${mushroom.toString()}")
                }
            }
            .await()
        documents.toObjects(Mushroom::class.java)
    } catch (e: Exception) {
        println("Error getting mushrooms from Firebase: ${e.message}")
        emptyList()
    }
}