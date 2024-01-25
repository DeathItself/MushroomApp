package com.example.project03

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Dining
import androidx.compose.material.icons.rounded.Diversity3
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Items_BottomSheet (
    val title: String,
    val icon: ImageVector
){
    object Item_menu01: Items_BottomSheet(
        "Aprender",
        Icons.Rounded.Category
    )

    object Item_menu02: Items_BottomSheet(
        "Comer",
        Icons.Rounded.Dining
    )

    object Item_menu03: Items_BottomSheet(
        "Comunidad",
        Icons.Rounded.Diversity3
    )

}