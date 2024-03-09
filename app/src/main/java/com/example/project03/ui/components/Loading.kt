package com.example.project03.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Loading {
    companion object {
        @JvmStatic
        @Composable
        fun LoadingState() {
            Column(
                Modifier
                    .size(50.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .size(50.dp),
                    color = MaterialTheme.colorScheme.inverseSurface,
                    strokeWidth = 5.dp
                )
            }
        }
    }
}