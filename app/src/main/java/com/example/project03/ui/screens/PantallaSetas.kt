package com.example.project03.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project03.R
import com.example.project03.model.Mushroom
import com.google.firebase.firestore.FirebaseFirestore


val mushrooms: MutableList<Mushroom> = mutableListOf()

@Composable
fun MushroomList() {
    val db = FirebaseFirestore.getInstance()
    val mushroomRef = db.collection("setas")

    mushroomRef.get().addOnSuccessListener { documents ->
        for (document in documents) {
            val mushroomData = document.toObject(Mushroom::class.java)
            if (mushroomData != null) {
                mushrooms.add(mushroomData)
            }
        }
    }.addOnFailureListener { exception ->
        println("Error getting mushrooms from Firebase: ${exception.message}")
    }

    LazyColumn {

        items(mushrooms) { mushroom ->
            ElevatedCard {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxSize()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = mushroom.imageResourceId),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = mushroom.commonName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = mushroom.scientificName,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                    Button(
                        onClick = {
                            /* Do something! */
                        }
                    ) {
                        Text("Buy")
                    }
                }
            }

        }
    }
}

class Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MushroomList()
        }
    }
}