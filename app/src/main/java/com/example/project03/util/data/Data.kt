package com.example.project03.util.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.project03.model.Mushroom
import com.example.project03.model.MyMushroom
import com.example.project03.ui.components.Loading.Companion.LoadingState
import com.example.project03.util.db.addMushroom
import com.example.project03.util.db.getMushrooms
import com.example.project03.util.db.getMyMushrooms
import com.example.project03.util.db.uploadImageAndGetUrl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Data {
    companion object {
        @JvmStatic
        @Composable
        fun wikiDBList(): List<Mushroom> {
            val db = FirebaseFirestore.getInstance()
            var mushroomList by remember { mutableStateOf(listOf<Mushroom>()) }
            var isLoading by remember { mutableStateOf(true) } // Asume carga inicialmente
            // Efecto lanzado para cargar los datos
            LaunchedEffect(key1 = Unit) {
                mushroomList = db.getMushrooms()
                isLoading = false // Marca la carga como finalizada después de obtener los datos
            }
            if (isLoading) {
                LoadingState()
            }
            return mushroomList
        }

        @JvmStatic
        @Composable
        fun myMushDBList(): List<MyMushroom> {
            val db = FirebaseFirestore.getInstance()
            var mushroomList by remember { mutableStateOf(listOf<MyMushroom>()) }
            var isLoading by remember { mutableStateOf(true) } // Asume carga inicialmente
            val userId = Firebase.auth.currentUser?.uid
            // Efecto lanzado para cargar los datos
            LaunchedEffect(key1 = Unit) {
                mushroomList = db.getMyMushrooms(userId!!)
                isLoading = false // Marca la carga como finalizada después de obtener los datos
            }
            if (isLoading) {
                LoadingState()
            }
            return mushroomList
        }

        suspend fun addMushroom(
            nameMushroom: String,
            description: String,
            imagePath: String,
            mushroom: List<Mushroom>,
            latitude: Double,
            longitude: Double,
            userId: String?
        ): String {
            val db = FirebaseFirestore.getInstance()
            val imageUrl = uploadImageAndGetUrl(imagePath)
            val matchedMushroom = mushroom.find { it.commonName == nameMushroom } ?: Mushroom()

            val myMush = MyMushroom(
                nameMushroom,
                description,
                photo = imageUrl,
                dificulty = matchedMushroom.dificulty,
                habitat = matchedMushroom.habitat,
                isEdible = matchedMushroom.isEdible,
                latitude = latitude,
                longitude = longitude,
                scientificName = matchedMushroom.scientificName,
                seasons = matchedMushroom.seasons,
                timestamp = com.google.firebase.Timestamp.now()
            )

            db.addMushroom(myMush, userId!!)
            return "Mushroom added"
        }
    }
}