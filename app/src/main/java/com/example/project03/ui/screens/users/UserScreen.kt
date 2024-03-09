package com.example.project03.ui.screens.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.model.User
import com.example.project03.ui.components.LabeledIconRow
import com.example.project03.ui.components.Loading
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.theme.interFamily
import com.example.project03.viewmodel.loginScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun MyUserDetailsScreen(navController: NavController) {
    val isHome = false
    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController, title = stringResource(R.string.user))
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
                .padding(15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.account_details),
                fontFamily = interFamily
            )

            Spacer(modifier = Modifier.height(10.dp))

            ElevatedCard(
                modifier = Modifier,
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.username_text),
                        fontSize = 18.sp,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.SemiBold,
                    )
                    LabeledIconRow(
                        labelText = user!!.username
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    modifier = Modifier
                        .width(353.dp)
                        .align(Alignment.CenterHorizontally),
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.email_text),
                        fontSize = 18.sp,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.SemiBold,
                    )

                    LabeledIconRow(
                        labelText = user!!.email
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = 35.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.account_settings),
                    fontFamily = interFamily
                )

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedCard(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    EditButton(navController)
                    Divider(
                        modifier = Modifier
                            .width(353.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                    SignOffButton(navController, viewModel)
                    Divider(
                        modifier = Modifier
                            .width(353.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                    DeleteAccountButton(navController, viewModel, user!!)
                }
            }
        }

    }
}

@Composable
fun EditButton(
    navController: NavController
) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        colors = buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = {
            // Usamos myUserId para navegar a la pantalla de edición del usuario.
            // Asegúrate de que AppScreens.EditMyUserScreen.route esté definido correctamente
            // y que la pantalla de edición pueda manejar el ID del usuario en su ruta.
            navController.navigate(AppScreens.EditMyUserScreen.route)
        }
    ) {
        Text(
            text = stringResource(R.string.edit),
            fontFamily = interFamily,
            fontSize = 18.sp
        )
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
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Red,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = {
            coroutineScope.launch { loginViewModel.deleteUser(user.id) }

            loginViewModel.signOut(navController)}
    ) {
        Text(
            text = stringResource(R.string.delete_account),
            fontFamily = interFamily,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
        )
    }
}

@Composable
fun SignOffButton(navController: NavController, loginViewModel: loginScreenViewModel) {
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
            disabledContentColor = Color.White
        ),
        onClick = {
            loginViewModel.signOut(navController)
        }
    ) {
        Text(
            text = stringResource(R.string.sign_out),
            fontFamily = interFamily,
            fontSize = 18.sp
        )
    }
}
