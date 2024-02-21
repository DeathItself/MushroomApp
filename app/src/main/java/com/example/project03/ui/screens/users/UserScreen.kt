package com.example.project03.ui.screens.users

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project03.ui.components.LabeledIconRow
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens

@Composable
fun MyUserDetailsScreen(navController: NavController, myUserId: String) {
    val isHome = false
    Scaffold(
        topBar = {
        TopAppBarWithoutScaffold(isHome, navController)
    }) { padding ->
        RecibirDatosUser(padding, navController, myUserId)
    }
}

@Composable
fun RecibirDatosUser(
    paddingValues: PaddingValues,
    navController: NavController,
    myUserId: String
){
    val UserObj = ""

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = AbsoluteAlignment.Left
    ){
        LabeledIconRow(
            labelText = "username",
        )

        LabeledIconRow(
            labelText = "email",
        )

        LabeledIconRow(
            labelText = "pasword",
        )

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
        EditButton(navController, UserObj)
        DeleteAccountButton()
        SignOffButton()
    }
}

@Composable
fun EditButton(
    navController: NavController,
    userObj: String
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
        ,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Blue,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
            disabledContentColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Blue
        ),
        onClick = { navController.navigate(AppScreens.EditMyUserScreen.route + "/" + userObj)}
    ) {
        Text(text = "Editar")
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
