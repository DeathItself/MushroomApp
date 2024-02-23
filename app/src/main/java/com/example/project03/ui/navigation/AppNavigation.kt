package com.example.project03.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project03.ui.components.CameraScreen
import com.example.project03.ui.screens.restaurant.MostrarRestaurantes
import com.example.project03.ui.screens.restaurant.RecibirRestaurante
import com.example.project03.ui.screens.addMushrooms.AddMushroomScreen
import com.example.project03.ui.screens.authentication.LoginScreen
import com.example.project03.ui.screens.edit.EditMyMushroomScreen
import com.example.project03.ui.screens.edit.EditMyUserScreen
import com.example.project03.ui.screens.home.HomeScreen
import com.example.project03.ui.screens.maps.MapScreen
import com.example.project03.ui.screens.myMush.MostrarMisSetasScreen
import com.example.project03.ui.screens.myMush.MyMushroomDetailsScreen
import com.example.project03.ui.screens.permission.CamLocationPermission
import com.example.project03.ui.screens.permission.LocationPermission
import com.example.project03.ui.screens.quiz.QuizApp
import com.example.project03.ui.screens.users.MyUserDetailsScreen
import com.example.project03.ui.screens.wiki.MostrarSetasScreen
import com.example.project03.ui.screens.wiki.MushroomDetailsScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val auth = Firebase.auth
//    auth.signOut()
    val user = auth.currentUser
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = if (user!=null)AppScreens.HomeScreen.route else AppScreens.LoginScreen.route) {
        composable(route = AppScreens.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(route = AppScreens.MapScreen.route) {
            MapScreen(navController)
        }

        composable(route = AppScreens.AddMushroomScreen.route) {
            AddMushroomScreen(navController)
        }

        composable(route = AppScreens.CameraScreen.route) {
            CameraScreen(navController)
        }

        composable(route = AppScreens.LocationScreen.route) {
            LocationPermission(navController)
        }
        composable(route = AppScreens.CamLocationScreen.route) {
            CamLocationPermission(navController)
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
                navController, commonName ?: ""
            )
        }
        composable(
            route = AppScreens.MisSetasDetailsScreen.route + "/{myMushID}",
            arguments = listOf(navArgument("myMushID") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val myMushID = backStackEntry.arguments?.getString("myMushID")
            MyMushroomDetailsScreen(navController, myMushID ?: "")
        }

        composable(
            AppScreens.EditMyMushroomScreen.route + "/{myMushID}",
            arguments = listOf(navArgument("myMushID") {
                type = NavType.StringType
            })
        ) {
            val myMushID = it.arguments?.getString("myMushID")
            EditMyMushroomScreen(navController, myMushID ?: "")
        }
        composable(route = AppScreens.QuizScreen.route) {
            QuizApp(navController)
        }
        composable(route = AppScreens.WikiScreen.route) {
            MostrarSetasScreen(navController)
        }
        composable(route = AppScreens.RestaurantsScreen.route) {
            MostrarRestaurantes(navController)
        }
        composable(
            route = AppScreens.RestaurantInfo.route + "/{nom}",
            arguments = listOf(navArgument("nom") {
                type = NavType.StringType
            })
        ) {backStackEntry ->
            val nom = backStackEntry.arguments?.getString("nom")
            RecibirRestaurante(
                navController, nom ?: ""
            )
        }

        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.MyUserScreen.route) {
            MyUserDetailsScreen(navController)
        }
        composable(route = AppScreens.EditMyUserScreen.route) {
            EditMyUserScreen(navController)
        }
    }
}