package com.example.project03.ui.screens.restaurant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.R
import com.example.project03.model.Restaurants
import com.example.project03.ui.components.TopAppBarWithoutScaffold
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

@Composable
fun RecibirDatosRestaurante(padding: PaddingValues, s: String) {

    val restObj = Data.restaurantList().find { it.nom == s }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (restObj != null) {
            Text(text = restObj.nom, fontWeight = FontWeight.Bold)
            AsyncImage(
                modifier = Modifier.size(200.dp), model = restObj.photo, contentDescription = null
            )
            Spacer(modifier = Modifier.height(16.dp))
            MapaRestaurante(restObj)
        }
    }
}

@Composable
fun MapaRestaurante(restaurants: Restaurants) {
    // Minimapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(restaurants.latitude, restaurants.longitud), 15f
        )
    }
    GoogleMap(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(horizontal = 26.dp),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapType = MapType.HYBRID
        )
    ) {
        if (restaurants != null) {
            Marker(
                state = MarkerState(position = LatLng(restaurants.latitude, restaurants.longitud)),
                title = restaurants.nom
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecibirRestaurante(navController: NavController, s: String) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = false
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(
            isHome, navController, title = stringResource(R.string.restaurants)
        )
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        RecibirDatosRestaurante(padding = padding, s)
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}