package com.example.project03.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(mushroom.photo)
                    .crossfade(true).build(),
                    contentDescription = "photo of a mushroom",
                    contentScale = ContentScale.FillWidth,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(
                                route = AppScreens.SetasDetailsScreen.route + "/${mushroom.commonName}"
                            )
                        })

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ) {
                    Text(
                        text = "Seta del dia",
                        modifier = Modifier
                            .shadow(
                                elevation = 20.dp, shape = RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 40.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )

                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(30.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .shadow(
                                elevation = 20.dp, shape = RoundedCornerShape(20.dp)
                            )
                            .padding(8.dp),
                        text = mushroom.commonName,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
