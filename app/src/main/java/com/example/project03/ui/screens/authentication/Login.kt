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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project03.ui.components.TextFields
import com.example.project03.ui.components.UIBottom

@Composable
fun LoginScreen(navController: NavController){
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
                    fontSize = 25.sp,
                )

                UseForm(
                    isCreateAccount = false
                ){
                        email, password ->
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
                        email, password ->
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                modifier = Modifier.width(330.dp),
                thickness = 1.dp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

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
    onDone: (String, String) -> Unit = {email, password ->}
) {
    val email = rememberSaveable{
        mutableStateOf("")
    }

    val password = rememberSaveable{
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(emailState = email)
        PasswordField(passwordState = password)
        SubmitBottom(
            textId = if(isCreateAccount) "Iniciar sesión" else "Unirse"
        ){
            onDone(email.value.trim(), password.value.trim())
        }
    }
}

@Composable
fun SubmitBottom(
    textId: String,
    onclick: () -> Unit
) {
    UIBottom(textId, onclick)
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




