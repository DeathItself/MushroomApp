package com.example.project03.ui.screens.quiz

import android.widget.ListView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.model.Ranking
import com.example.project03.util.data.Data
import com.example.project03.viewmodel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun RankingScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val id = Firebase.auth.currentUser?.uid
    val rankings = Data.myRankList()
    val filterOptions = listOf(
        "Todos",
        "Más alto",
        "Más bajo"
    )
    val selectedFilter = remember { mutableStateOf(filterOptions[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ranking de Puntuaciones",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Dropdown para seleccionar el filtro
        DropdownMenu(
            expanded = selectedFilter.value != filterOptions[0],
            onDismissRequest = { selectedFilter.value = filterOptions[0] },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            filterOptions.forEach { option ->
                DropdownMenuItem(
                    onClick = { selectedFilter.value = option }
                ) {
                    Text(text = option)
                }
            }
        }

        // Lista de rankings
        when (selectedFilter.value) {
            "Todos" -> {
                RankingsList(rankings)
            }
            "Más alto" -> {
                // Mostrar solo rankings de mayor a menor
                val rangos = rankings.sortedBy {it.puntuacion }
                RankingsList(rangos)
            }
            "Más bajo" -> {
                // Mostrar solo tu rango
                val userRanking = rankings.find { it.userId == id }
                if(userRanking != null){
                    RankingsList(userRanking)
                }

            }
        }
    }

}

@Composable
fun RankingsList(rankings: List<Ranking>) {
    ListView(
        rankings,
        modifier = Modifier.fillMaxWidth()
    ) { ranking ->
        RankingItem(ranking)
    }
}

@Composable
fun RankingItem(ranking: Ranking) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = ranking.name, modifier = Modifier.width(200.dp)) //necesito conseguir el nombre
        Text(text = ranking.puntuacion.toString(), modifier = Modifier.align(Alignment.CenterVertically))
    }
}
