package com.example.project03.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Dining
import androidx.compose.material.icons.rounded.Diversity3
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ItemsBottomSheet (
    val title: String,
    val icon: ImageVector
){
    object ItemMenu01: ItemsBottomSheet(
        "Aprender",
        Icons.Rounded.Category
    )

    object ItemMenu02: ItemsBottomSheet(
        "Comer",
        Icons.Rounded.Dining
    )

    object ItemMenu03: ItemsBottomSheet(
        "Comunidad",
        Icons.Rounded.Diversity3
    )

}