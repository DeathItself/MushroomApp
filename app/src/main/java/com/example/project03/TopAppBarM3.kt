package com.example.project03

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun TopAppBarWithScaffold() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        if (scrollBehavior.isPinned) {
            TopAppBarM3(scrollBehavior = scrollBehavior)
        } else {
            LargeTopAppBarM3(scrollBehavior = scrollBehavior)
        }
    }) {
        /* padding ->
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
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(title = { Text(text = "MushTool") }, scrollBehavior = scrollBehavior, actions = {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Filled.Face, contentDescription = null)
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarM3(
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(title = { Text(text = "MushTool") }, scrollBehavior = scrollBehavior, actions = {
        IconButton(onClick = { /*TODO*/}) {
            Icon(imageVector = Icons.Filled.Face, contentDescription = null)
        }
    })
}/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterTopAppBarM3(
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "MushTool") },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = { *//*TODO*//*
 }) {
                Icon(imageVector = Icons.Filled.Face, contentDescription = null)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumTopAppBarM3(
    scrollBehavior: TopAppBarScrollBehavior
) {
    MediumTopAppBar(
        title = { Text(text = "MushTool") },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = { *//*TODO*//*
 }) {
                Icon(imageVector = Icons.Filled.Face, contentDescription = null)
            }
        }
    )
}
*/
