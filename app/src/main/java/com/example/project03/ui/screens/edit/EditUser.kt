package com.example.project03.ui.screens.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project03.model.User
import com.example.project03.ui.components.Loading
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


@Composable
fun EditUserScreen(
    navController: NavController,
    userId: String
){
    val isHome = false

    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController)
        }
    ){padding ->
        EditUser(navController = navController, userId = userId , padding = padding)
    }
}
@Composable
fun EditUser(navController: NavController, userId: String, padding: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()
    var user by remember { mutableStateOf(User("", "", "", "")) }
    var isLoading by remember { mutableStateOf(true) }

    // Carga los datos del usuario por ID y actualiza el estado
    LaunchedEffect(key1 = userId) {
        db.collection("users").document(userId).get().addOnSuccessListener { documentSnapshot ->
            user = documentSnapshot.toObject(User::class.java) ?: User("", "", "", "")
            isLoading = false
        }
    }

    if (isLoading) {
        Loading.LoadingState()// Muestra un indicador de carga mientras se cargan los datos
    } else {
        // Asegúrate de que la UI para editar el usuario se muestra después de cargar los datos
        EditUserForm(user = user, onSave = { updatedUser ->
            coroutineScope.launch {
                db.collection("users").document(userId).set(updatedUser.toMap())
                navController.popBackStack()
            }
        }, onCancel = {
            navController.popBackStack()
        }, padding)
    }
}

@Composable
fun EditUserForm(
    user: User,
    onSave: (User) -> Unit,
    onCancel: () -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var username by remember { mutableStateOf(user.username) }
        var email by remember { mutableStateOf(user.email) }

        OutlinedTextField(value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de Usuario") })
        OutlinedTextField(value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") })

        Row {
            Button(onClick = {
                // Actualiza user con los nuevos valores
                user.username = username
                user.email = email
                onSave(user)
            }) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }
}