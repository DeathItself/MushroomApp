package com.example.project03.ui.screens.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.ui.components.TopBarWeather
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController){
    val mainViewModel: MainViewModel = viewModel()
    val isHome = false
    Scaffold(topBar = {
        TopBarWeather(isHome, navController)
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            WeatherContent(padding)
            //submenu
            if (mainViewModel.showBottomSheet) {
                ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                    ContentBottomSheet(mainViewModel, navController)
                }
            }
        }
    }
}

@Composable
fun WeatherContent(
    padding: PaddingValues
){

}
