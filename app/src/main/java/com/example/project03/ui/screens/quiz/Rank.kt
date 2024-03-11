package com.example.project03.ui.screens.quiz

import androidx.compose.foundation.background
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
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.model.Ranking
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.ui.theme.interFamily
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
    val isHome = true
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
        stringResource(R.string.all_rank),
        stringResource(R.string.higher),
        stringResource(R.string.lower),
        stringResource(R.string.mine),
        stringResource(R.string.newer),
        stringResource(R.string.older)
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
            Column (modifier = Modifier.padding(16.dp)
                .clip(MaterialTheme.shapes.small)
            ){
                DropdownList(
                    itemList = filterOptions, // Use your list of filter options
                    selectedIndex = selectedFilter.value.indexOf(selectedFilter.value), // Get index of selected option
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.small),
                    onItemClick = { index ->
                        selectedFilter.value =
                            filterOptions[index] // Update selected filter based on index
                    }
                )
            }

            // Show rankings based on selected filter
            when (selectedFilter.value) {
                stringResource(R.string.all_rank) -> {
                    RankingsList(
                        rankings.sortedByDescending { it.puntuacion },
                        modifier = Modifier
                    ) // Highest to lowest
                }

                stringResource(R.string.higher) -> {
                    RankingsList(
                        rankings.sortedByDescending { it.puntuacion },
                        modifier = Modifier
                    ) // Highest to lowest
                }

                stringResource(R.string.lower) -> {
                    RankingsList(
                        rankings.sortedBy { it.puntuacion },
                        modifier = Modifier
                    ) // Lowest to highest
                }

                stringResource(R.string.mine) -> {
                    val userRanking = rankings.find { it.userId == id }
                    if (userRanking != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(stringResource(R.string.your_score)+": "+userRanking.puntuacion)
                            Button(
                                onClick = { navController.navigate(route = AppScreens.QuizScreen.route) },
                                modifier = Modifier.padding(start = 16.dp)
                            ) {
                                Text(stringResource(R.string.retry))
                            }
                        }
                    } else {
                        Text(stringResource(R.string.your_rank_not_found))
                    }
                }

               stringResource(R.string.newer) -> {
                    RankingsList(
                        rankings.sortedByDescending { it.timestamp },
                        modifier = Modifier
                    ) // Ordenado por más nuevo
                }

                stringResource(R.string.older) -> {
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.global_score),
                modifier = Modifier.padding(12.dp), fontFamily = interFamily, fontWeight = FontWeight.Bold, fontSize = 30.sp)
            LazyColumn {
                items(rankings) { ranking ->
                    RankingItem(ranking)
                }
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
    ElevatedCard(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .fillMaxSize(), colors = CardColors(
            MaterialTheme.colorScheme.inverseOnSurface,
            MaterialTheme.colorScheme.inverseSurface,
            Color.Transparent,
            Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(text = userName, modifier = Modifier.width(270.dp), fontFamily = interFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
            Text(
                text = ranking.puntuacion.toString(),
                fontFamily = interFamily,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
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
            .background(MaterialTheme.colorScheme.inverseOnSurface) // Use a neutral background color
            .clickable { showDropdown = true }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = itemList[selectedIndex],
            fontFamily = interFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            modifier = Modifier.fillMaxWidth().
            padding(16.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            itemList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        onItemClick(index)
                        showDropdown = false
                    }
                ) {
                    Text(
                        text = item,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
