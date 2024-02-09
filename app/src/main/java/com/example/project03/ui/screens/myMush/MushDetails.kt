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
import com.example.project03.util.data.Data
import com.example.project03.viewmodel.MainViewModel


@Composable
fun RecibirDatosSeta(mushroom: Mushroom, padding: PaddingValues, s: String) {

     val mushObj = Data.DbCall().find { it.commonName == s }





    Column(
        modifier = androidx.compose.ui.Modifier
            .padding(padding)
            .fillMaxSize()
    ) {
        if (mushObj != null) {
            val isEdibleText = if (mushObj.isEdible) "Comestible" else "No comestible"
            Text(text = mushObj.commonName, fontWeight = FontWeight.Bold)
            AsyncImage(modifier = Modifier.size(64.dp),model = mushObj.photo, contentDescription = null)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = mushObj.scientificName, fontStyle = FontStyle.Italic)
            Text(text = mushObj.description, fontWeight = FontWeight.Bold)
            Text(text = mushObj.habitat, fontWeight = FontWeight.Bold)
            Text(text = isEdibleText, fontWeight = FontWeight.Bold)
            Text(text = mushObj.seasons, fontWeight = FontWeight.Bold)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MushroomDetailsScreen(navController: NavController, s: String) {
    val mainViewModel: MainViewModel = viewModel()
    var isHome by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopAppBarWithoutScaffold(isHome,navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        })
    {padding ->
        if (s != null) {
            RecibirDatosSeta(mushroom = Mushroom(), padding = padding, s)
        }
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mainViewModel.showBottomSheet = false }
            ) {
                ContentBottomSheet(mainViewModel,navController)
            }
        }
    }
}
