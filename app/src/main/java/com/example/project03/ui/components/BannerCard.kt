package com.example.project03.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.project03.R
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.util.data.Data
import java.time.LocalDate
import kotlin.random.Random


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BannerCard(navController: NavController) {
    val wikiList = Data.wikiDBList()
    if (wikiList.isNotEmpty()) {
        // Obtiene la fecha actual como una cadena
        val currentDate = LocalDate.now().toString()
        // Usa la fecha actual como semilla para el generador de números aleatorios
        val randomSeed = currentDate.hashCode()
        val random = remember { Random(randomSeed) }
        // Selecciona una seta aleatoria basada en la semilla
        val mushroom by remember(currentDate) { mutableStateOf(wikiList.random(random)) }
        ElevatedCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .clickable {
                    navController.navigate(
                        route = AppScreens.SetasDetailsScreen.route + "/${mushroom.commonName}"
                    )
                },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current).data(mushroom.photo)
                    .crossfade(true).build(),
                    contentDescription = "photo of a mushroom",
                    contentScale = ContentScale.FillWidth,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.error)
                )

                Box(modifier = Modifier.padding(bottom = 12.dp, end = 12.dp)) {
                    OutLineText(
                        text = mushroom.commonName
                    )
                }
            }
        }
    }
}

