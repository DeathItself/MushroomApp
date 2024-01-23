package com.example.project03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.google.gson.Gson
import java.io.File
import java.net.URI
data class Mushroom(
    val commonName: String,
    val scientificName: String,
    val imageResourceId: Int
)

val mushrooms = listOf(
    Mushroom("Chanterelle", "Cantharellus cibarius", R.drawable.fly_agaric_mushroom),
    Mushroom("Fly Agaric", "Amanita muscaria", R.drawable.fresh_enoki_mushroom_500x500),
    Mushroom("Morel", "Morchella", R.drawable.chant_3),
    Mushroom("Lion's Mane", "Hericium erinaceus", R.drawable.lions_mane_2),
    Mushroom("Shiitake", "Lentinula edodes", R.drawable.shiitake_cultivo),
    Mushroom("Porcini", "Boletus edulis", R.drawable._200px_pleurotus_ostreatus_jpg7),
    Mushroom("Oyster Mushroom", "Pleurotus ostreatus", R.drawable.descarga),
    Mushroom("Reishi", "Ganoderma lucidum", R.drawable.hongo_inmortalidad_anticancerigeno_que_reduce_estres_alarga_vida_98),
    Mushroom("Enoki Mushroom", "Flammulina velutipes", R.drawable.istockphoto_505505411_612x612)
)

@Composable
fun MushroomList(mushrooms: List<Mushroom>) {
    LazyColumn {
        items(mushrooms) { mushroom ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = mushroom.imageResourceId),
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
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MushroomList(mushrooms)
        }
    }
}