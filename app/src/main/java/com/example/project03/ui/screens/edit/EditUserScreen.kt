package com.example.project03.ui.screens.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.model.User
import com.example.project03.ui.components.Loading
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.theme.interFamily
import com.example.project03.viewmodel.loginScreenViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun EditMyUserScreen(
    navController: NavController,

){
    val isHome = false
    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController, title = "Editar usuario")
        }) { padding ->
        EditMyUser(padding, navController)
    }
}

@Composable
fun EditMyUser(
    paddingValues: PaddingValues,
    navController: NavController
){
    val viewModel: loginScreenViewModel = viewModel()
    val auth = Firebase.auth
    var loading by remember { mutableStateOf(true) }
    var user by remember { mutableStateOf<User?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(true) {
        user = viewModel.loadUserData(auth.currentUser!!.uid)
        loading = false
    }
    if (loading) {
        Loading.LoadingState()
    } else {

        EditMyUserForm(
            user = user!!,
            onSave = {
                updateUser ->
                coroutineScope.launch {
                    db.collection("users").document(user!!.id).set(updateUser)
                    navController.popBackStack()
                }
            } ,
            onCancel = {
                navController.popBackStack()
            },
            paddingValues
        )
    }
}

@Composable
fun EditMyUserForm(
    user: User,
    onSave: (User) -> Unit,
    onCancel: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var username by remember { mutableStateOf(user.username) }
        var email by remember { mutableStateOf(user.email) }
        //password

        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Nombre de usuario",
                fontSize = 18.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { "username" },
                shape = RoundedCornerShape(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Correo electrónico",
                fontSize = 18.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { "email" },
                shape = RoundedCornerShape(10.dp)
            )
        }

        //password
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            //modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Button(
                onClick = {
                    user.username = username
                    user.email = email
                    onSave(user)
                }
            ){
                Text(text = "Guardar", fontFamily = interFamily)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onCancel) {
                Text(text = "Cancelar", fontFamily = interFamily)
            }
        }
    }

}