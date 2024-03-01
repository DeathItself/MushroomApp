package com.example.project03.util.ApiService

import com.example.project03.model.GeocodingApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_MAP = "https://maps.googleapis.com"

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL_MAP)
    .build()

val apiKey: String = System.getenv("API_KEY")

object GeocodingApi{
    val retrofitGeocoding: GeocodingApiService by lazy{
        retrofit.create(GeocodingApiService::class.java)
    }
}
