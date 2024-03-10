package com.example.project03.ui.screens.weather

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.model.DailyWeatherData
import com.example.project03.ui.components.TopBarWeather
import com.example.project03.ui.components.getMyLocation
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.ui.theme.interFamily
import com.example.project03.viewmodel.ApiGeocodingViewModel
import com.example.project03.viewmodel.ApiWeatherViewModel
import com.example.project03.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import java.util.Calendar

data class DatoClimatico(
    val temperaturaMaxima: Double,
    val temperaturaMinima: Double,
    val apparent_temperature_max: Double,
    val apparent_temperature_min: Double,
    val sunrise: String? = null,
    val sunset: String? = null,
    val probabilidadPrecipitacion: Double,
    val wind_direction_10m_dominant: Double? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController){
    val mainViewModel: MainViewModel = viewModel()
    val viewModel: ApiWeatherViewModel = viewModel()
    val apiGeocodingViewModel: ApiGeocodingViewModel = viewModel()

    val isHome = false
    Scaffold(topBar = {
        TopBarWeather(isHome, navController)
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            WeatherContent(padding, viewModel, apiGeocodingViewModel)
            //submenu
            if (mainViewModel.showBottomSheet) {
                ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                    ContentBottomSheet(mainViewModel, navController)
                }
            }
        }
    }
}


@Composable
fun WeatherContent(
    padding: PaddingValues,
    viewModel: ApiWeatherViewModel,
    apiGeocodingViewModel: ApiGeocodingViewModel,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShowHourly(viewModel, apiGeocodingViewModel)
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Card(
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShowDaily(viewModel, apiGeocodingViewModel)
            }
        }


    }
}

