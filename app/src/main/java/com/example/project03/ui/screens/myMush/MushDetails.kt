package com.example.project03.ui.screens.myMush

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.model.Mushroom
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.MainViewModel


@Composable
fun RecibirDatosSeta(mushroom: Mushroom, padding: PaddingValues) {
    
    val isEdibleText = if (mushroom.isEdible) "Comestible" else "No comestible"
    
    Column(
        modifier = Modifier.padding(padding) .fillMaxSize()
    ) {
        Text(text = mushroom.commonName, fontWeight = FontWeight.Bold)
        AsyncImage(modifier = Modifier.size(64.dp),model = mushroom.photo, contentDescription = null)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = mushroom.scientificName, fontStyle = FontStyle.Italic)
        Text(text = mushroom.description, fontWeight = FontWeight.Bold)
        Text(text = mushroom.habitat, fontWeight = FontWeight.Bold)
        Text(text = isEdibleText, fontWeight = FontWeight.Bold)
        Text(text = mushroom.seasons, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MushroomDetailsScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    var isHome by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBarWithoutScaffold(isHome)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        })
    {padding ->
        RecibirDatosSeta(mushroom = Mushroom(), padding = padding)
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mainViewModel.showBottomSheet = false }
            ) {
                ContentBottomSheet(mainViewModel)
            }
        }
    }
}
