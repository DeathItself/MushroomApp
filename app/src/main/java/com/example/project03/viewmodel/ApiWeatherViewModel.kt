package com.example.project03.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project03.model.CurrentWeatherData
import com.example.project03.model.DailyWeatherData
import com.example.project03.model.HourlyWeatherData
import com.example.project03.util.ApiService.WeatherApi
import kotlinx.coroutines.launch


class ApiWeatherViewModel() : ViewModel(){
    private val _currentWeatherData = MutableLiveData<CurrentWeatherData?>()
    val currentWeatherData: LiveData<CurrentWeatherData?> = _currentWeatherData

    private val _hourlyWeatherData = MutableLiveData<HourlyWeatherData?>()
    val hourlyWeatherData: LiveData<HourlyWeatherData?> = _hourlyWeatherData

    private val _dailyWeatherData = MutableLiveData<DailyWeatherData?>()
    val dailyWeatherData: LiveData<DailyWeatherData?> = _dailyWeatherData

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> = _longitude

    fun setCoordinates(latitude: Double, longitude: Double) {
        _latitude.value = latitude
        _longitude.value = longitude
    }

    fun getWeatherData(dataType: String) {
        val lat = latitude.value ?: return
        val lng = longitude.value ?: return
        GetWeatherData(dataType, lat, lng)
    }


     fun GetWeatherData(dataType: String, latitude: Double, longitude: Double){
        viewModelScope.launch{
            //val (latitude, longitude) = getMyLocation(context)
            try {
                val weatherData = when (dataType) {
                    "current" -> WeatherApi.retrofitService.getWeather(latitude, longitude, current = "temperature_2m,apparent_temperature,precipitation,rain,wind_speed_10m,wind_direction_10m,is_day")
                    "hourly" -> WeatherApi.retrofitService.getWeather(latitude, longitude, hourly = "temperature_2m,apparent_temperature,precipitation,rain,snowfall,cloud_cover_low,cloud_cover_mid,cloud_cover_high,wind_speed_100m,wind_direction_100m,temperature_100m")
                    "daily" -> WeatherApi.retrofitService.getWeather(latitude, longitude, daily = "temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,precipitation_hours,wind_direction_10m_dominant")
                    else -> null // Puedes manejar otros casos según tus necesidades
                }
                when (dataType) {
                    "current" -> _currentWeatherData.value = weatherData?.current
                    "hourly" -> _hourlyWeatherData.value = weatherData?.hourly
                    "daily" -> _dailyWeatherData.value = weatherData?.daily
                }

                Log.d("Mushtool", "GetWeather: $weatherData")
            } catch (e: Exception) {
                Log.e("Mushtool", "Error fetching weather  data: ${e.message}", e)
            }
        }
    }
}