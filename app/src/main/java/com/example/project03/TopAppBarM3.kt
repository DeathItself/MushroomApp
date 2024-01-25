package com.example.project03

import android.annotation.SuppressLint
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TopAppBarWithScaffold(isHome: Boolean) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            LargeTopAppBarM3(scrollBehavior = scrollBehavior, isHome = isHome)
    }) {
         /*padding ->
         LazyColumn(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(padding),
             contentPadding = PaddingValues(16.dp)
         ) {
             items(50) {
                 ListItem(
                     headlineContent = { Text(text = "Item $it") },
                     leadingContent = {
                         Icon(
                             imageVector = Icons.Default.Face,
                             contentDescription = null
                         )
                     }
                 )
             }
         }*/
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
