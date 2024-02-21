package com.example.project03.ui.screens.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.model.User
import com.example.project03.ui.components.Loading
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.viewmodel.loginScreenViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun EditMyUserScreen(
    navController: NavController,

){
    val isHome = false
    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController)
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
    val userId = viewModel.userObject().id
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()
    var user by remember { mutableStateOf(viewModel.userObject) }
    var isLoading by remember { mutableStateOf(true) }

    if (userId.isNotEmpty()) {
        LaunchedEffect(key1 = userId){
            db.collection("users").document(userId).get().addOnSuccessListener { documentSnapshot ->
                user = documentSnapshot.toObject(User::class.java)?:User("", "", "", "")
                isLoading = false
            }
        }
    } else {
        isLoading = false
        Text("No se encontró el usuario")
    }

    if (isLoading){
        Loading.LoadingState()
    }else{
        EditMyUserForm(
            user = user,
            onSave = { updateUser ->
                coroutineScope.launch {
                    db.collection("users").document(userId).set(updateUser)
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
    val viewModel: loginScreenViewModel = viewModel()
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = AbsoluteAlignment.Left
    ) {
        var username by remember { mutableStateOf(viewModel.userObject.username) }
        var email by remember { mutableStateOf(viewModel.userObject.email) }
        //password

        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = {viewModel.userObject.username}
        )

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {viewModel.userObject.email}
        )

        //password

        Row{
            Button(
                onClick = {
                    user.username = username
                    user.email = email
                    onSave(user)
                }
            ){
                Text("Guardar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }

}