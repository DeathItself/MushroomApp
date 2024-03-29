package com.example.project03.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project03.viewmodel.MainViewModel

@Composable
fun ContentBottomSheet(
    mainViewModel: MainViewModel,
    navController: NavController
) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .height(280.dp)
            .padding(
                horizontal = 28.dp,

                )
    ){
        Menu.itemsMenu.forEach{ item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate("item_menu/"+item.title) }
            ){
                // Usar stringResource para obtener el string del ID del recurso
                val titleString = stringResource(id = item.titleRes)
                Icon(item.icon, contentDescription = titleString)
                Spacer(
                    modifier = Modifier
                        .width(24.dp)
                )
                Text(
                    text = titleString,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

    }
}