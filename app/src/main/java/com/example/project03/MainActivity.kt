package com.example.project03


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project03.ui.components.TopAppBarWithScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.theme.Project03Theme
import com.example.project03.ui.navigation.Items_BottomSheet.*
import com.example.project03.viewmodel.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Project03Theme {
                // A surface container using the 'background' color from the theme
                SetBarColor(color = MaterialTheme.colorScheme.background)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground

                ) {
                    HomeScreen()
                }
            }
        }
    }

    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = color
            )
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen() {
        val mainViewModel: MainViewModel = viewModel()
        var isHome by remember { mutableStateOf(true) }
        Scaffold(
            bottomBar = {
                BottomNavigationBar()
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)

            ) {
                //Secciones
                TopAppBarWithScaffold(isHome)
            }

            if(mainViewModel.showBottomSheet){
                ModalBottomSheet(
                    onDismissRequest = {mainViewModel.showBottomSheet = false}
                ) {
                    ContentBottomSheet(mainViewModel)
                }
            }
        }
    }

    @Composable
    fun ContentBottomSheet(
        mainViewModel: MainViewModel
    ) {
        val items_menu = listOf(
            Item_menu01,
            Item_menu02,
            Item_menu03
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .height(280.dp)
                .padding(
                    horizontal = 28.dp,

                    )
        ){
            items_menu.forEach{item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(48.dp)
                ){
                    Icon(item.icon, item.title)
                    Spacer(
                        modifier = Modifier
                            .width(24.dp)
                    )
                    Text(
                        text = item.title
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

        }
    }

}



