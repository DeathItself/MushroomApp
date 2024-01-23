package com.example.project03


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project03.ui.theme.Project03Theme
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
                    color = MaterialTheme.colorScheme.background
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun HomeScreen() {
        val mainViewModel: MainViewModel = viewModel()
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
        Column (
            modifier = Modifier
                .fillMaxSize()
                .height(280.dp)
                .padding(horizontal = 28.dp)
        ){
            Text(
                text = "Submenu",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

}



