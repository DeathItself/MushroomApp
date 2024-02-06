package com.example.project03.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.model.BottomNavigation
import com.example.project03.viewmodel.MainViewModel


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigation(
            title = stringResource(R.string.BottomNavHome),
            icon = Icons.Rounded.Home
        ),

        BottomNavigation(
            title = stringResource( R.string.BottomNavMap),
            icon = Icons.Rounded.Map
        ),

        BottomNavigation(
            title = stringResource(R.string.BottomNavAdd),
            icon = Icons.Rounded.AddCircle
        ),

        BottomNavigation(
            title = stringResource(R.string.BottomNavMyMush),
            icon = Icons.Rounded.ShoppingBasket
        ),

        BottomNavigation(
            title = stringResource(R.string.BottomNavMenu),
            icon = Icons.Rounded.Menu
        )
    )
    NavigationBar {
        val mainViewModel: MainViewModel = viewModel()
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == 0,
                    onClick = {
                        when (index) {
                            0 -> navController.navigate(route = AppScreens.HomeScreen.route)
                            1 -> navController.navigate(route = AppScreens.MapScreen.route)
                            2 -> navController.navigate(route = AppScreens.AddMushroomScreen.route)
                            3 -> Unit
                            4 -> mainViewModel.showBottomSheet = true
                        }
                    },
                    icon = {

                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                    },
                    label = {
                        Text(
                            text = item.title,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )


            }
        }
    }
}