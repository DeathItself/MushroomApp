package com.example.project03.ui.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.project03.R
import com.example.project03.model.ImagePath
import com.example.project03.model.Mushroom
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.screens.maps.permissionGranted
import com.example.project03.util.data.Data
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun AddCard(navController: NavController) {
    //PONER EL PATH VIEW MODEL, REVISAR EL CAHTGPT
    val mushroom = Data.wikiDBList()
    val imagePath = ImagePath.imagePath
    var nameMushroom = ""
    var description = ""
    val context = LocalContext.current
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    ElevatedCard(
        modifier = Modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(235, 235, 227),
            contentColor = Color.Black,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(1.dp),

        ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Seta Encontrada!", style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (permissionGranted) {
                        navController.navigate(route = AppScreens.CameraScreen.route)
                    } else {
                        navController.navigate(route = AppScreens.CamLocationScreen.route)
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ), shape = RoundedCornerShape(12.dp)

            ) {
                if (imagePath.isNotEmpty()) {
                    Image(
                        modifier = Modifier.size(width = 300.dp, height = 100.dp),
                        painter = rememberAsyncImagePainter(imagePath),
                        contentDescription = "Añadir imagen seta"
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = "Añadir imagen seta"
                    )
                }
            }
        }


        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            nameMushroom = categoryList(mushroom)

            Spacer(modifier = Modifier.height(10.dp))
            description = textField()
            LaunchedEffect(key1 = true) {
                val (lat, lon) = getMyLocation(context)
                if (lat != null) {
                    latitude = lat
                }
                if (lon != null) {
                    longitude = lon
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    // Aqui se guarda la informacion recogida en CategoryList, TextField y la imagen en la base de datos
                    CoroutineScope(Dispatchers.IO).launch {
                        Data.addMushroom(
                            nameMushroom, description, imagePath, mushroom, latitude, longitude
                        )
                    }
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color(126, 116, 116), contentColor = Color.Black
                )
            ) {
                Text(
                    "Guardar", color = Color.White
                )
            }
        }
    }

}

@SuppressLint("MissingPermission")
suspend fun getMyLocation(context: Context): Pair<Double?, Double?> {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    return try {
        val location = fusedLocationClient.lastLocation.await()
        Pair(location.latitude, location.longitude)
    } catch (e: Exception) {
        e.printStackTrace()
        Pair(null, null) // Retorna un valor por defecto o maneja el error adecuadamente
    }
}

//@Preview
@Composable
fun textField(): String {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    TextField(
        modifier = Modifier,
        value = text,
        onValueChange = { text = it },
        enabled = true,
        readOnly = false,
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            Text(text = "Escribe un comentario")
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedPlaceholderColor = Color.Black,
            unfocusedIndicatorColor = Color(126, 116, 116),
            focusedIndicatorColor = Color(126, 116, 116)

        )
    )
    return text.text
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun categoryList(mushroom: List<Mushroom>): String {
    val options = mushroom.indices.map { mushroom[it].commonName } + "Otro"
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text("Tipo de seta") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedPlaceholderColor = Color.Black,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color(126, 116, 116),
                focusedIndicatorColor = Color(126, 116, 116)


            )
        )

        ExposedDropdownMenu(
            modifier = Modifier,
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(modifier = Modifier, text = { Text(selectionOption) }, onClick = {
                    selectedOptionText = selectionOption
                    expanded = false
                }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
    return selectedOptionText
}