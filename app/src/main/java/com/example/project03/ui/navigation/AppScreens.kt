package com.example.project03.ui.navigation

sealed class AppScreens(val route: String){
    object HomeScreen: AppScreens("home_screen")
    object MapScreen: AppScreens("map_screen")
    object AddMushroomScreen: AppScreens("add_mushroom_screen")
    object CameraScreen: AppScreens("camera_screen")
    object LocationScreen : AppScreens("location_screen")
    object CamLocationScreen: AppScreens("cam_location_screen")
    object HomeLocationScreen: AppScreens("home_location_permission")
    object MisSetasScreen: AppScreens("misSetas_screen")
    object SetasDetailsScreen: AppScreens("detail_screen")
    object MisSetasDetailsScreen: AppScreens("misSetas_detail_screen")
    object EditMyMushroomScreen: AppScreens("edit_mush_screen")
    object QuizScreen: AppScreens("item_menu/Learn")
    object WikiScreen: AppScreens("item_menu/Wiki")
    object RestaurantsScreen: AppScreens("item_menu/Eat")
    object RestaurantInfo: AppScreens("restaurantInfo_screen")
    object LoginScreen: AppScreens("login_screen")
    object MyUserScreen: AppScreens("user_screen")
    object EditMyUserScreen: AppScreens("edit_user_screen")
    object RankScreen: AppScreens("item_menu/Ranking")
    object WeatherScreen: AppScreens("item_menu/Weather")
    object ForumScreen: AppScreens("item_menu/Forum")


}