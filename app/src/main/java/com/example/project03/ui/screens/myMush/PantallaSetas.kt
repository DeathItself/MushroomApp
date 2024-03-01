package com.example.project03.ui.screens.myMush

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.model.MyMushroom
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.util.data.Data
import com.example.project03.util.db.deleteMushroom
import com.example.project03.viewmodel.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PantallaSetas(navController: NavController) {
    MyMushroomList(mushrooms = Data.myMushDBList(), navController)
}

@Composable
fun EmptyState() {
    Column {
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "No hay setas",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        ) // Ejemplo de componente de carga, puede ser sustituido por cualquier otro
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarMisSetasScreen(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = false
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(isHome, navController, title = "Mis Setas")
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        Column(Modifier.padding(padding)) {
            PantallaSetas(navController)
        }
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMushroomList(mushrooms: List<MyMushroom>, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (mushrooms.isEmpty()) {
            item {
                EmptyState()
            }
        }
        items(mushrooms) { mushroom ->
            ElevatedCard(
                modifier = Modifier
                    .padding(2.dp)
                    .clickable {
                        navController.navigate(
                            AppScreens.MisSetasDetailsScreen.route + "/" + mushroom.myMushID
                        )
                    }, elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 26.dp, end = 8.dp)
                            .fillMaxWidth()

                    ) {
                        AsyncImage(
                            model = mushroom.photo,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.width(190.dp)) {
                            Text(
                                text = mushroom.commonName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = mushroom.scientificName,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .padding(end = 10.dp)
                            )

                        }
                        Column(
                            horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxHeight()
                        ) {
                            Icon(
                                imageVector = if (mushroom.isEdible == true) Icons.Filled.Dining else Icons.Filled.Dining,
                                contentDescription = if (mushroom.isEdible == true) "Comestible" else "No comestible",
                                tint = if (mushroom.isEdible == true) Color.Green else Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.End
                        ) {


                            Column {
                                Button(
                                    onClick = {
                                        navController.navigate(AppScreens.EditMyMushroomScreen.route + "/" + mushroom.myMushID)
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "edit",
                                        tint = MaterialTheme.colorScheme.inverseSurface,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                                Button(colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    contentPadding = PaddingValues(0.dp),
                                    onClick = {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            FirebaseFirestore.getInstance().deleteMushroom(
                                                mushroom.commonName, mushroom.description
                                            )
                                        }
                                    }) {
                                    Icon(
                                        imageVector = Icons.Filled.DeleteForever,
                                        contentDescription = "delete",
                                        tint = MaterialTheme.colorScheme.inverseSurface,
                                    )

                                }
                            }

                        }
                    }
                }
            }
        }
    }
}