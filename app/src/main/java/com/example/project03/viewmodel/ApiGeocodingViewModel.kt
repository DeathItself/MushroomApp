package com.example.project03.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project03.model.GeocodingResponse
import com.example.project03.util.ApiService.apiKey

class ApiGeocodingViewModel() : ViewModel() {
    private val _cityCoordinates = MutableLiveData<GeocodingResponse>()
    val cityCoordinates: LiveData<GeocodingResponse> = _cityCoordinates

    suspend fun getCityCoordinates(cityName: String){
            try {
                val coordinates = com.example.project03.util.ApiService.GeocodingApi.retrofitGeocoding.getCoordinates(cityName, apiKey)
                _cityCoordinates.value = coordinates
                Log.d("Mushtool", "GetWeather: $coordinates")
            }catch (e: Exception){
                Log.e("Mushtool", "Error fetching weather  data: ${e.message}", e)
            }
        }
}