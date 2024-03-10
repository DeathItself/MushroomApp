package com.example.project03.ui.screens.myMush

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.R
import com.example.project03.model.MyMushroom
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.ui.theme.interFamily
import com.example.project03.util.data.Data
import com.example.project03.util.db.deleteMushroom
import com.example.project03.viewmodel.MainViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecibirDatosSeta(padding: PaddingValues, myMushID: String, navController: NavController) {
    val mushObj = Data.myMushDBList().find { it.myMushID == myMushID }
    val wikimush = Data.wikiDBList().find { it.commonName == mushObj?.commonName }
    val wikimushImg = wikimush?.photo
    val pagerState = androidx.compose.foundation.pager.rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = -0.3f, pageCount = { 2 }
    )

    LazyColumn(modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .scrollable(orientation = Orientation.Vertical,
            enabled = true,
            reverseDirection = false,
            state = ScrollableState { delta -> delta }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            if (mushObj != null) {
                val isEdibleText = when (mushObj.isEdible) {
                    true -> "Comestible"
                    false -> "No comestible"
                    else -> "Información no disponible"
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(15.dp),
                        text = mushObj.commonName,
                        fontFamily = interFamily,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    //Carrusel de imagen
                    HorizontalPager(
                        state = pagerState
                    ) {page ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxSize(0.7f)
                                    .padding(horizontal = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(999.dp),
                                colors = CardDefaults.elevatedCardColors(Color.Transparent)
                            ) {
                                Box(
                                    contentAlignment = Alignment.BottomCenter
                                ){
                                    AsyncImage(
                                        modifier = Modifier.size(280.dp),
                                        model = if (page == 0) mushObj.photo else wikimushImg,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.padding(12.dp),
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
                        //TODO
                        Text(
                            text = mushObj.timestamp.toDate().toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.padding(horizontal = 8.dp), onClick = {
                                // Navega a la pantalla de edición con los datos de la seta
                                navController.navigate(AppScreens.EditMyMushroomScreen.route + "/" + mushObj.myMushID)
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Icon(
                                Icons.Filled.Edit, contentDescription = "Editar", tint = Color.White
                            )
                            Text(stringResource(R.string.edit), color = Color.White, fontFamily = interFamily)
                        }

                        Button(
                            onClick = {
                                // Llama a la función para borrar la seta
                                CoroutineScope(Dispatchers.IO).launch {
                                    FirebaseFirestore.getInstance()
                                        .deleteMushroom(mushObj.commonName, mushObj.description)
                                }
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Icon(
                                Icons.Filled.DeleteForever,
                                contentDescription = "Borrar",
                                tint = Color.White
                            )
                            Text("Borrar", color = Color.White, fontFamily = interFamily)
                        }
                    }
                    //mostramos la ubicacion de la seta en un mapa pequeño debajo de la descripcion
                    if (mushObj.latitude != null && mushObj.longitude != null) Mapa(mushObj)
                }
            }
        }
    }
}

@Composable
fun Mapa(mushroom: MyMushroom) {
    // Minimapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(mushroom.latitude!!, mushroom.longitude!!), 15f
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
        Marker(
            state = MarkerState(position = LatLng(mushroom.latitude!!, mushroom.longitude!!)),
            title = mushroom.commonName,
            snippet = mushroom.scientificName
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMushroomDetailsScreen(navController: NavController, myMushID: String) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = false
    Scaffold(
        topBar = { TopAppBarWithoutScaffold(isHome, navController, title = stringResource(R.string.my_mushs)) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        RecibirDatosSeta(padding = padding, myMushID, navController)
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}
