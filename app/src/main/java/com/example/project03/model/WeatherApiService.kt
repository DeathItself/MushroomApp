package com.example.project03.model

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/meteofrance")
    suspend fun getWeather(
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Query("current") current: String? = null,
        @Query("hourly") hourly: String? = null,
        @Query("daily") daily: String? = null
    ): WeatherResponse
}
