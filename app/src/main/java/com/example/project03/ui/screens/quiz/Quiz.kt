package com.example.project03.ui.screens.quiz

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.model.Mushroom
import com.example.project03.model.Question
import com.example.project03.model.QuestionType
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.util.data.Data
import com.example.project03.viewmodel.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun generateImageQuestion(
    mushrooms: List<Mushroom>,
    score: Int,
    usedMushrooms: HashSet<String>
): Question {
    if (mushrooms.isEmpty()) return Question(
        image = "",
        type = QuestionType.Text,
        question = "No hay setas disponibles",
        options = emptyList(),
        correctAnswer = ""
    )
    // Filtrar setas según la dificultad y puntuación del usuario
    val filteredMushrooms = if (score < 30) {
        mushrooms.filter { it.dificulty == "Low" && !usedMushrooms.contains(it.commonName) }
    } else if (score < 80) {
        mushrooms.filter {
            it.dificulty in arrayOf(
                "Low",
                "Medium"
            ) && !usedMushrooms.contains(it.commonName)
        }
    } else {
        mushrooms.filter { it.dificulty == "Hard" && !usedMushrooms.contains(it.commonName) }
    }
    val selectedMushroom: Mushroom

    if (filteredMushrooms.isNotEmpty()) {
        // Seleccionar un hongo aleatorio del conjunto filtrado
        selectedMushroom = filteredMushrooms.shuffled().random()
    } else {
        return Question(
            type = QuestionType.Text,
            image = "",
            question = "**No hay respuestas disponibles**",
            options = emptyList(),
            correctAnswer = ""
        )
    }
    selectedMushroom.let {
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
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun QuizApp(navController: NavController) {
    val mainViewModel: MainViewModel = viewModel()
    val mushrooms = Data.wikiDBList()
    val score = remember { mutableStateOf(0) }
    val usedMushrooms = remember { mutableStateOf(HashSet<String>()) }
    val triggerNewQuestion = remember { mutableStateOf(false) }
    val currentQuestion =
        mutableStateOf(generateImageQuestion(mushrooms, score.value, usedMushrooms.value))
    val options = currentQuestion.value.options
    val isHome = false

    val selectedIndex = remember { mutableStateOf(-1) } // -1 indica que no hay selección
    val startTime = remember { mutableStateOf(20000) }
    val elapsedTime = remember { mutableStateOf(0) }
    if (currentQuestion.value.question == "No hay más preguntas disponibles") {
        // Muestra el resultado si no quedan mas preguntas
        ShowResultScreen(score.value, mushrooms.size, navController)
    } else {
        // Observa cambios en el trigger para generar una nueva pregunta
        if (triggerNewQuestion.value) {
            // Update the question and reset states
            currentQuestion.value =
                generateImageQuestion(mushrooms, score.value, usedMushrooms.value)
            triggerNewQuestion.value = false
            selectedIndex.value = -1
            elapsedTime.value = 0
        }
        Scaffold(topBar = {
            TopAppBarWithoutScaffold(isHome, navController)
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
                            ShowResultScreen(score.value, usedMushrooms.value.size, navController)
                        }
                    }
                }
                val context = LocalContext.current
                if (remainingTime == 0) {
                    triggerNewQuestion.value = false
                    elapsedTime.value = 0
                    score.value = 0
                    question.image?.let {
                        ShowFailureScreen(
                            correctAnswer = question.correctAnswer,
                            imagen = it,
                            navController
                        )
                    }
                }
                Button(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                    onClick = {
                        // Primero, verifica si se ha seleccionado alguna opción
                        if (selectedIndex.value == -1) {
                            // Si no hay selección, muestra un Toast y retorna
                            Toast.makeText(
                                context,
                                "Por favor, selecciona una opción",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        // Lógica para manejar la respuesta seleccionada
                        if (selectedIndex.value == options.indexOf(currentQuestion.value.correctAnswer) && remainingTime > 0) {
                            // Respuesta correcta, incrementa la puntuación y prepara la siguiente pregunta
                            val bonusPoints = remainingTime / 1000
                            score.value += bonusPoints
                            selectedIndex.value =
                                -1 // Resetea la selección para la siguiente pregunta
                            elapsedTime.value = 0
                            usedMushrooms.value.add(currentQuestion.value.correctAnswer)
                            triggerNewQuestion.value = true
                            Toast.makeText(
                                context,
                                "Correcto! Has obtenido +$bonusPoints puntos por tiempo restante",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Respuesta incorrecta, resetea la puntuación y prepara la siguiente pregunta
                            score.value = 0
                            elapsedTime.value = 0
                            Toast.makeText(context, "Respuesta incorrecta", Toast.LENGTH_SHORT)
                                .show()
                            triggerNewQuestion.value = false

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
}

@Composable
fun ShowResultScreen(score: Int, totalMushrooms: Int, navController: NavController) {
    val context = LocalContext.current
    val userId = Firebase.auth.currentUser?.uid
    val rango = Data.myRankList()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "¡Felicidades!",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Has acertado todas las setas ($totalMushrooms) con $score puntos.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            onClick = {
                // Share the score
                CoroutineScope(Dispatchers.IO).launch {
                    val result = Data.addRank(
                        score.toDouble(), userId, rango// Asegúrate de pasar el userId aquí
                    )
                    withContext(Dispatchers.Main) {
                        if (result == "Rank added") { //Preguntar a Kevin porque puso esto
                            Toast.makeText(
                                context, "Rango guardada con éxito", Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context, "Error al guardar el rango", Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }) {
            Text("Compartir puntuación")
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            onClick = {
                // Restart the quiz
                navController.navigate(
                    AppScreens.QuizScreen.route
                )
            }) {
            Text("Volver a jugar")
        }
    }
}

@Composable
fun ShowFailureScreen(
    correctAnswer: String,
    imagen: String,
    navController: NavController
) {
    val backgroundImage = remember { mutableStateOf(imagen) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center),
            model = backgroundImage,
            contentDescription = "Imagen de fondo de fallo",
        )
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Has fallado",
                style = TextStyle(
                    color = Color.Red,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = "La respuesta correcta era: $correctAnswer",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { navController.navigate(AppScreens.QuizScreen.route) },
                    modifier = Modifier
                        .width(120.dp)
                        .background(Color.Green)
                ) {
                    Text(text = "Reintentar")
                }

                Button(
                    onClick = { navController.navigate(AppScreens.HomeScreen.route) },
                    modifier = Modifier
                        .width(120.dp)
                        .background(Color.Red)
                ) {
                    Text(text = "Salir")
                }
            }
        }
    }
}


fun showRankingAddedPopup(
    context: Context,
    rankingId: String
) {
    val dialog = AlertDialog.Builder(context)
        .setTitle("¡Nuevo ranking añadido!")
        .setMessage("Se ha añadido tu ranking con el ID: $rankingId")
        .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
        .create()

    dialog.show()
}

fun showRankingNotAddedPopup(
    context: Context,
    rankingId: String
) {
    val dialog = AlertDialog.Builder(context)
        .setTitle("No se ha podido subir la puntuación")
        .setNegativeButton("Error") { dialog, _ -> dialog.dismiss() }
        .create()

    dialog.show()
}


//crear una funcion a parte donde generaré la pantalla cuando se acabe el juego. Hay 2 escenarios, uno en el que el usuario acierta todas las setas y otro en el que falla una seta o se queda sin tiempo.
// 2 opciones de if, para saber si ha acertado todas solo he de mirar el tamaño de usuedMushroom, si es el mismo que el de la lista de setas pues las ha acertado todas, en caso contrario ha fallado alguna.
//esta función la llamaremos desde el scaffold donde irá un if, para saber si mostrar el quiz o el resultado. En la pantalla de resultado tendrás un boton para volver a jugar o publicar la puntuación



