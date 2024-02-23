package com.example.project03.ui.screens.restaurant

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
import com.example.project03.model.Restaurants
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.util.data.Data
import com.example.project03.viewmodel.MainViewModel


@Composable
fun PantallaRestaurantes(navController: NavController) {
    RestaurantList(restaurants = Data.restaurantList(), navController)
}

@Composable
fun EmptyState() {
    Column {
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "No hay restaurantes",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarRestaurantes(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = false
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(isHome, navController)
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        Column(Modifier.padding(padding)) {
            PantallaRestaurantes(navController)
        }
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}

@Composable
fun RestaurantList(restaurants: List<Restaurants>, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (restaurants.isEmpty()) {
            item {
                com.example.project03.ui.screens.myMush.EmptyState()
            }
        }
        items(restaurants) { restaurant ->
            ElevatedCard(
                modifier = Modifier
                    .padding(2.dp)
                    .clickable {
                               navController.navigate(
                                   AppScreens.RestaurantInfo.route + "/" + restaurant.nom
                               )
                    },
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
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
                            model = restaurant.photo,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = restaurant.nom,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}