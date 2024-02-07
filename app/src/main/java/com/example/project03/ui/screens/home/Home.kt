package com.example.project03.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.ui.components.BannerCard
import com.example.project03.ui.components.CarouselCard
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    var isHome by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome,navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {padding ->
        ContentHomeScreen(padding = padding)

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
@Composable
fun ContentHomeScreen(padding: PaddingValues){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)

    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        BannerCard()
        CarouselCard()
    }
}