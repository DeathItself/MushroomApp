package com.example.project03.model

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("/maps/api/geocode/json")
    suspend fun getCoordinates(
        @Query("address") cityName: String,
        @Query("key") apiKey: String?
    ): GeocodingResponse
}