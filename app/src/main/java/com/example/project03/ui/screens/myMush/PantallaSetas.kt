package com.example.project03.ui.screens.myMush

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.model.Mushroom
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.util.db.getMushrooms
import com.example.project03.viewmodel.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PantallaSetas(navController: NavController) {
    // Contexto de la base de datos Firestore
    val db = FirebaseFirestore.getInstance()

    // Estados de la UI
    var mushroomList by remember { mutableStateOf(listOf<Mushroom>()) }
    var loading by remember { mutableStateOf(true) }

    // Efecto lanzado para cargar los datos
    LaunchedEffect(key1 = Unit) {
        mushroomList = db.getMushrooms()
        loading = false
    }

    // UI condicional basada en el estado de carga
    if (loading) {
//        LoadingState()
    } else {
        MushroomList(mushrooms = mushroomList, navController)
    }
}

@Composable
fun LoadingState() {
    CircularProgressIndicator() // Ejemplo de componente de carga, puede ser sustituido por cualquier otro
}
//val mushrooms: MutableList<Mushroom> = mutableListOf()

/*@Composable
fun MushroomList() {
    val db = Firebase.firestore
    val mushroomRef = db.collection("setas")

    mushroomRef.get().addOnSuccessListener { documents ->
        for (document in documents) {
            val mushroomData = document.toObject(Mushroom::class.java)
            if (mushroomData != null) {
                mushrooms.add(mushroomData)

            }
        }
    }.addOnFailureListener { exception ->
        println("Error getting mushrooms from Firebase: ${exception.message}")
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarDatosScreen(navController: NavController, text: String?) {
    val mainViewModel: MainViewModel = viewModel()
    var isHome by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        })
    { padding ->
        Column (Modifier.padding(padding)) {
            PantallaSetas(navController)
        }
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mainViewModel.showBottomSheet = false }
            ) {
                ContentBottomSheet(mainViewModel)
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MushroomList(mushrooms: List<Mushroom>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(mushrooms) { mushroom ->
            ElevatedCard(
                modifier = Modifier
                    .padding(2.dp)
                    .clickable { navController.navigate("detail_screen") }, // Asumiendo que cada seta tiene un ID único
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 26.dp)
                    ) {
                        AsyncImage(
                            model = mushroom.photo,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
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
                    }
                }
            }
        }
    }
}
