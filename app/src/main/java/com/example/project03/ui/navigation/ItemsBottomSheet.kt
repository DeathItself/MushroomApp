package com.example.project03.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Dining
import androidx.compose.material.icons.rounded.Diversity3
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.project03.R

sealed class ItemsBottomSheet(
    val title: String,
    @StringRes val titleRes: Int, // Almacena el ID del recurso
    val icon: ImageVector
) {
    object ItemMenu01 : ItemsBottomSheet("Learn",R.string.BottomSheetMenuLearn, Icons.Rounded.Category)
    object ItemMenu02 : ItemsBottomSheet("Eat",R.string.BottomSheetMenuEat, Icons.Rounded.Dining)
    object ItemMenu03 : ItemsBottomSheet("Community",R.string.BottomSheetMenuCommunity, Icons.Rounded.Diversity3)
    object ItemMenu04 : ItemsBottomSheet("Wiki",R.string.BottomSheetMenuWiki, Icons.Rounded.AutoStories)

    object ItemMenu05 : ItemsBottomSheet("Weather", R.string.BottomSheetMenuWeather, Icons.Rounded.Cloud)

}