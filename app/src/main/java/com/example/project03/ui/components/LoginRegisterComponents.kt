package com.example.project03.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextFields(
    label: String,
    valueState: MutableState<String>,
) {
    OutlinedTextField(
        modifier = Modifier,
        value = valueState.value,
        onValueChange = {
            valueState.value = it
        },
        label = { Text(label) },
        enabled = true,
        readOnly = false,
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            // Customize colors here
        )
    )
}
@Composable
fun UIBottom(
    textId: String,
    enabled: Boolean,
    onclick: () -> Unit
){
    OutlinedButton(
        onClick = {
           onclick()
        },
        modifier = Modifier,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(textId)
    }
}

@Composable
fun LabeledIconRow(
    labelText: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(250.dp)
            .padding(all = 18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(labelText)
    }
    Spacer(modifier = Modifier.height(10.dp))
}
