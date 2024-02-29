package com.example.project03.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector


data class WeatherResponse(
    val hourly: HourlyWeatherData? = null,
    val daily: DailyWeatherData? = null,
    val current: CurrentWeatherData? = null
)
data class HourlyWeatherData(
    val temperature_2m: List<Double>,
    val temperature_100m: List<Double>,
    val apparent_temperature: List<Double>,
    val precipitation: List<Double>,
    val rain: List<Double>,
    val snowfall: List<Double>,
    val cloud_cover_low: List<Double>,
    val cloud_cover_mid: List<Double>,
    val cloud_cover_high: List<Double>,
    val wind_speed_100m: List<Double>,
    val wind_direction_100m: List<Double>
)

data class DailyWeatherData(
    val temperature_2m_max: Double,
    val temperature_2m_min: Double,
    val apparent_temperature_max: Double,
    val apparent_temperature_min: Double,
    val sunrise: String,
    val sunset: String,
    val precipitation_hours: Double,
    val wind_direction_10m_dominant: Double
)

data class CurrentWeatherData(
    val temperature_2m: Double,
    val apparent_temperature: Double,
    val precipitation: Double,
    val rain: Double,
    val wind_speed_10m: Double,
    val wind_direction_10m: Double,
    val is_day: Int,
){
    fun getWeatherIcon(): ImageVector {
        return if (is_day == 1) {
            // Devuelve el ID del icono para el día
            Icons.Rounded.WbSunny
        } else {
            // Devuelve el ID del icono para la noche
            Icons.Rounded.NightsStay
        }
    }
}