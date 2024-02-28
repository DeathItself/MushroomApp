package com.example.project03.util.db

import android.net.Uri
import android.util.Log
import com.example.project03.model.Mushroom
import com.example.project03.model.MyMushroom
import com.example.project03.model.Ranking
import com.example.project03.model.Restaurants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

suspend fun FirebaseFirestore.getMushrooms(): List<Mushroom> {
    return try {
        val documents = collection("setas").get().await()
        documents.toObjects(Mushroom::class.java)
    } catch (e: Exception) {
        println("Error getting mushrooms from Firebase: ${e.message}")
        emptyList()
    }
}

suspend fun FirebaseFirestore.getMyMushrooms(userId: String): List<MyMushroom> {
    return try {
        val excludedDocumentName = "Otro"
        val documents = collection("my_mushrooms")
            .whereEqualTo("userId", userId)
            .get().await().documents.mapNotNull { document ->
                document.toObject(MyMushroom::class.java)
            }
        documents
    } catch (e: Exception) {
        println("Error getting mushrooms from Firebase: ${e.message}")
        emptyList()
    }
}

suspend fun FirebaseFirestore.addMushroom(mushroom: MyMushroom, userId: String) {
    try {
        // Get the current counter value
        val counterDocument = collection("counters").document("my_mushroom_counter").get().await()
        var counter = counterDocument.getLong("counter")!!

        counter += 1

        // Use the counter as part of the document ID
        val documentId = "my_mush$counter"
        // Update the counter in Firestore
        collection("counters").document("my_mushroom_counter").set(mapOf("counter" to counter))
            .await()
        mushroom.userId = userId
        mushroom.myMushID = documentId
        // Add the new mushroom with the auto-incremented document ID
        collection("my_mushrooms").document(documentId).set(mushroom).await()


    } catch (e: Exception) {
        println("Error adding mushroom to Firebase: ${e.message}")
    }
}

suspend fun FirebaseFirestore.getRanking(userId: String): List<Ranking> {
    return try {
        val excludedDocumentName = "prueba"
        val documents = collection("puntuacion")
            .whereEqualTo("userId", userId)
            .get().await().documents.mapNotNull { document ->
                document.toObject(Ranking::class.java)
            }
        documents
    } catch (e: Exception) {
        println("Error getting mushrooms from Firebase: ${e.message}")
        emptyList()
    }
}

suspend fun FirebaseFirestore.addRanking(ranking: Ranking){
    collection("puntuacion").add(ranking)
        .addOnSuccessListener { documentReference ->
            Log.d("Ranking", "New ranking added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { exception ->
            Log.e("Ranking", "Error adding ranking: ${exception.message}")
        }
}

suspend fun FirebaseFirestore.updateScore(ranking: Ranking) {
    collection("puntuacion").document(ranking.userId).set(ranking)
}

suspend fun FirebaseFirestore.deleteMushroom(commonName: String, Description: String) {
    val documentId = FirebaseFirestore.getInstance().collection("my_mushrooms")
        .whereEqualTo("commonName", commonName).whereEqualTo("description", Description).get()
        .await().documents[0].id
    try {
        // Delete the mushroom document
        collection("my_mushrooms").document(documentId).delete().await()

    } catch (e: Exception) {
        println("Error deleting mushroom from Firebase: ${e.message}")
    }
}

suspend fun uploadImageAndGetUrl(imagePath: String): String {
    val storageRef = FirebaseStorage.getInstance().reference
    val file = Uri.fromFile(File(imagePath))
    val imageRef = storageRef.child("images/${file.lastPathSegment}")
    var downloadUrl = ""

    try {
        imageRef.putFile(file).await()
        downloadUrl = imageRef.downloadUrl.await().toString()
    } catch (e: Exception) {
        println("Error uploading image to Firebase Storage: ${e.message}")
    }

    return downloadUrl
}
suspend fun FirebaseFirestore.getRestaurants(): List<Restaurants> {
    return try {
        val documents = collection("restaurantes").get()
            .await()
        documents.toObjects(Restaurants::class.java)
    }catch (e: Exception) {
        println("Error getting restaurants from Firebase: ${e.message}")
        emptyList()
    }
}