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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@Composable
fun generateImageQuestion(mushrooms: List<Mushroom>, score: Int): Question {
    if (mushrooms.isEmpty()) return Question(
        image = "",
        type = QuestionType.Text,
        question = "No hay setas disponibles",
        options = emptyList(),
        correctAnswer = ""
    )
    // Filtrar setas según la dificultad y puntuación del usuario
    val filteredMushrooms = if (score < 30) {
        mushrooms.filter { it.dificulty == "Low" }
    } else if (score < 80) {
        mushrooms.filter { it.dificulty == "Low" || it.dificulty == "Medium" }
    } else {
        mushrooms.filter { it.dificulty == "Hard" }
    }

    // Seleccionar un hongo aleatorio del conjunto filtrado
    val selectedMushroom = filteredMushrooms.shuffled().random()
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
    val score = remember { mutableStateOf(0) }
    val currentQuestion = mutableStateOf(generateImageQuestion(mushrooms, score.value))
    val options = currentQuestion.value.options
    var radioIndex: Int
    val isHome = false
    val triggerNewQuestion = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(-1) } // -1 indica que no hay selección
    val startTime = remember { mutableStateOf(20000) }
    val elapsedTime = remember { mutableStateOf(0) }

    // Observa cambios en el trigger para generar una nueva pregunta
    if (triggerNewQuestion.value) {
        // Actualizar la pregunta actual
        currentQuestion.value = generateImageQuestion(mushrooms, score.value)
        triggerNewQuestion.value = false // Restablecer el disparador
    }
    Scaffold(topBar = {
        TopAppBarWithoutScaffold(isHome, navController, title = "Quiz")
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {

            val question = currentQuestion.value
            Text(
                modifier = Modifier
                    .padding(5.dp)
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
            var remainingTime = startTime.value - elapsedTime.value
            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    while (remainingTime != 0) {
                        delay(1000)
                        elapsedTime.value += 1000
                    }
                }
            }
            Text(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Tiempo restante: ${remainingTime / 1000}s",
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
                                .size(150.dp)
                                .align(Alignment.CenterHorizontally),
                            model = question.image,
                            contentDescription = null,
                        )
                        // Muestra las opciones como RadioButtons
                        options.forEachIndexed { index, option ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedIndex.value == index, // Verifica si este RadioButton está seleccionado
                                    onClick = {
                                        selectedIndex.value =
                                            index // Actualiza el estado con el índice de la opción seleccionada
                                    })
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
            if (remainingTime == 0) {
                triggerNewQuestion.value = true
                elapsedTime.value = 0
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(16.dp),
                onClick = {
                // Primero, verifica si se ha seleccionado alguna opción
                if (selectedIndex.value == -1) {
                    // Si no hay selección, muestra un Toast y retorna
                    Toast.makeText(context, "Por favor, selecciona una opción", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Lógica para manejar la respuesta seleccionada
                if (options[selectedIndex.value] == currentQuestion.value.correctAnswer && remainingTime > 0) {
                    // Respuesta correcta, incrementa la puntuación y prepara la siguiente pregunta
                    val bonusPoints = remainingTime / 1000
                    score.value += bonusPoints
                    triggerNewQuestion.value = true
                    selectedIndex.value = -1 // Resetea la selección para la siguiente pregunta
                    elapsedTime.value = 0
                    Toast.makeText(
                        context,
                        "Correcto! Has obtenido +$bonusPoints puntos por tiempo restante",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Respuesta incorrecta, resetea la puntuación y prepara la siguiente pregunta
                    score.value = 0
                    elapsedTime.value = 0
                    Toast.makeText(context, "Respuesta incorrecta", Toast.LENGTH_SHORT).show()
                    triggerNewQuestion.value = true
                }
            }) {
                Text("Siguiente pregunta")
            }

        }
        if (mainViewModel.showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { mainViewModel.showBottomSheet = false }) {
                ContentBottomSheet(mainViewModel, navController)
            }
        }
    }
}


