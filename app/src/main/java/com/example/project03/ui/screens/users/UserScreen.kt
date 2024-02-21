package com.example.project03.ui.screens.users

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.model.User
import com.example.project03.ui.components.LabeledIconRow
import com.example.project03.ui.components.Loading
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.viewmodel.loginScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

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
) {

    val viewModel: loginScreenViewModel = viewModel()
    val auth = Firebase.auth
    var loading by remember { mutableStateOf(true) }
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(true) {
        user = viewModel.loadUserData(auth.currentUser!!.uid)
        loading = false
    }
    if (loading) {
        Loading.LoadingState()
    } else {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Nombre de usuario",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            LabeledIconRow(
                labelText = user!!.username
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier.padding(10.dp),
                text = "Correo electrónico",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )

            LabeledIconRow(
                labelText = user!!.email
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
            DeleteAccountButton(navController, viewModel, user!!)
            SignOffButton(navController, viewModel)
        }
    }
}

@Composable
fun EditButton(
    navController: NavController
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
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
fun DeleteAccountButton(navController: NavController, loginViewModel: loginScreenViewModel, user: User) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
            disabledContentColor = Color.White
        ),

        onClick = {
            coroutineScope.launch { loginViewModel.deleteUser(user.id) }

            loginViewModel.signOut(navController)}
    ) {
        Text(text = "Eliminar cuenta")
    }
}

@Composable
fun SignOffButton(navController: NavController, loginViewModel: loginScreenViewModel) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp),
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
            loginViewModel.signOut(navController)
        }
    ) {
        Text(text = "Cerrar sesión")
    }
}
