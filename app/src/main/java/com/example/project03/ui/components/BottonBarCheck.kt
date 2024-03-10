package com.example.project03.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.model.BottomNavigation
import com.example.project03.model.ImagePath
import com.example.project03.ui.navigation.AppScreens
import java.io.File

@Composable
fun BottomBarCheck(navController: NavController){
    val selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        BottomNavigation (
            title = stringResource(R.string.cancel),
            icon = Icons.Rounded.Cancel
        ),

        BottomNavigation (
            title = stringResource(R.string.confirm),
            icon = Icons.Rounded.Check
        )
    )

    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ){
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItem == index,
                    onClick = {
                          when(index){
                              0 -> cancelPhoto(navController)
                              1 -> navController.navigate(route = AppScreens.AddMushroomScreen.route)
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

fun cancelPhoto(navController: NavController){
    val imagePath = ImagePath.imagePath
    deleteImageStorage(imagePath)
    ImagePath.imagePath = ""
    navController.navigate(route = AppScreens.CameraScreen.route)
}

fun deleteImageStorage(imagePath: String): Boolean{
    val file = File(imagePath)
    return file.delete()
}