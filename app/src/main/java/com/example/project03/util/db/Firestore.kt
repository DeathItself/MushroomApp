package com.example.project03.util.db

import android.net.Uri
import com.example.project03.model.Mushroom
import com.example.project03.model.MyMushroom
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

suspend fun FirebaseFirestore.getMyMushrooms(): List<MyMushroom> {
    return try {
        val excludedDocumentName = "Otro"
        val documents = collection("my_mushrooms").get().await().documents.mapNotNull { document ->
                if (document.id != excludedDocumentName) {
                    document.toObject(MyMushroom::class.java)
                } else {
                    null
                }
            }
        documents
    } catch (e: Exception) {
        println("Error getting mushrooms from Firebase: ${e.message}")
        emptyList()
    }
}

suspend fun FirebaseFirestore.addMushroom(mushroom: MyMushroom) {
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

        mushroom.myMushID = documentId
        // Add the new mushroom with the auto-incremented document ID
        collection("my_mushrooms").document(documentId).set(mushroom).await()


    } catch (e: Exception) {
        println("Error adding mushroom to Firebase: ${e.message}")
    }
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