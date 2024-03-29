package com.example.project03.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.ui.components.BannerCard
import com.example.project03.ui.components.CarouselCard
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.components.WeatherBanner
import com.example.project03.ui.components.getMyLocation
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.ui.theme.interFamily
import com.example.project03.util.data.Data
import com.example.project03.viewmodel.ApiWeatherViewModel
import com.example.project03.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = true
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(isHome, navController, title = "MushTool")
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ContentHomeScreen(padding = padding, navController = navController)

            //submenu
            if (mainViewModel.showBottomSheet) {
                ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                     ContentBottomSheet(mainViewModel, navController)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentHomeScreen(padding: PaddingValues, navController: NavController) {
    val context = LocalContext.current
    val viewModel: ApiWeatherViewModel = viewModel()
    val user = Data.getUserObj()
    LaunchedEffect(Unit){
        val(latitude, longitude) = getMyLocation(context)
        if (latitude == null || longitude == null){
            navController.navigate(AppScreens.HomeLocationScreen.route)
        }else{
            viewModel.setCoordinates(latitude, longitude)
            viewModel.GetWeatherData("current", latitude, longitude)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(vertical = 10.dp),

    ) {
        Text(
            modifier = Modifier
                .padding(start = 25.dp, bottom = 10.dp),
            fontSize = 30.sp,
            text = stringResource(R.string.welcome)+", " + user.username,
            fontWeight = FontWeight.Medium,
            fontFamily = interFamily
        )
        WeatherBanner(viewModel, navController)
        Spacer(modifier = Modifier.padding(10.dp))
        Text(
            modifier = Modifier.padding(start = 25.dp, bottom = 10.dp),
            text = stringResource(R.string.Mush_of_the_day),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = interFamily,
            fontWeight = FontWeight.Medium
        )
        BannerCard(navController = navController)
        Spacer(modifier = Modifier.padding(12.dp))
        CarouselCard(navController = navController)
    }
}