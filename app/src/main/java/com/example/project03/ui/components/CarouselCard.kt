package com.example.project03.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.project03.R
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.util.data.Data
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun CarouselCard(navController: NavController) {
    val mushroomList = Data.myMushDBList()
    Modifier.padding(8.dp)
    val pagerState = rememberPagerState(initialPage = 1)
    val addMushImage = R.drawable.add_squared
    if (mushroomList.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .aspectRatio(1.4f)
        ) {

            Text(
                text = stringResource(R.string.carrouselMyMush),
                Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineMedium
            )
            ElevatedCard(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(999.dp),
                colors = CardDefaults.elevatedCardColors(Color.Transparent),
                onClick = { navController.navigate(route = AppScreens.AddMushroomScreen.route) }) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        Modifier.align(
                            Alignment.CenterHorizontally,
                        )
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "No hay setas añadidas",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    AsyncImage(

                        modifier = Modifier.size(130.dp),
                        model = addMushImage,
                        contentDescription = "photo of a mushroom",
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.placeholder),
                        error = painterResource(id = R.drawable.error),
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Añade una seta",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.inverseSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .aspectRatio(1.5f)
        ) {

            Text(
                text = stringResource(R.string.carrouselMyMush),
                Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalPager(
                count = mushroomList.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 50.dp),
                modifier = Modifier.height(800.dp),
            ) { page ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(999.dp),
                    colors = CardDefaults.elevatedCardColors(Color.Transparent)

                ) {
                    Box(contentAlignment = Alignment.BottomCenter) {
                        AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                            .data(mushroomList.toList()[page].photo).crossfade(true)
                            .scale(Scale.FILL).build(),
                            contentDescription = "photo of a mushroom",
                            contentScale = ContentScale.FillBounds,

                            placeholder = painterResource(id = R.drawable.placeholder),
                            error = painterResource(id = R.drawable.error),
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { navController.navigate(route = AppScreens.SetasDetailsScreen.route + "/${mushroomList.toList()[page].commonName}") })
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .shadow(
                                        elevation = 99.dp, shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(8.dp),
                                text = mushroomList.toList()[page].commonName,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}