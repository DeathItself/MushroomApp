package com.example.project03.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Dining
import androidx.compose.material.icons.rounded.Diversity3
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.project03.R

data class ItemsBottomSheet(
    val title: String,
    @StringRes val titleRes: Int, // Almacena el ID del recurso
    val icon: ImageVector
)

object Menu {
    val itemsMenu = listOf(
        ItemsBottomSheet("Learn",R.string.BottomSheetMenuLearn, Icons.Rounded.Category),
        ItemsBottomSheet("Eat",R.string.BottomSheetMenuEat, Icons.Rounded.Dining),
        ItemsBottomSheet("Forum",R.string.BottomSheetMenuForum, Icons.Rounded.Diversity3),
        ItemsBottomSheet("Wiki",R.string.BottomSheetMenuWiki, Icons.Rounded.AutoStories),
        ItemsBottomSheet("Ranking",R.string.BottomSheetMenuRank,Icons.Rounded.FilterList),
        ItemsBottomSheet("Weather", R.string.BottomSheetMenuWeather, Icons.Rounded.Cloud)
    )
}