package com.example.project03.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.ui.theme.interFamily
import com.example.project03.viewmodel.ApiGeocodingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWeather(isHome: Boolean, navController: NavController){
    var isSearching by remember { mutableStateOf(false) }
    var cityName by remember { mutableStateOf("") }
    val viewModel: ApiGeocodingViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        modifier = Modifier,
        title = {
            Text(
                "Ciudad",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = interFamily,
                fontWeight = FontWeight.Black
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
        actions = {
            if (isSearching){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        value = cityName,
                        onValueChange = {
                            cityName = it
                        },
                        label = { Text("Buscar ciudad") },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            isSearching=false
                            coroutineScope.launch {
                                viewModel.getCityCoordinates(cityName)
                                navController.navigate(navController.currentDestination?.route ?: "")
                            }

                        }
                    ){
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Buscar ciudad"
                        )
                    }

                }
                //SearchCityCoordinates()

            }else{
                IconButton(
                    onClick = { isSearching = true }
                ){
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
    )
}






