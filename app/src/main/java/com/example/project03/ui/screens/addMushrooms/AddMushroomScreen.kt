package com.example.project03.ui.screens.addMushrooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.ui.components.AddCard
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMushroomScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = true
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(isHome, navController, title = stringResource(R.string.add_mushroom))
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        ContentAddMushroom(padding, navController)

        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}

@Composable
fun ContentAddMushroom(
    padding: PaddingValues, navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddCard(navController)
    }
}