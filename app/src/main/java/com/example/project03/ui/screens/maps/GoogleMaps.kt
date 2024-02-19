package com.example.project03.ui.screens.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.model.MyMushroom
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
    val isHome = true
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(isHome, navController)
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        ContentGoogleMaps(padding = padding, navController)

        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}


var permissionGranted: Boolean = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentGoogleMaps(padding: PaddingValues, navController: NavController) {
    val mushroom: List<MyMushroom> = Data.myMushDBList()
    val initialPosition = LatLng(41.564, 2.019)
    var selectedMushroom by remember { mutableStateOf<MyMushroom?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
//    val posicionSeta = LatLng(mushroom[0].latitude, mushroom[0].longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 15f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        if (showBottomSheet && selectedMushroom != null) {
            ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                // Aquí puedes definir cómo quieres mostrar la información de la seta seleccionada
                // Por ejemplo, podrías tener un composable que tome un objeto Mushroom y muestre su información
                MapContentBottomSheet(mushroom = selectedMushroom!!)
            }
        }

        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.HYBRID,
                isMyLocationEnabled = permissionGranted,
            ),

            ) {
            // Itera por cada seta y coloca un marcador en su ubicación
            mushroom.forEach { mushroom ->
                val position = LatLng(mushroom.latitude, mushroom.longitude)
                Marker(
                    onClick = {
                        selectedMushroom = mushroom // Almacena la seta seleccionada
                        showBottomSheet = true // Muestra el ModalBottomSheet
                        true // Indica que el evento de clic ha sido manejado
                    },
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
fun MapContentBottomSheet(mushroom: MyMushroom) {
    // Implementa la UI para mostrar la información de la seta aquí
    // Por ejemplo, podrías mostrar el nombre, descripción, etc.
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .padding(horizontal = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .padding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isEdibleText = when (mushroom.isEdible) {
                true -> "Comestible"
                false -> "No comestible"
                else -> "Información no disponible"
            }
            MushDetailsMap(mushroom, isEdibleText)
        }
    }
}

@Composable
fun MushDetailsMap(mushroom: MyMushroom, isEdibleText: String) {
    Text(text = mushroom.commonName, fontWeight = FontWeight.Bold)
    AsyncImage(
        modifier = Modifier.size(200.dp), model = mushroom.photo, contentDescription = null
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = mushroom.scientificName, fontStyle = FontStyle.Italic)
    Column(
        modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mushroom.description,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = mushroom.habitat,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
    Text(text = isEdibleText, fontWeight = FontWeight.Bold)
    Text(text = mushroom.seasons, fontWeight = FontWeight.Bold)
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
                imageVector = Icons.Filled.GpsFixed, contentDescription = "Menu"
            )
        }

    }
}