package com.example.project03.ui.screens.maps

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
        ContentGoogleMaps(padding = padding)

        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mainViewModel.showBottomSheet = false }
            ) {
                ContentBottomSheet(mainViewModel,navController)
            }
        }
    }
}


@Composable
fun ContentGoogleMaps(padding: PaddingValues){
    val spain = LatLng(41.56667, 2.01667)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(spain, 15f)
    }

    LocationPermission()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ){
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.HYBRID)
        ) {
            Marker(
                state = MarkerState(position = spain),
                title = "Spain",
                snippet = "Marker in Spain"
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(){
    val permissionState = rememberMultiplePermissionsState(permissions = listOf(Manifest.permission_group.LOCATION))

    LaunchedEffect(true){
        permissionState.launchMultiplePermissionRequest()
    }

    if(permissionState.allPermissionsGranted){
        Text(text = "Permiso concendido")
    }else{
        Text(text = "Permiso denegado")
    }

}