@Composable
fun ShowHourly(
    viewModel: ApiWeatherViewModel,
    apiGeocodingViewModel: ApiGeocodingViewModel,
) {
    val context = LocalContext.current
    val cityCoordinates = apiGeocodingViewModel.cityCoordinates.observeAsState()

    Log.d("Coordenadas", "CityCordinates ${cityCoordinates.value}")
    LaunchedEffect(Unit){
        if (cityCoordinates.value == null || cityCoordinates.value!!.results.isEmpty()) {
            val(latitude, longitude) = getMyLocation(context)
            viewModel.setCoordinates(latitude!!, longitude!!)
            viewModel.getWeatherData("hourly")
            Log.d("Coordenadas", "GetMyLoation: $latitude" + ", $longitude")
        } else {
            // Si se ha buscado una ciudad, obtener los datos climáticos de esa ciudad
            val latitude = cityCoordinates.value!!.results[0].geometry.location.lat
            val longitude = cityCoordinates.value!!.results[0].geometry.location.lng
            viewModel.GetWeatherData("hourly", latitude, longitude)
            Log.d("Coordenadas", "GetMyLoccation: $latitude" + ", $longitude")
        }
    }

    val hourlyWeatherData by viewModel.hourlyWeatherData.observeAsState()

    val currentHour = remember { mutableStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
    LaunchedEffect(true) {
        while (true) {
            delay(3600000) // Esperar 1 hora (3600 segundos * 1000 milisegundos)
            currentHour.value = (currentHour.value + 1) % 24 // Actualizar la hora actual
        }
    }

    if(hourlyWeatherData != null){
        Text(
            text = "Horas",
            fontSize = 30.sp,
            fontFamily = interFamily
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Center
        ) {


            //val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

            repeat(hourlyWeatherData!!.temperature_2m.size) {index ->
                val hour = (currentHour.value + index) % 24
                Column(modifier = Modifier.fillMaxWidth()){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Text(
                            text = "${hour.toString().padStart(2, '0')}:00",
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 23.sp),
                            fontFamily = interFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                text = "${hourlyWeatherData!!.temperature_2m[index]}º",
                                modifier = Modifier
                                    .width(80.dp)
                                    .padding(horizontal = 12.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                fontFamily = interFamily
                            )

                            Spacer(modifier = Modifier.height(17.dp))

                            Row (modifier = Modifier.fillMaxWidth()){
                                Icon(
                                    imageVector = Icons.Rounded.WaterDrop,
                                    contentDescription = "ubicacion",
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "${hourlyWeatherData!!.precipitation[index]}%",
                                    modifier = Modifier
                                        .width(80.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                    fontFamily = interFamily
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp)) // Espacio entre cada hora
            }
        }
    }
}

@Composable
fun ShowDaily(
    viewModel: ApiWeatherViewModel,
    apiGeocodingViewModel: ApiGeocodingViewModel,
){
    val context = LocalContext.current
    val cityCoordinates = apiGeocodingViewModel.cityCoordinates.observeAsState()

    LaunchedEffect(Unit){
        if (cityCoordinates.value == null || cityCoordinates.value!!.results.isEmpty()) {
            val(latitude, longitude) = getMyLocation(context)
            viewModel.setCoordinates(latitude!!, longitude!!)
            viewModel.getWeatherData( "daily")
        } else {
            // Si se ha buscado una ciudad, obtener los datos climáticos de esa ciudad
            val latitude = cityCoordinates.value!!.results[0].geometry.location.lat
            val longitude = cityCoordinates.value!!.results[0].geometry.location.lng
            viewModel.GetWeatherData("daily", latitude, longitude)
        }
    }
    val dailyWeatherData  by viewModel.dailyWeatherData.observeAsState()

    val diasSemana = listOf(
        stringResource(R.string.monday),
        stringResource(R.string.tuesday),
        stringResource(R.string.wednesday),
        stringResource(R.string.thursday),
        stringResource(R.string.friday),
        stringResource(R.string.saturday),
        stringResource(R.string.sunday)
    )
    val calendar = Calendar.getInstance()
    val diaActual = calendar.get(Calendar.DAY_OF_WEEK) - 1

    val diasRestantes = mutableListOf<String>()

    for (i in 0 until 4) { // Obtener los próximos 4 días
        val dia = diasSemana[(diaActual + i) % 7] // Módulo 7 para que el índice esté dentro del rango de 0 a 6
        diasRestantes.add(dia)
    }


    if(dailyWeatherData != null){
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(R.string.next_days) +": ",
                fontSize = 30.sp,
                fontFamily = interFamily
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.days),
                    fontFamily = interFamily,
                    modifier = Modifier.width(87.dp)
                )
                Text(text = "Precip.%", fontFamily = interFamily)
                Text(text = "TºMax / Min", fontFamily = interFamily)
            }

            Spacer(modifier = Modifier.height(15.dp))

            for ((index,dia) in diasRestantes.withIndex()) {
                Column{
                    val datoClimatico = obtenerDatoClimaticoParaDia(dailyWeatherData!!, index)
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = dia,
                            fontSize = 21.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.width(110.dp),
                            fontFamily = interFamily
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.WaterDrop,
                                contentDescription = "ubicacion",
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "${datoClimatico.probabilidadPrecipitacion}",
                                fontSize = 16.sp,
                                fontFamily = interFamily
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "${datoClimatico.temperaturaMaxima}º/${datoClimatico.temperaturaMinima}º",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = interFamily
                        )
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }
        }
    }
}

fun obtenerDatoClimaticoParaDia(dailyWeatherData: DailyWeatherData, index: Int): DatoClimatico {
    val temperaturaMaxima = dailyWeatherData.temperature_2m_max[index]
    val temperaturaMinima = dailyWeatherData.temperature_2m_min[index]
    val precipitacion = dailyWeatherData.precipitation_hours[index]
    val apparentTemperatureMax = dailyWeatherData.apparent_temperature_max[index]
    val apparentTemperatureMin = dailyWeatherData.apparent_temperature_min[index]

    return DatoClimatico(
        temperaturaMaxima = temperaturaMaxima,
        temperaturaMinima = temperaturaMinima,
        probabilidadPrecipitacion = precipitacion,
        apparent_temperature_max = apparentTemperatureMax,
        apparent_temperature_min = apparentTemperatureMin,
    )
}
