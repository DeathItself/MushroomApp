package com.example.project03.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project03.model.GeocodingResponse
import com.example.project03.util.ApiService.GeocodingApi
import com.example.project03.util.ApiService.apiKey

class ApiGeocodingViewModel() : ViewModel() {
    private val _cityCoordinates = MutableLiveData<GeocodingResponse>()
    val cityCoordinates: LiveData<GeocodingResponse> = _cityCoordinates

    @Composable
    fun getCityCoordinates(cityName: String){
        LaunchedEffect(true){
            try {
                val coordinates = GeocodingApi.retrofitGeocoding.getCoordinates(cityName, apiKey)
                _cityCoordinates.value = coordinates
                Log.d("Mushtool", "GetWeather: $coordinates")
            }catch (e: Exception){
                Log.e("Mushtool", "Error fetching weather  data: ${e.message}", e)
            }

        }
    }
}