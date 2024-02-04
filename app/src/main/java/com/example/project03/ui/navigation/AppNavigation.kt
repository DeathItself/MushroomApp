package com.example.project03.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project03.ui.screens.home.HomeScreen
import com.example.project03.ui.screens.maps.MapScreen
import com.example.project03.ui.screens.myMush.MostrarDatosScreen
import com.example.project03.ui.screens.myMush.MushroomDetailsScreen


@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route){
        composable(route = AppScreens.HomeScreen.route){
            HomeScreen(navController)
        }
        composable(route = AppScreens.MapScreen.route){
            MapScreen(navController)
        }
        composable(route = AppScreens.MisSetasScreen.route + "/{text}", arguments = listOf(
            navArgument(name = "text") {
                type = NavType.StringType
            }
        )) {
            MostrarDatosScreen(navController, it.arguments?.getString("text"))
        }
        composable(route = AppScreens.SetasDetailsScreen.route){
            MushroomDetailsScreen(navController)
        }
    }
}