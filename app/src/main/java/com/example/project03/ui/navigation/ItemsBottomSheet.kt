package com.example.project03.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Dining
import androidx.compose.material.icons.rounded.Diversity3
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.project03.R

sealed class ItemsBottomSheet(
    @StringRes val titleRes: Int, // Almacena el ID del recurso
    val icon: ImageVector
) {
    object ItemMenu01 : ItemsBottomSheet(R.string.BottomSheetMenuLearn, Icons.Rounded.Category)
    object ItemMenu02 : ItemsBottomSheet(R.string.BottomSheetMenuEat, Icons.Rounded.Dining)
    object ItemMenu03 : ItemsBottomSheet(R.string.BottomSheetMenuComunity, Icons.Rounded.Diversity3)

}