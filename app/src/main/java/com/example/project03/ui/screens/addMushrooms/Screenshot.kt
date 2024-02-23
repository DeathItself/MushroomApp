package com.example.project03.ui.screens.addMushrooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.project03.model.ImagePath
import com.example.project03.ui.components.BottomBarCheck


@Composable
fun Screenshot (
    navController: NavController
){
    Scaffold(
        bottomBar = { BottomBarCheck(navController) }
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val imagePath = ImagePath.imagePath
            Image(
                painter = rememberAsyncImagePainter(imagePath),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp)
            )
        }
    }
}