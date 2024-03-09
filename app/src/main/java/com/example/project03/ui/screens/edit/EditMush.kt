package com.example.project03.ui.screens.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.model.MyMushroom
import com.example.project03.ui.components.Loading
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMyMushroomScreen(navController: NavController, myMushID: String) {
    val mainViewModel: MainViewModel = viewModel()
    val isHome = false
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(isHome, navController, title = stringResource(R.string.edit_mushroom))
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        EditMyMushroom(navController, myMushID, padding)
        //submenu
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}

//***
@Composable
fun EditMyMushroom(navController: NavController, myMushID: String, padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    // Carga los datos del hongo por ID y actualiza el estado
    var myMushroom by remember { mutableStateOf(MyMushroom()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = myMushID) {
        val docRef = db.collection("my_mushrooms").document(myMushID)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            myMushroom = documentSnapshot.toObject(MyMushroom::class.java) ?: MyMushroom()
            isLoading = false
        }
    }

    if (isLoading) {
        Loading.LoadingState() // Muestra un indicador de carga mientras se cargan los datos
    } else {
        // Asegúrate de que la UI para editar el hongo se muestra después de cargar los datos
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            EditMyMushroomForm(myMushroom = myMushroom, onSave = { updatedMushroom ->
                coroutineScope.launch {
                    db.collection("my_mushrooms").document(myMushID).set(updatedMushroom)
                    navController.popBackStack()
                }
            }, onCancel = {
                navController.popBackStack()
            }, padding)
        }
    }
}

@Composable
fun EditMyMushroomForm(
    myMushroom: MyMushroom,
    onSave: (MyMushroom) -> Unit,
    onCancel: () -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Utiliza myMushroom para inicializar los estados de los campos de texto
        var commentary by remember { mutableStateOf(myMushroom.commentary) }
        var commonName by remember { mutableStateOf(myMushroom.commonName) }
        var description by remember { mutableStateOf(myMushroom.description) }
        var habitat by remember { mutableStateOf(myMushroom.habitat) }
        var isEdible by remember { mutableStateOf(myMushroom.isEdible) }
        var seasons by remember { mutableStateOf(myMushroom.seasons) }

        OutlinedTextField(value = commonName,
            onValueChange = { commonName = it },
            label = { Text(stringResource(R.string.common_name)) })
        OutlinedTextField(
            value = commentary,
            onValueChange = { commentary = it },
            label = { Text(stringResource(R.string.commentary)) }
        )
        OutlinedTextField(value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) })
        OutlinedTextField(value = habitat,
            onValueChange = { habitat = it },
            label = { Text(stringResource(R.string.habitat)) })
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.edible))
            Switch(checked = isEdible ?: false, onCheckedChange = { isEdible = it })
        }
        OutlinedTextField(value = seasons,
            onValueChange = { seasons = it },
            label = { Text(stringResource(R.string.seasons)) })

        Row {
            Button(onClick = {
                // Actualiza myMushroom con los nuevos valores
                myMushroom.commentary = commentary
                myMushroom.commonName = commonName
                myMushroom.description = description
                myMushroom.habitat = habitat
                myMushroom.isEdible = isEdible
                myMushroom.seasons = seasons

                onSave(myMushroom)
            }) {
                Text(stringResource(R.string.save))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
        }
//        dev info about the mushroom
        Text(
            text = "Mush DevInfo:\n"
                    + "${myMushroom.myMushID}, "
                    + "${myMushroom.latitude}, "
                    + "${myMushroom.longitude},"
                    + "${myMushroom.timestamp.toDate()}"
        )
    }
}

