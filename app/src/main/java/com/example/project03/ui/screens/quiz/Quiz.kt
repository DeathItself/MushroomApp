package com.example.project03.ui.screens.quiz

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.model.Mushroom
import com.example.project03.model.Question
import com.example.project03.model.QuestionType
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.util.data.Data
import com.example.project03.viewmodel.MainViewModel


@Composable
fun generateImageQuestion(mushrooms: List<Mushroom>): Question {
    if (mushrooms.isEmpty()) return Question(
        image = "",
        type = QuestionType.Text,
        question = "No hay setas disponibles",
        options = emptyList(),
        correctAnswer = ""
    )
    val selectedMushroom = mushrooms.random()
    val mushroomName = selectedMushroom.commonName
    val mushroomPic = selectedMushroom.photo
    // Asegurarse de que las opciones incluyan el nombre correcto y sean únicas
    val options =
        (listOf(mushroomName) + mushrooms.map { it.commonName }.shuffled().take(3)).distinct()
            .shuffled()
    return Question(
        type = QuestionType.Image,
        image = mushroomPic,
        question = "**¿Cómo se llama esta seta?**",
        options = options,
        correctAnswer = mushroomName
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun QuizApp(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val mushrooms = Data.wikiDBList()
    val currentQuestion = mutableStateOf(generateImageQuestion(mushrooms))
    val score = remember { mutableStateOf(0) }
    val options = currentQuestion.value.options
    val isHome = false
    val triggerNewQuestion = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(-1) } // -1 indica que no hay selección


    // Observa cambios en el trigger para generar una nueva pregunta
    if (triggerNewQuestion.value) {
        // Actualizar la pregunta actual
        currentQuestion.value = generateImageQuestion(mushrooms)
        triggerNewQuestion.value = false // Restablecer el disparador
    }
    Scaffold(
        topBar = {
            TopAppBarWithoutScaffold(isHome, navController)
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            val question = currentQuestion.value
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally),
                text = "QUIZ",
                style = MaterialTheme.typography.displayMedium,
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Puntuación: ${score.value}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp)
            ) {
                when (question.type) {
                    QuestionType.Image -> {
                        AsyncImage(
                            modifier = Modifier
                                .size(250.dp)
                                .align(Alignment.CenterHorizontally),
                            model = question.image,
                            contentDescription = null,
                        )
                        // Muestra las opciones como RadioButtons
                        options.forEachIndexed { index, option ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedIndex.value == index, // Verifica si este RadioButton está seleccionado
                                    onClick = {
                                        selectedIndex.value =
                                            index // Actualiza el estado con el índice de la opción seleccionada
                                    }
                                )
                                Text(option)
                            }
                        }

                    }

                    else -> {
                        Text(text = "Tipo de pregunta no compatible")
                    }
                }
            }
            val context = LocalContext.current
            Button(onClick = {
                // Comprobar si la respuesta seleccionada es correcta y actualizar la pregunta
                if (options[selectedIndex.value] == currentQuestion.value.correctAnswer) {
                    // Incrementar puntuación o mostrar mensaje de correcto, si es necesario
                    score.value += 1
                    triggerNewQuestion.value = true
                    selectedIndex.value = -1
                } else {
                    score.value = 0
                    Toast.makeText(context, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
                }

            }) {
                Text("Siguiente pregunta")
            }
        }
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mainViewModel.showBottomSheet = false }
            ) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}


