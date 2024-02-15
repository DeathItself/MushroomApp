package com.example.project03.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project03.ui.components.CameraScreen
import com.example.project03.ui.screens.addMushrooms.AddMushroomScreen
import com.example.project03.ui.screens.home.HomeScreen
import com.example.project03.ui.screens.maps.MapScreen
import com.example.project03.ui.screens.myMush.MostrarMisSetasScreen
import com.example.project03.ui.screens.myMush.MushroomDetailsScreen
import com.example.project03.ui.screens.permission.LocationPermission
import com.example.project03.ui.screens.quiz.QuizApp
import com.example.project03.ui.screens.wiki.MostrarSetasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.HomeScreen.route) {
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = AppScreens.MapScreen.route) {
            MapScreen(navController)
        }

        composable(route = AppScreens.AddMushroomScreen.route){
            AddMushroomScreen(navController)
        }


        composable(route = AppScreens.CameraScreen.route){
            CameraScreen()
        }

        composable(route = AppScreens.LocationScreen.route){
            LocationPermission(navController)
        }
        composable(route = AppScreens.MisSetasScreen.route) {
            MostrarMisSetasScreen(navController)
        }
        composable(
            route = AppScreens.SetasDetailsScreen.route + "/{commonName}",
            arguments = listOf(navArgument("commonName") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val commonName = backStackEntry.arguments?.getString("commonName")
            MushroomDetailsScreen(
                navController, commonName ?: "")
        }
        composable(route = AppScreens.QuizScreen.route){
            QuizApp(navController)
        }
        composable(route = AppScreens.WikiScreen.route){
            MostrarSetasScreen(navController)
        }
    }
}