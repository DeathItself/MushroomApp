package com.example.project03.ui.screens.quiz

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.project03.model.Mushroom
import com.example.project03.model.Question
import com.example.project03.model.QuestionType
import com.example.project03.util.data.Data


@Composable
fun generateImageQuestion(mushrooms: List<Mushroom>): Question {
    val mushroomName = Data.DbCall().random().commonName
    val mushroomPic = Data.DbCall().random().photo
    val options = listOf(mushroomName) + mushrooms.shuffled().take(3).map { it.commonName }
    return Question(
        type = QuestionType.Image,
        image = mushroomPic,
        question = "**¿Cómo se llama esta seta?**",
        options = options.shuffled(),
        correctAnswer = mushroomName
    )
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun QuizApp(navController: NavController) {
    val mushrooms = Data.DbCall()
    val currentQuestion = mutableStateOf(generateImageQuestion(mushrooms))
    val score by remember { mutableStateOf(false) }
    val options = listOf("Opción 1", "Opción 2", "Opción 3")

    Scaffold { padding ->
        Column(Modifier.padding(padding)) {
            val question = currentQuestion.value

            Text(
                modifier = Modifier
                    .padding(130.dp)
                    .size(200.dp),
                text = "QUIZ",
                style = MaterialTheme.typography.displayLarge,
            )

            when (question.type) {
                QuestionType.Image -> {
                    AsyncImage(
                        modifier = Modifier.size(200.dp),
                        model = question.image,
                        contentDescription = null
                    )
                }

                QuestionType.Text -> {
                    Text(text = question.question)
                    // Mostrar opciones de respuesta para preguntas de tipo texto
                    // ...
                }

                else -> {
                    // Mostrar un mensaje de error o un componente por defecto
                    Text(text = "Tipo de pregunta no compatible")
                }
            }


            var selectedIndex by remember { mutableStateOf(0) }
            val interactionSource = remember { MutableInteractionSource() }

            Row {
                for (i in options.indices) {
                    RadioButton(
                        selected = (selectedIndex == i),
                        onClick = { selectedIndex = i },
                        interactionSource = interactionSource
                    )
                }




                Button(onClick = { /* Handle next question */ }) {
                    Text("Siguiente pregunta")
                }
            }
        }
    }
}

