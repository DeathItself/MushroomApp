package com.example.project03.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project03.model.GeocodingResponse
import com.example.project03.util.ApiService.GeocodingApi
import com.example.project03.util.ApiService.apiKey

class ApiGeocodingViewModel() : ViewModel() {
    private val _cityCoordinates = MutableLiveData<GeocodingResponse>()
    val cityCoordinates: LiveData<GeocodingResponse> = _cityCoordinates

    suspend fun getCityCoordinates(cityName: String){
        try {
            val coordinates = GeocodingApi.retrofitGeocoding.getCoordinates(cityName, apiKey)
            _cityCoordinates.value = coordinates
            Log.d("CoordenadasViewmodel", "GetCoordenadas: ${_cityCoordinates.value}")
            Log.d("Coordenadas", "CityCordinates ${cityCoordinates.value}")
        }catch (e: Exception){
            Log.e("CoordenadasViewmodel", "Error fetching weather  data: ${e.message}", e)
        }
    }
}