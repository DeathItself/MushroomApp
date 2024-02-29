package com.example.project03.util.ApiService

import com.example.project03.model.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/*
https://api.open-meteo.com/v1/meteofrance?latitude=40&longitude=-4&current=temperature_2m,apparent_temperature,precipitation,rain,wind_speed_10m,wind_direction_10m&hourly=temperature_2m,apparent_temperature,precipitation,rain,snowfall,cloud_cover_low,cloud_cover_mid,cloud_cover_high,wind_speed_100m,wind_direction_100m,temperature_100m&daily=temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,precipitation_hours,wind_direction_10m_dominant&timezone=auto
 */
private const val BASE_URL = "https://api.open-meteo.com/"

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()
object WeatherApi {
    val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}
