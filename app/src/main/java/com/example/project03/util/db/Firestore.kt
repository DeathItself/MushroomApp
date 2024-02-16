package com.example.project03.util.db

import com.example.project03.model.Mushroom
import com.example.project03.model.MyMushroom
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun FirebaseFirestore.getMushrooms(): List<Mushroom> {
    return try {
        val documents = collection("setas").get()
            .await()
        documents.toObjects(Mushroom::class.java)
    } catch (e: Exception) {
        println("Error getting mushrooms from Firebase: ${e.message}")
        emptyList()
    }
}


suspend fun FirebaseFirestore.AddMushroom(mushroom: MyMushroom) {
    try {
        collection("my_mushrooms").document(mushroom.commonName).set(mushroom).await()
    } catch (e: Exception) {
        println("Error adding mushroom to Firebase: ${e.message}")
    }
}
suspend fun FirebaseFirestore.getMyMushrooms(): List<MyMushroom> {
    return try {
        val documents = collection("my_mushrooms").get()
            .await()
        documents.toObjects(MyMushroom::class.java)
    } catch (e: Exception) {
        println("Error getting mushrooms from Firebase: ${e.message}")
        emptyList()
    }
}