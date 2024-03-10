package com.example.project03.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.theme.interFamily
import com.example.project03.viewmodel.ApiWeatherViewModel


@Composable
fun WeatherBanner(viewModel: ApiWeatherViewModel, navController: NavController) {
    val currentWeatherData by viewModel.currentWeatherData.observeAsState()
    val ubicacion = "Terrassa"

    if (currentWeatherData != null) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clickable {
                    navController.navigate(route = AppScreens.WeatherScreen.route)
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 60.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = currentWeatherData!!.getWeatherIcon(),
                    contentDescription = "ClimaIcono",
                    modifier = Modifier
                        .size(62.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxSize()
                ) {
                    Row {
                        Text(
                            text = "${currentWeatherData?.temperature_2m}" + "º",
                            fontFamily = interFamily,
                            fontSize = 34.sp
                        )
                    }
                    Row {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = "ubicacion",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = ubicacion,
                            fontFamily = interFamily,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = stringResource(R.string.termic_sensation) + ": " + currentWeatherData?.apparent_temperature + "º",
                        fontFamily = interFamily,
                        fontSize = 14.sp
                    )

                    Text(
                        text = stringResource(R.string.precipitation) + ": " + currentWeatherData?.precipitation + "%",
                        fontFamily = interFamily,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
