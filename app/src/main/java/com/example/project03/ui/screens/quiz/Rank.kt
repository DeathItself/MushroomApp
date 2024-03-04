package com.example.project03.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.model.Ranking
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.util.data.Data
import com.example.project03.util.db.getUserName
import com.example.project03.viewmodel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val id = Firebase.auth.currentUser?.uid
    val isHome = false
    val rankings = Data.myRankList() // Assuming Data.myRankList fetches all scores
    val filterOptions = listOf(
        "Todos",
        "Más alto",
        "Más bajo",
        "Mío"
    )
    val selectedFilter = remember { mutableStateOf(filterOptions[0]) }
    val expanded = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController, "Ranking")
        }, bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Dropdown menu for filtering
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                filterOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedFilter.value = option
                            expanded.value = false // Cierra el menú después de seleccionar una opción
                        }
                    ) {
                        Text(text = option)
                    }
                }
            }

            // Show rankings based on selected filter
            when (selectedFilter.value) {
                "Todos" -> {
                    RankingsList(
                        rankings.sortedByDescending { it.puntuacion },
                        modifier = Modifier
                    ) // Highest to lowest
                }

                "Más alto" -> {
                    RankingsList(
                        rankings.sortedByDescending { it.puntuacion },
                        modifier = Modifier
                    ) // Highest to lowest
                }

                "Más bajo" -> {
                    RankingsList(
                        rankings.sortedBy { it.puntuacion },
                        modifier = Modifier
                    ) // Lowest to highest
                }

                "Mío" -> {
                    val userRanking = rankings.find { it.userId == id }
                    if (userRanking != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Tu puntuación: ${userRanking.puntuacion}")
                            Button(
                                onClick = { /* Handle user action */ },
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    } else {
                        Text("No se encontró tu rango")
                    }
                }
            }
        }
    }

}

@Composable
fun RankingsList(rankings: List<Ranking>, modifier: Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        LazyColumn {
            items(rankings) { ranking ->
                RankingItem(ranking)
            }
        }
    }
}


@Composable
fun RankingItem(ranking: Ranking) {
    val coroutineScope = rememberCoroutineScope()
    var userName by remember { mutableStateOf("") }

    LaunchedEffect(ranking.userId) {
        userName = coroutineScope.async { getUserName(ranking.userId) }.await().toString()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = userName, modifier = Modifier.width(200.dp))
        Text(
            text = ranking.puntuacion.toString(),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
