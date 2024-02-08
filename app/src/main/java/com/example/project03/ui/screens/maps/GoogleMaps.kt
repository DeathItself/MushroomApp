package com.example.project03.ui.screens.maps

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
private var lastKnownLocation: Location? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController){
    val mainViewModel: MainViewModel = viewModel()
    var isHome by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome,navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {padding ->
        ContentGoogleMaps(padding = padding, navController)

        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mainViewModel.showBottomSheet = false }
            ) {
                ContentBottomSheet(mainViewModel)
            }
        }
    }

}

var permissionGranted: Boolean= false
@Composable
fun ContentGoogleMaps(padding: PaddingValues, navController: NavController){
    val spain = LatLng(41.56667, 2.01667)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(spain, 15f)
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ){
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.HYBRID,
                isMyLocationEnabled = permissionGranted,
                ),
        ) {
            Marker(
                state = MarkerState(position = spain),
                title = "Spain",
                snippet = "Marker in Spain"
            )
        }
        if (!permissionGranted)ButtonGps(navController)

    }
}

@Composable
fun ButtonGps(navController: NavController){
    Column {
        IconButton(
            onClick = {

                navController.navigate(route = AppScreens.LocationScreen.route)

            },
        ) {
            Icon(
                imageVector = Icons.Filled.GpsFixed,
                contentDescription = "Menu"
            )
        }

    }
}