package com.example.project03.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.theme.interFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithoutScaffold(isHome: Boolean, navController: NavController, title: String ) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    SmallTopAppBarM3(
        scrollBehavior = scrollBehavior, isHome = isHome, navController = navController, title = title
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarM3(
    scrollBehavior: TopAppBarScrollBehavior, isHome: Boolean, navController: NavController, title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontFamily = interFamily,
                fontWeight =  FontWeight.Bold
            )
        },
        navigationIcon = {
            if (!isHome) {
                run {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.MyUserScreen.route) }) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null)
            }
        }
    )
}
