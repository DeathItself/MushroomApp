package com.example.project03.ui.screens.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.R
import com.example.project03.model.MyMushroom
import com.example.project03.model.Restaurants
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.ui.theme.interFamily
import com.example.project03.util.data.Data
import com.example.project03.viewmodel.MainViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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
        TopAppBarWithoutScaffold(isHome, navController, title = stringResource(R.string.BottomNavMap))
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
    val restaurant: List<Restaurants> = Data.restaurantList()
    var selectedRestaurant by remember { mutableStateOf<Restaurants?>(null) }
    val initialPosition = LatLng(41.564, 2.019)
    var selectedMushroom by remember { mutableStateOf<MyMushroom?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBottomSheet2 by remember { mutableStateOf(false) }
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
                MapContentBottomSheet(mushroom = selectedMushroom!!)
            }
        }else if (showBottomSheet2 && selectedRestaurant != null){
            ModalBottomSheet(onDismissRequest = {showBottomSheet2 = false }) {
                MapContentBottomSheet2( restaurants = selectedRestaurant!!)
            }
        }

        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = MapType.HYBRID, isMyLocationEnabled = permissionGranted
            ),

            ) {
            // Itera por cada seta y coloca un marcador en su ubicación
            mushroom.forEach { mushroom ->
                val position = LatLng(mushroom.latitude!!, mushroom.longitude!!)
                val badMarker = BitmapDescriptorFactory.fromResource(R.drawable.bad_mush_map_pin_overlay)
                val goodMarker = BitmapDescriptorFactory.fromResource(R.drawable.good_mush_map_pin_overlay)
                val marker = if (mushroom.isEdible == true) goodMarker else badMarker
                Marker(
                    icon = marker,
                    onClick = {
                        selectedMushroom = mushroom // Almacena la seta seleccionada
                        showBottomSheet = true // Muestra el ModalBottomSheet
                        true // Indica que el evento de clic ha sido manejado
                    },
                    state = MarkerState(position = position),
                    title = mushroom.commonName, // Usa el nombre común de la seta como título
                    snippet = mushroom.scientificName
                )
            }
            restaurant.forEach { restaurants ->
                val position = LatLng(restaurants.latitude, restaurants.longitud)
                val marker =
                    BitmapDescriptorFactory.fromResource(R.drawable.icono_restaurante)
                Marker(
                    icon = marker,
                    onClick = {
                        selectedRestaurant = restaurants
                        showBottomSheet2 = true
                        true
                    },
                    state = MarkerState(position = position),
                    title = restaurants.nom
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
                true -> stringResource(R.string.edible)
                false -> stringResource(R.string.not_edible)
                else -> stringResource(R.string.unknown)
            }
            MushDetailsMap(mushroom, isEdibleText)
        }
    }
}

@Composable
fun MapContentBottomSheet2(restaurants: Restaurants) {
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
            RestaurantMap(restaurants)
        }
    }
}


@Composable
fun MushDetailsMap(mushroom: MyMushroom, isEdibleText: String) {
    Text(text = mushroom.commonName, fontWeight = FontWeight.Black, fontFamily = interFamily, fontSize = 30.sp)
    Spacer(modifier = Modifier.height(25.dp))
    AsyncImage(
        modifier = Modifier.size(200.dp), model = mushroom.photo, contentDescription = null
    )
    Spacer(modifier = Modifier.height(26.dp))
    Column(
        modifier = Modifier.padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row (modifier = Modifier.padding(3.dp)){
            Text(text = stringResource(R.string.scientific_name)+": ",
                fontSize = 18.sp,fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
            Text(
                text = mushroom.scientificName,
                fontSize = 20.sp,
                fontFamily = interFamily,
                fontStyle = FontStyle.Italic
            )
        }
        Row (modifier = Modifier.padding(3.dp)) {
            Text(text = stringResource(R.string.commentary)+": ",
                fontSize = 18.sp,fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
            Text (
                text = mushroom.commentary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
        Row(modifier = Modifier.padding(3.dp)) {
            Text(text = stringResource(R.string.description)+": ",
                fontSize = 20.sp, fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
            Text(text = mushroom.description, fontSize = 20.sp,fontFamily = interFamily)
        }

        Row (modifier = Modifier.padding(3.dp)){
            Text(text = stringResource(R.string.habitat)+": ",
                fontSize = 20.sp, fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
            Text(text = mushroom.habitat, fontSize = 20.sp,fontFamily = interFamily)
        }

        Row(modifier = Modifier.padding(3.dp)) {
            Text(text = stringResource(R.string.edible)+": ",
                fontSize = 20.sp, fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
            Text(text = isEdibleText, fontSize = 20.sp,fontFamily = interFamily)
        }


        Row(modifier = Modifier.padding(3.dp)) {
            Text(text = stringResource(R.string.seasons)+": ",
                fontSize = 20.sp,fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
            Text(text = mushroom.seasons, fontSize = 20.sp, fontFamily = interFamily)
        }
        Row(modifier = Modifier.padding(3.dp)) {
            Text(
                text = stringResource(R.string.date_of_recolection)+": ",
                fontSize = 18.sp,fontFamily = interFamily, fontWeight = FontWeight.SemiBold
            )
            Text(
                text = mushroom.timestamp.toDate().toString(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }

    }
}

@Composable
fun RestaurantMap(restaurants: Restaurants) {
    Text(text = restaurants.nom, fontWeight = FontWeight.Black, fontFamily = interFamily, fontSize = 30.sp)
    AsyncImage(
        modifier = Modifier.size(200.dp), model = restaurants.photo, contentDescription = null
    )
    Spacer(modifier = Modifier.height(16.dp))
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