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
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.ui.components.LabeledIconRow
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.viewmodel.loginScreenViewModel

@Composable
fun MyUserDetailsScreen(navController: NavController) {
    val isHome = false
    Scaffold(
        topBar = {
        TopAppBarWithoutScaffold(isHome, navController)
    }) { padding ->
        RecibirDatosUser(padding, navController)
    }
}

@Composable
fun RecibirDatosUser(
    paddingValues: PaddingValues,
    navController: NavController
){
    val viewModel: loginScreenViewModel = viewModel()
    val myUserId = viewModel.user.id
    val loginViewModel: loginScreenViewModel = viewModel()
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = AbsoluteAlignment.Left
    ){
        LabeledIconRow(
            labelText = viewModel.user.username
        )

        LabeledIconRow(
            labelText = viewModel.user.email
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
        EditButton(navController)
        DeleteAccountButton(navController, loginViewModel)
        SignOffButton(navController, loginViewModel)
    }
}

@Composable
fun EditButton(
    navController: NavController
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
        colors = buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary
        ),
        onClick = {
            // Usamos myUserId para navegar a la pantalla de edición del usuario.
            // Asegúrate de que AppScreens.EditMyUserScreen.route esté definido correctamente
            // y que la pantalla de edición pueda manejar el ID del usuario en su ruta.
            navController.navigate(AppScreens.EditMyUserScreen.route)
        }
    ) {
        Text(text = "Editar")
    }
}

@Composable
fun DeleteAccountButton(navController: NavController, loginViewModel: loginScreenViewModel) {
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
        onClick = { loginViewModel.deleteUser() }
    ) {
        Text(text = "Eliminar cuenta")
    }
}

@Composable
fun SignOffButton(navController: NavController, loginViewModel: loginScreenViewModel) {
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
        onClick = {
            loginViewModel.signOut()
        }
    ) {
        Text(text = "Cerrar sesión")
    }
}
