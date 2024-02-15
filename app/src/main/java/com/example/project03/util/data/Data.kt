package com.example.project03.util.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.project03.model.Mushroom
import com.example.project03.model.MyMushroom
import com.example.project03.util.db.AddMushroom
import com.example.project03.util.db.getMushrooms
import com.example.project03.util.db.getMyMushrooms
import com.google.firebase.firestore.FirebaseFirestore

class Data {
    companion object {
        @JvmStatic
        @Composable
        fun wikiDBList(): List<Mushroom> {
            val db = FirebaseFirestore.getInstance()
            var mushroomList by remember { mutableStateOf(listOf<Mushroom>()) }
            // Efecto lanzado para cargar los datos
            LaunchedEffect(key1 = Unit) {
                mushroomList = db.getMushrooms()
            }
            return mushroomList
        }

        @JvmStatic
        @Composable
        fun myMushDBList(): List<MyMushroom> {
            val db = FirebaseFirestore.getInstance()
            var mushroomList by remember { mutableStateOf(listOf<MyMushroom>()) }
            // Efecto lanzado para cargar los datos
            LaunchedEffect(key1 = Unit) {
                mushroomList = db.getMyMushrooms()
            }
            return mushroomList
        }

        @JvmStatic
        @Composable
        fun AddMushroom(mushroom: MyMushroom) {
            val db = FirebaseFirestore.getInstance()
            LaunchedEffect(key1 = Unit) {
                db.AddMushroom(mushroom)
            }
        }
    }
}