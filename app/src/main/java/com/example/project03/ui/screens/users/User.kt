package com.example.project03.ui.screens.users

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project03.ui.components.EditableUsernameField
import com.example.project03.ui.components.LabeledIconRow
import com.example.project03.ui.components.TopAppBarWithoutScaffold

@Composable
fun UserScreen(
    navController: NavController
){
    val isHome = false
    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController)
        }
    ){padding ->
        ContentUserScreen(padding)
    }
}


@Composable
fun ContentUserScreen(
    paddingValues: PaddingValues,
){
    var username by remember { mutableStateOf("Usuario") }
    var editedUsername by remember { mutableStateOf(username) }
    var isEditingUsername by remember { mutableStateOf(false) }


    var email by remember { mutableStateOf("email@example.com") }
    var editedEmail by remember { mutableStateOf(email) }
    var isEditingEmail by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(
                horizontal = 12.dp,
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = AbsoluteAlignment.Left

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mis Datos",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 26.sp,
            )
        }
        
        Spacer(modifier = Modifier.height(15.dp))

        if(isEditingUsername){
            EditableUsernameField(
                username = editedUsername,
                onUsernameChange = { editedUsername = it },
                onSave = {
                    username = editedUsername
                    isEditingUsername = false
                },
                onCancel = { isEditingUsername = false }
            )
        }else{
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "Nombre de usuario"
            )
            Spacer(modifier = Modifier.height(8.dp))
            LabeledIconRow(
                labelText = "$username",
                onClick = { isEditingUsername = true }
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        if(isEditingEmail){
            EditableUsernameField(
                username = editedEmail,
                onUsernameChange = { editedEmail = it },
                onSave = {
                    email = editedEmail
                    isEditingEmail = false
                },
                onCancel = { isEditingEmail = false }
            )
        }else{
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "Correo electrónico"
            )
            Spacer(modifier = Modifier.height(8.dp))
            LabeledIconRow(
                labelText = "$email",
                onClick = { isEditingEmail = true }
            )
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(
                horizontal = 12.dp,
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DeleteAccountButton()
        SignOffButton()
    }
}

@Composable
fun DeleteAccountButton(){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
        ,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
            disabledContentColor = Color.White
        ),
        onClick = { /*TODO*/ }
    ) {
        Text(text = "Eliminar cuenta")
    }
}

@Composable
fun SignOffButton(){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
        ,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Red,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
            disabledContentColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Red
        ),
        onClick = { /*TODO*/ }
    ) {
        Text(text = "Cerrar sesión")
    }
}
