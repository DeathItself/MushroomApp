package com.example.project03

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

val items = listOf(
    BottomNavigation(
        title = "Home",
        icon = Icons.Rounded.Home
    ),

    BottomNavigation(
        title = "Buscar",
        icon = Icons.Rounded.Search
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

@Composable
fun BottomNavigationBar() {
    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = index == 0,
                    onClick = { },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        }
    }
}