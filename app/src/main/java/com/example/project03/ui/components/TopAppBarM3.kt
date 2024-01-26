package com.example.project03.ui.components

import androidx.compose.material.icons.Icons
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithoutScaffold(isHome: Boolean) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    if (isHome) {
        SmallTopAppBarM3(scrollBehavior = scrollBehavior, isHome = isHome)
    } else {
        LargeTopAppBarM3(scrollBehavior = scrollBehavior, isHome = isHome)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarM3(
    scrollBehavior: TopAppBarScrollBehavior, isHome: Boolean
) {
    LargeTopAppBar(title = { Text(text = "MushTool") }, navigationIcon = {
        if (!isHome) {
            run {
                IconButton(onClick = { /* acción de regreso */ }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Regresar")
                }
            }
        }
    }, scrollBehavior = scrollBehavior, actions = {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.Face, contentDescription = null)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBarM3(
    scrollBehavior: TopAppBarScrollBehavior, isHome: Boolean
) {
    TopAppBar(title = { Text(text = "MushTool") }, navigationIcon = {
        if (!isHome) {
            run {
                IconButton(onClick = { /* acción de regreso */ }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Regresar")
                }
            }
        }
    }, scrollBehavior = scrollBehavior, actions = {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.Face, contentDescription = null)
        }
    })
}
