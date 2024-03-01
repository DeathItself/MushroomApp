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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.model.CityName
import com.example.project03.viewmodel.ApiGeocodingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWeather(isHome: Boolean, navController: NavController){
    var isSearching by remember { mutableStateOf(false) }
    var adress by remember { mutableStateOf<CityName?>(null) }
    var cityName by remember { mutableStateOf("") }
    val viewModel: ApiGeocodingViewModel = viewModel()

    TopAppBar(
        modifier = Modifier,
        title = {
            Text(
                "Ciudad",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                        onValueChange = { cityName = it },
                        label = { Text("Buscar ciudad") },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            isSearching=false
                            //adress!!.cityName = cityName
                        }
                    ){
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Buscar ciudad"
                        )
                    }
                    //viewModel.getCityCoordinates(adress!!.cityName)
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



