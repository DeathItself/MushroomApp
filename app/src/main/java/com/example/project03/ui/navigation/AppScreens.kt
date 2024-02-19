package com.example.project03.ui.navigation

sealed class AppScreens(val route: String){
    object HomeScreen: AppScreens("home_screen")
    object MapScreen: AppScreens("map_screen")
    object AddMushroomScreen: AppScreens("add_mushroom_screen")
    object CameraScreen: AppScreens("camera_screen")

    object LocationScreen : AppScreens("location_screen")
    object MisSetasScreen: AppScreens("misSetas_screen")
    object SetasDetailsScreen: AppScreens("detail_screen")
    object QuizScreen: AppScreens("item_menu/Learn")
    object WikiScreen: AppScreens("item_menu/Wiki")
    object LoginScreen: AppScreens("login_screen")
    object RegisterScreen: AppScreens("register_screen")

}