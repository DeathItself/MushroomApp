package com.example.project03.ui.screens.permission

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.project03.ui.navigation.AppScreens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(navController: NavController){
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    var textoShow: String = ""
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LaunchedEffect(locationPermissionsState){
            if(locationPermissionsState.allPermissionsGranted){
                textoShow = "Volver al Mapa"
            }else{
                locationPermissionsState.launchMultiplePermissionRequest()
            }
        }

        Button(onClick = {navController.navigate(route = AppScreens.MapScreen.route) }) {
            Text(text = textoShow)
        }
    }

}