package com.example.project03.ui.navigation

sealed class AppScreens(val route: String){
    object HomeScreen: AppScreens("home_screen")
    object MapScreen: AppScreens("map_screen")
    object AddMushroomScreen: AppScreens("add_mushroom_screen")
    object CameraScreen: AppScreens("camera_screen")
}