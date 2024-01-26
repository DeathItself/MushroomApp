package com.example.project03.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.project03.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CarouselCard(modifier: Modifier = Modifier) {
    Modifier.padding(8.dp)
    val pagerState = rememberPagerState(initialPage = 1)
    val sliderMap = mapOf(
        "Chanterelle" to "https://www.wildfooduk.com/wp-content/uploads/2018/01/Chant-3.jpg",
        "Fly Agaric" to "https://myceliuminspired.com/wp-content/uploads/2023/01/fly_agaric_mushroom-1024x683.png",
        "Morel" to "https://www.allrecipes.com/thmb/efTTisfeqU5eUonXlCp51rV6FMw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Morel-Mushrooms-by-Kevin-Miyazaki-2000-d596adcb2dfa44e6859811fbaa1f3c15.jpg"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .aspectRatio(1.8f)
    ) {

        Text(
            text = "Mis setas",
            Modifier.padding(8.dp),
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )

        HorizontalPager(
            count = sliderMap.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 50.dp),
            modifier = Modifier.height(800.dp),
        ) { page ->
            ElevatedCard(
                elevation = CardDefaults.cardElevation(8.dp)
                , colors = CardDefaults.elevatedCardColors(Color.Transparent)

            ) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(sliderMap.values.toList()[page])
                            .crossfade(true).scale(Scale.FIT)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.placeholder),
                        error = painterResource(id = R.drawable.error)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp),
                            text = sliderMap.keys.toList()[page],
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