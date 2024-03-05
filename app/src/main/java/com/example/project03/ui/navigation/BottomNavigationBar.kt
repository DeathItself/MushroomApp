package com.example.project03.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.ui.theme.interFamily
import com.example.project03.viewmodel.MainViewModel

data class BottomNavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem(
            route = AppScreens.HomeScreen.route,
            title = stringResource(R.string.BottomNavHome),
            icon = Icons.Rounded.Home
        ), BottomNavigationItem(
            route = AppScreens.MapScreen.route,
            title = stringResource(R.string.BottomNavMap),
            icon = Icons.Rounded.Map
        ), BottomNavigationItem(
            route = AppScreens.AddMushroomScreen.route,
            title = stringResource(R.string.BottomNavAdd),
            icon = Icons.Rounded.AddCircle,
        ), BottomNavigationItem(
            route = AppScreens.MisSetasScreen.route,
            title = stringResource(R.string.BottomNavMyMush),
            icon = Icons.Rounded.ShoppingBasket
        ), BottomNavigationItem(
            route = "menu",
            title = stringResource(R.string.BottomNavMenu),
            icon = Icons.Rounded.Menu
        )
    )

    NavigationBar {
        val currentRoute = navController.currentDestination?.route
        val mainViewModel: MainViewModel = viewModel()

        items.forEach { item ->
            NavigationBarItem(selected = currentRoute == item.route, onClick = {
                if (item.route == "menu") {
                    mainViewModel.showBottomSheet = true
                } else {
                    navController.navigate(item.route) {
                        // Modify this to suit your navigation setup
                        // For example, to clear back stack:
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            }, icon = {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }, label = {
                Text(
                    text = item.title,
                    color = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = interFamily
                )
            })
        }
    }
}
