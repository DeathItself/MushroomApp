package com.example.project03.ui.screens.quiz

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.R
import com.example.project03.model.Mushroom
import com.example.project03.model.Question
import com.example.project03.model.QuestionType
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.AppScreens
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.ui.theme.interFamily
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
    usedMushrooms: HashSet<String>,
    difficulty: MutableState<String>
): Question {
    if (mushrooms.isEmpty()) return Question(
        image = "",
        type = QuestionType.Text,
        question = stringResource(R.string.there_is_no_mushrooms_available),
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
    if (score < 30) {
        difficulty.value = "Dificuldad: baja"
    } else if (score < 80) {
        difficulty.value = "Dificuldad: media"
    } else {
        difficulty.value = "Dificuldad: alta"
    }
    val selectedMushroom: Mushroom

    if (filteredMushrooms.isNotEmpty()) {
        // Seleccionar un hongo aleatorio del conjunto filtrado
        selectedMushroom = filteredMushrooms.shuffled().random()
    } else {
        return Question(
            type = QuestionType.Text,
            image = "",
            question = stringResource(R.string.there_is_no_answers_available),
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
            question = stringResource(R.string.how_is_this_mushroom_called),
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
    val score = remember { mutableIntStateOf(0) }
    val usedMushrooms = remember { mutableStateOf(HashSet<String>()) }
    val triggerNewQuestion = remember { mutableStateOf(false) }
    var difficulty = remember { mutableStateOf("") }
    val currentQuestion =
        mutableStateOf(generateImageQuestion(mushrooms, score.intValue, usedMushrooms.value, difficulty))
    val options = currentQuestion.value.options
    val isHome = false
    val selectedIndex = remember { mutableIntStateOf(-1) } // -1 indica que no hay selección
    val startTime = remember { mutableIntStateOf(20000) }
    val elapsedTime = remember { mutableIntStateOf(0) }
    val checker = remember { mutableStateOf(false) }
    var checkerTrue = false
    if (currentQuestion.value.question == stringResource(R.string.there_is_no_answers_available)){
        // Muestra el resultado si no quedan mas preguntas
        checkerTrue = true
        ShowResultScreen(score.intValue, mushrooms.size, navController)
        elapsedTime.intValue = 0
    } else {
        // Observa cambios en el trigger para generar una nueva pregunta
        if (triggerNewQuestion.value) {
            // Update the question and reset states
            currentQuestion.value =
                generateImageQuestion(mushrooms, score.intValue, usedMushrooms.value, difficulty)
            triggerNewQuestion.value = false
            selectedIndex.intValue = -1
            elapsedTime.intValue = 0
        }
        Scaffold(topBar = {
            TopAppBarWithoutScaffold(isHome, navController, title = "Quiz")
        }, bottomBar = {
            BottomNavigationBar(navController)
        }) { padding ->
            LazyColumn(
                Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .scrollable(orientation = Orientation.Vertical,
                        enabled = true,
                        reverseDirection = false,
                        state = ScrollableState { delta -> delta })
            ) {
                item {
                    val question = currentQuestion.value
                    var remainingTime = 0
                    LaunchedEffect(Unit) {
                        withContext(Dispatchers.IO) {
                            while (remainingTime > 0) {
                                delay(1000)
                                elapsedTime.intValue += 1000
                            }
                        }
                    }
                    if (elapsedTime.intValue <= 20000) {
                        remainingTime = startTime.intValue - elapsedTime.intValue
                    }
                    Row(horizontalArrangement = Arrangement.Center) {

                        Text(
                            modifier = Modifier
                                .padding(horizontal = 30.dp),
                            text = stringResource(R.string.score)+": " + score.intValue,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )

                        Text(
                            modifier = Modifier,
                            text = stringResource(R.string.time_remaining)+": " + remainingTime / 1000 + "s",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
//                        nivel de la seta
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 50.dp),
                            text = difficulty.value,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (question.type) {
                            QuestionType.Image -> {
                                AsyncImage(
                                    modifier = Modifier
                                        .size(270.dp)
                                        .align(Alignment.CenterHorizontally),
                                    model = question.image,
                                    contentDescription = null,
                                )
                                // Muestra las opciones como RadioButtons
                                options.forEachIndexed { index, option ->
                                    ElevatedCard (
                                        modifier = Modifier
                                            .selectable(selectedIndex.intValue == index){
                                                selectedIndex.intValue = index
                                            }
                                            .fillMaxWidth(0.9f)
                                            .padding(vertical = 5.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.elevatedCardColors(
                                            containerColor = if (selectedIndex.intValue == index)MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surfaceContainerHighest,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                            disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                                        )
                                    ){
                                        Text(
                                            modifier = Modifier.padding(27.dp),
                                            text = option
                                        )
                                    }
                                }
                            }

                            else -> {
                                checkerTrue = true
                                ShowResultScreen(
                                    score.intValue,
                                    usedMushrooms.value.size,
                                    navController
                                )
                                elapsedTime.intValue = 0
                            }
                        }
                    }
                    val context = LocalContext.current
                    /*if (remainingTime <= 0) {
                        triggerNewQuestion.value = false
                        score.value = 0
                        question.image?.let {
                            ShowFailureScreen(
                                correctAnswer = question.correctAnswer,
                                imagen = it,
                                navController
                            )
                        }
                    }*/
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        if (!checker.value || remainingTime > 0) {
                            Button(modifier = Modifier
                                .padding(10.dp),
                                onClick = {
                                    // Primero, verifica si se ha seleccionado alguna opción
                                    if (selectedIndex.intValue == -1) {
                                        // Si no hay selección, muestra un Toast y retorna
                                        Toast.makeText(
                                            context,
                                            "Por favor, selecciona una opción",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return@Button
                                    }

                                    // Lógica para manejar la respuesta seleccionada
                                    if (selectedIndex.intValue == options.indexOf(currentQuestion.value.correctAnswer) && remainingTime > 0) {
                                        // Respuesta correcta, incrementa la puntuación y prepara la siguiente pregunta
                                        val bonusPoints = remainingTime / 1000
                                        score.intValue += bonusPoints
                                        selectedIndex.intValue =
                                            -1 // Resetea la selección para la siguiente pregunta
                                        elapsedTime.intValue = 0
                                        usedMushrooms.value.add(currentQuestion.value.correctAnswer)
                                        triggerNewQuestion.value = true
                                        Toast.makeText(
                                            context,
                                            "Correcto! Has obtenido +$bonusPoints puntos por tiempo restante",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // Respuesta incorrecta, resetea la puntuación y prepara la siguiente pregunta
                                        score.intValue = 0
                                        Toast.makeText(
                                            context,
                                            "Respuesta incorrecta",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        triggerNewQuestion.value = false
                                        checker.value = true
                                        elapsedTime.intValue = 20000
                                    }
                                }) {
                                if (checkerTrue) {
                                    Text(stringResource(R.string.quiz_finished), fontFamily = interFamily)
                                } else if (!checker.value) {
                                    Text(stringResource(R.string.next_question), fontFamily = interFamily)
                                } else {
                                    Text(stringResource(R.string.retry), fontFamily = interFamily)
                                }
                            }
                        }
                    }
                    if (checker.value || remainingTime <= 0) {
                        triggerNewQuestion.value = false
                        score.intValue = 0
                        question.image?.let {
                            ShowFailureScreen(
                                correctAnswer = question.correctAnswer,
                                imagen = it,
                                navController
                            )
                        }
                    }
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
    var timestamp: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.congratulations),
            style = MaterialTheme.typography.displayMedium,
            fontFamily = interFamily,
        )
        Text(
            text = stringResource(R.string.you_have_identified_all_mushrooms) +totalMushrooms+ stringResource(
                R.string.with
            ) +score + stringResource(R.string.score)+".",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = interFamily,
            fontSize = 17.sp
        )
        Button(
            modifier = Modifier
                .padding(16.dp),
            onClick = {
                // Share the score
                CoroutineScope(Dispatchers.IO).launch {
                    val result = Data.addRank(
                        score.toDouble(),
                        userId,
                        timestamp,
                        rango// Asegúrate de pasar el userId aquí
                    )
                    withContext(Dispatchers.Main) {
                        if (result == "New user added") { //Preguntar a Kevin porque puso esto
                            Toast.makeText(
                                context, context.getString(R.string.rank_saved), Toast.LENGTH_LONG
                            ).show()
                        } else if (result == "Score updated") {
                            Toast.makeText(
                                context, context.getString(R.string.rank_updated), Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.error_saving_rank), Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        ) {
            Text(stringResource(R.string.share_score), fontFamily = interFamily)
        }
        Button(
            modifier = Modifier
                .padding(16.dp),
            onClick = {
                // Restart the quiz
                navController.navigate(
                    AppScreens.QuizScreen.route
                )
            }) {
            Text(stringResource(R.string.play_again), fontFamily = interFamily)
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
                text = stringResource(R.string.you_failed),
                style = TextStyle(
                    color = Color.Red,
                    fontSize = 24.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = stringResource(R.string.the_correct_ans_was)+": " +correctAnswer,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontFamily = interFamily
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
                ) {
                    Text(text = stringResource(R.string.retry), fontFamily = interFamily)
                }

                Button(
                    onClick = { navController.navigate(AppScreens.HomeScreen.route) },
                    modifier = Modifier
                        .width(120.dp)
                ) {
                    Text(text = stringResource(R.string.exit), fontFamily = interFamily)
                }
            }
        }
    }
}


@Composable
fun showRankingAddedPopup(
    context: Context,
    rankingId: String
) {
    val dialog = AlertDialog.Builder(context)
        .setTitle(stringResource(R.string.rank_added))
        .setMessage(stringResource(R.string.ranking_aded_with_id) +": "+rankingId)
        .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
        .create()

    dialog.show()
}

@Composable
fun showRankingNotAddedPopup(
    context: Context,
    rankingId: String
) {
    val dialog = AlertDialog.Builder(context)
        .setTitle(stringResource(R.string.rank_not_added))
        .setNegativeButton("Error") { dialog, _ -> dialog.dismiss() }
        .create()

    dialog.show()
}


//crear una funcion a parte donde generaré la pantalla cuando se acabe el juego. Hay 2 escenarios,
// uno en el que el usuario acierta todas las setas y otro en el que falla una seta o se queda sin tiempo.
// 2 opciones de if, para saber si ha acertado todas solo he de mirar el tamaño de usuedMushroom,
// si es el mismo que el de la lista de setas pues las ha acertado todas, en caso contrario ha fallado alguna.
//esta función la llamaremos desde el scaffold donde irá un if, para saber si mostrar el quiz o el resultado.
// En la pantalla de resultado tendrás un boton para volver a jugar o publicar la puntuación



