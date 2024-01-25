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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project03.model.BottomNavigation
import com.example.project03.viewmodel.MainViewModel

val items = listOf(
    BottomNavigation(
        title = "Home",
        icon = Icons.Rounded.Home
    ),

    BottomNavigation(
        title = "Mapa",
        icon = Icons.Rounded.Map
    ),

    BottomNavigation(
        title = "Añadir Seta",
        icon = Icons.Rounded.AddCircle
    ),

    BottomNavigation(
        title = "Mis Setas",
        icon = Icons.Rounded.ShoppingBasket
    ),

    BottomNavigation(
        title = "Menu",
        icon = Icons.Rounded.Menu
    )
)

@Preview
@Composable
fun BottomNavigationBar() {
    NavigationBar {
        val mainViewModel: MainViewModel = viewModel()
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == 0,
                    onClick = {
                          when(index){
                              0 -> Unit
                              1 -> Unit
                              2 -> Unit
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