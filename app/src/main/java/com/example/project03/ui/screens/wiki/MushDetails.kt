package com.example.project03.ui.screens.wiki

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.R
import com.example.project03.model.Mushroom
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.ui.theme.interFamily
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
fun RecibirDatosSeta(padding: PaddingValues, s: String) {

    val mushObj = Data.wikiDBList().find { it.commonName == s }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (mushObj != null) {
            val isEdibleText = when (mushObj.isEdible) {
                true -> stringResource(R.string.edible)
                false -> stringResource(R.string.not_edible)
                else -> stringResource(R.string.unknown)
            }
            Text(
                text = mushObj.commonName,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = interFamily
            )
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(999.dp),
                colors = CardDefaults.elevatedCardColors(Color.Transparent)

            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ){
                    AsyncImage(
                        modifier = Modifier
                            .width(380.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        model = mushObj.photo,
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row (modifier = Modifier.padding(3.dp)){
                    Text(text = "Nombre científico: ", fontSize = 18.sp,fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = mushObj.scientificName,
                        fontSize = 18.sp,
                        fontFamily = interFamily,
                        fontStyle = FontStyle.Italic
                    )
                }

                Row(modifier = Modifier.padding(3.dp)) {
                    Text(text = "Descripcion: ",fontSize = 18.sp, fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
                    Text(text = mushObj.description, fontSize = 18.sp,fontFamily = interFamily,)
                }

                Row (modifier = Modifier.padding(3.dp)){
                    Text(text = "Hábitat: ",fontSize = 18.sp, fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
                    Text(text = mushObj.habitat, fontSize = 18.sp,fontFamily = interFamily)
                }
                Row(modifier = Modifier.padding(3.dp)) {
                    Text(text = "Estado: ",fontSize = 18.sp, fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
                    Text(text = isEdibleText, fontSize = 18.sp,fontFamily = interFamily)
                }

                Row(modifier = Modifier.padding(3.dp)) {
                    Text(text = "Estación: ", fontSize = 18.sp,fontFamily = interFamily, fontWeight = FontWeight.SemiBold)
                    Text(text = mushObj.seasons, fontSize = 18.sp, fontFamily = interFamily)
                }
            }
            //mostramos la ubicacion de la seta en un mapa pequeño debajo de la descripcion
            Mapa(mushObj)
        }
    }


}

@Composable
fun Mapa(mushroom: Mushroom?) {
    // Minimapa
    val cameraPositionState = rememberCameraPositionState {
        if (mushroom != null) {
            position = CameraPosition.fromLatLngZoom(
                LatLng(mushroom.latitude, mushroom.longitude), 15f
            )
        }
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
        val edible = if (mushroom?.isEdible == true) stringResource(R.string.edible) else stringResource(R.string.not_edible)
        if (mushroom != null) {
            Marker(
                state = MarkerState(position = LatLng(mushroom.latitude, mushroom.longitude)),
                title = mushroom.commonName,
                snippet = edible
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MushroomDetailsScreen(navController: NavController, s: String) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = false
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(isHome, navController, title = "Wiki")
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        RecibirDatosSeta(padding = padding, s)
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}
