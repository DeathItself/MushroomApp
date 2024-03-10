package com.example.project03.ui.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.model.Ranking
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
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
    val userMaxScores = mutableMapOf<String, Double>() // Mapa para almacenar puntuaciones máximas
    val id = Firebase.auth.currentUser?.uid
    val isHome = false
    val rankings = Data.myRankList() // Assuming Data.myRankList fetches all scores
    val filteredRankings = rankings.filter { ranking ->
        val userId = ranking.userId
        val maxScore = userMaxScores[userId] ?: 0.0

        // Mostrar solo si la puntuación del ranking es la máxima
        ranking.puntuacion == maxScore
    }
    for (ranking in rankings) {
        val userId = ranking.userId
        val score = ranking.puntuacion

        // Actualizar la puntuación máxima del usuario si es mayor
        if ((userMaxScores[userId]?.compareTo(score) ?: 0) < 0) {
            userMaxScores[userId] = score
        }
    }

    val filterOptions = listOf(
        "Todos",
        "Más alto",
        "Más bajo",
        "Mío",
        "Mas nuevo",
        "Mas antiguo"
    )
    val selectedFilter = remember { mutableStateOf(filterOptions[0]) }

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
            // Dropdown for filter selection
            DropdownList(
                itemList = filterOptions, // Use your list of filter options
                selectedIndex = selectedFilter.value.indexOf(selectedFilter.value), // Get index of selected option
                modifier = Modifier.fillMaxWidth(),
                onItemClick = { index ->
                    selectedFilter.value =
                        filterOptions[index] // Update selected filter based on index
                }
            )
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
                                onClick = { navController.navigate(route = AppScreens.QuizScreen.route) },
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                Text("Mejorar puntuación")
                            }
                        }
                    } else {
                        Text("No se encontró tu rango")
                    }
                }

                "Mas nuevo" -> {
                    RankingsList(
                        rankings.sortedByDescending { it.timestamp },
                        modifier = Modifier
                    ) // Ordenado por más nuevo
                }

                "Mas antiguo" -> {
                    RankingsList(
                        rankings.sortedBy { it.timestamp },
                        modifier = Modifier
                    ) // Ordenado por mas antiguo
                }
            }
        }
    }
    if (mainViewModel.showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
            ContentBottomSheet(mainViewModel, navController)
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

@Composable
fun DropdownList(
    itemList: List<String>,
    selectedIndex: Int,
    modifier: Modifier,
    onItemClick: (Int) -> Unit
) {
    var showDropdown by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .background(Color.White) // Use a neutral background color
            .border(width = 1.dp, color = Color.Gray)
            .clickable { showDropdown = true }
            .padding(all = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = itemList[selectedIndex])

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            itemList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        onItemClick(index)
                        showDropdown = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}
