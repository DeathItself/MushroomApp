package com.example.project03.ui.screens.maps

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
import com.example.project03.model.Mushroom
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.util.data.Data
import com.example.project03.viewmodel.MainViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    var isHome by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        ContentGoogleMaps(padding = padding, navController)

        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mainViewModel.showBottomSheet = false }
            ) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}


var permissionGranted: Boolean = false

@Composable
fun ContentGoogleMaps(padding: PaddingValues, navController: NavController) {
    val mushroom: List<Mushroom> = Data.DbCall()
    var i: Int = 0
    val initialPosition = LatLng(41.564, 2.019)
//    val posicionSeta = LatLng(mushroom[0].latitude, mushroom[0].longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 15f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.SATELLITE,
                isMyLocationEnabled = permissionGranted,
            ),
        ) {
            // Itera por cada seta y coloca un marcador en su ubicación
            mushroom.forEach { mushroom ->
                val position = LatLng(mushroom.latitude, mushroom.longitude)
                Marker(
                    state = MarkerState(position = position),
                    title = mushroom.commonName, // Usa el nombre común de la seta como título
                    snippet = "Lat: ${mushroom.latitude}, Long: ${mushroom.longitude}" // Opcional: muestra latitud y longitud como snippet
                )
            }
        }
        if (!permissionGranted) ButtonGps(navController)

    }
}

@Composable
fun ButtonGps(navController: NavController) {
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