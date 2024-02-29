package com.example.project03.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project03.model.WeatherResponse
import com.example.project03.ui.components.getMyLocation
import com.example.project03.util.ApiService.WeatherApi

class ApiWeatherViewModel() : ViewModel(){
    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

     @Composable
     fun GetCurrentWeather(){
        val context = LocalContext.current
        LaunchedEffect(true){
            val (latitude, longitude) = getMyLocation(context)
            try {
                val current = WeatherApi.retrofitService.getWeather(latitude, longitude, current = "temperature_2m,apparent_temperature,precipitation,rain,wind_speed_10m,wind_direction_10m,is_day")
                _weatherData.value = current
                Log.d("Mushtool", "GetWeather: $current")
            } catch (e: Exception) {
                Log.e("Mushtool", "Error fetching weather  data: ${e.message}", e)
            }
        }

    }
}