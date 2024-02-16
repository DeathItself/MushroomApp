package com.example.project03.ui.screens.wiki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun WikiScreen(navController: NavController) {
    MushroomList(mushrooms = Data.wikiDBList(), navController)
}

@Composable
fun EmptyState() {
    Column {
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "No hay setas",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        ) // Ejemplo de componente de carga, puede ser sustituido por cualquier otro
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarSetasScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = false
    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        })
    { padding ->
        Column(Modifier.padding(padding)) {
            WikiScreen(navController)
        }
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mainViewModel.showBottomSheet = false }
            ) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MushroomList(mushrooms: List<Mushroom>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(mushrooms) { mushroom ->
            ElevatedCard(
                modifier = Modifier
                    .padding(2.dp)
                    .clickable {
                        navController.navigate(
                            "detail_screen/${mushroom.commonName}"
                        )
                    },
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                if (mushrooms.isEmpty()) {
                    EmptyState()
                } else {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 26.dp)
                        ) {
                            AsyncImage(
                                model = mushroom.photo,
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
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .padding(end = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}