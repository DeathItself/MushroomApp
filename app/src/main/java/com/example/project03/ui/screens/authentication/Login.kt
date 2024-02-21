package com.example.project03.ui.screens.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.ui.components.TextFields
import com.example.project03.ui.components.UIBottom
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.viewmodel.loginScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,

){
    val viewModel: loginScreenViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val showLoginForm = rememberSaveable{
        mutableStateOf(true)
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(showLoginForm.value){
                Text(
                    text = "Inicia sesión",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 33.sp,
                )
                
                Spacer(modifier = Modifier.height(15.dp))

                UseForm(
                    isCreateAccount = false
                ){
                    username,email, password ->
                    coroutineScope.launch {
                        viewModel.signInWithEmailAndPassword(email, password){
                            navController.navigate(AppScreens.HomeScreen.route)
                        }
                    }
                }
//                viewModel.checkLoggedIn(navController)
                SubmitBottom(
                    textId = "Unirse",
                    enabled = showLoginForm.value
                ){
                    showLoginForm.value = false
                }

            }else{
                Text(
                    text = "Resgistrate",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 25.sp,
                )

                UseForm(
                    isCreateAccount = true
                ){
                    username, email, password ->
                    coroutineScope.launch {
                        viewModel.createUserWithEmailAndPassword(username,email, password){
                            navController.navigate(AppScreens.LoginScreen.route)
                        }
                    }

                }

                SubmitBottom(
                    textId = "Iniciar sesión",
                    enabled = !showLoginForm.value
                ){
                    showLoginForm.value = true
                }
            }

            /*
            val text1 =
               if (showLoginForm.value) "Eres nuevo? Únete a nosotros!"
               else "¿Ya tienes cuenta? Inicia sesión"

            Text(text1)

             */
        }
    }
}

@Composable
fun UseForm(
    isCreateAccount: Boolean,
    onDone: (String, String, String) -> Unit
) {
    val email = rememberSaveable{
        mutableStateOf("")
    }
    val password = rememberSaveable{
        mutableStateOf("")
    }
    val username = rememberSaveable{
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(emailState = email)
        Spacer(modifier = Modifier.height(10.dp))

        PasswordField(passwordState = password)
        Spacer(modifier = Modifier.height(10.dp))

        SubmitBottom(
            textId = if(isCreateAccount) "Unirse" else "Iniciar sesión",
            enabled = true
        ){
            onDone(email.value.split("@").get(0), email.value.trim(), password.value.trim())
        }

        Spacer(modifier = Modifier.height(20.dp))
        val text = if(isCreateAccount) "¿Ya tienes cuenta? Inicia sesión" else "Eres nuevo? Únete a nosotros!"
        HorizontalDivider(
            modifier = Modifier.width(330.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(10.dp))
        
        Text(text)

        Spacer(modifier = Modifier.height(5.dp))
        
    }
}

@Composable
fun SubmitBottom(
    textId: String,
    enabled: Boolean,
    onclick: () -> Unit
) {
    UIBottom(
        textId = textId,
        enabled = enabled,
        onclick = onclick
    )
}

@Composable
fun EmailField(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    TextFields(
        label = labelId,
        valueState = emailState
    )
}

@Composable
fun PasswordField(
    passwordState: MutableState<String>,
    labelId: String = "Password"
){
    TextFields(
        label = labelId,
        valueState = passwordState
    )
}




