package com.example.project03.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project03.ui.screens.home.BannerCard


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopAppBarWithScaffold(isHome: Boolean) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LargeTopAppBarM3(scrollBehavior = scrollBehavior, isHome = isHome)

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
