package com.example.project03.ui.screens.permission

import android.Manifest
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.screens.maps.permissionGranted
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeLocationPermission(navController: NavController) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(locationPermissionsState) {
        if (!locationPermissionsState.allPermissionsGranted) {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    if (locationPermissionsState.allPermissionsGranted) {
        permissionGranted = true
        navController.navigate(AppScreens.HomeScreen.route)
    } else {
        Text(text = "Esperando permisos")// Puedes mostrar algún mensaje o UI aquí mientras esperas la respuesta del usuario
    }
}