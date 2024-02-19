package com.example.project03.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.project03.R
import androidx.navigation.NavController
import com.example.project03.ui.navigation.AppScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithoutScaffold(isHome: Boolean, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    if (isHome) {
        SmallTopAppBarM3(
            scrollBehavior = scrollBehavior,
            isHome = isHome,
            navController = navController
        )
    } else {
        LargeTopAppBarM3(
            scrollBehavior = scrollBehavior, isHome = isHome,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarM3(
    scrollBehavior: TopAppBarScrollBehavior, isHome: Boolean, navController: NavController
) {
    LargeTopAppBar(title = { Text(text = "MushTool") }, navigationIcon = {
        if (!isHome) {
            run {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Regresar")
                }
            }
        }
    }, scrollBehavior = scrollBehavior, actions = {
        IconButton(onClick = { navController.navigate(AppScreens.LoginScreen.route)}) {
            Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarM3(
    scrollBehavior: TopAppBarScrollBehavior, isHome: Boolean, navController: NavController
) {
    TopAppBar(title = { Text(text = "MushTool") }, navigationIcon = {
        if (!isHome) {
            run {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Regresar")
                }
            }
        }
    }, scrollBehavior = scrollBehavior, actions = {
        IconButton(onClick = { navController.navigate(AppScreens.LoginScreen.route)}) {
            Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null)
        }
    })
}
