package com.example.project03.ui.screens.forum

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.model.ForumAnswer
import com.example.project03.model.ForumQuestion
import com.example.project03.ui.components.TopAppBarWithoutScaffold
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.ForumViewModel
import com.example.project03.viewmodel.MainViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

val currentUser = FirebaseAuth.getInstance().currentUser


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    forumViewModel: ForumViewModel, navController: NavController
) {
    val questions = forumViewModel.forumQuestions.collectAsState(emptyList()).value.toMutableList()
    var selectedQuestionId by remember { mutableStateOf<String?>(null) }
    var addingQuestion by remember { mutableStateOf(false) }
    val currentUserUid = currentUser?.uid
    val mainViewModel: MainViewModel = viewModel()
    forumViewModel.loadForumQuestions()
    Log.d("ForumScreen", "$questions")

    Scaffold(topBar = {
        TopAppBarWithoutScaffold(
            isHome = false, navController = navController, title = "Forum"
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = { addingQuestion = true }) {
            Icon(Icons.Filled.Add, "Add Question")
        }
    }, bottomBar = {
        BottomNavigationBar(navController)
    }) { padding ->
        if (addingQuestion) {
            AddQuestionForm(onAddQuestion = { title, content ->
                val newQuestion = ForumQuestion(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    userId = currentUserUid ?: "unknown",
                    userName = currentUser?.email?.split("@")?.get(0) ?: "unknown",
                    timestamp = Timestamp.now()
                )
                questions += newQuestion
                forumViewModel.loadForumQuestions()
                addingQuestion = false
                forumViewModel.postQuestion(newQuestion)
            }, onCancel = { addingQuestion = false }, padding = padding
            )
//            refresh the forum questions after adding a new question to the list of questions
            forumViewModel.loadForumQuestions()
        }
        if (questions.isEmpty() && !addingQuestion) {
            Column(Modifier.padding(padding)) {
                Text("No questions available", style = MaterialTheme.typography.bodyMedium)
            }
        } else if (addingQuestion) {

        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .scrollable(
                        state = rememberScrollableState { delta -> delta },
                        orientation = Orientation.Vertical,
                        enabled = true
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(questions) { question ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuestionItem(
                            question = question,
                            isSelected = question.id == selectedQuestionId,
                            onQuestionClicked = { selectedQuestion ->
                                selectedQuestionId =
                                    if (selectedQuestionId == selectedQuestion.id) null else selectedQuestion.id
                            },
                            forumViewModel = forumViewModel
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


@Composable
fun AddQuestionForm(
    onAddQuestion: (title: String, content: String) -> Unit,
    onCancel: () -> Unit,
    padding: PaddingValues
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .scrollable(
                state = rememberScrollableState { delta -> delta },
                orientation = Orientation.Vertical,
                enabled = true
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.title)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = content,
            onValueChange = { content = it },
            label = { Text(stringResource(R.string.text)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { onAddQuestion(title, content) }) {
                Text(stringResource(R.string.add))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
        }
    }

}


@Composable
fun QuestionItem(
    question: ForumQuestion,
    isSelected: Boolean,
    onQuestionClicked: (ForumQuestion) -> Unit,
    forumViewModel: ForumViewModel,
) {
    val answers = forumViewModel.loadAnswersForQuestion(question.id)

    //Create a loading state for the answers


    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(), colors = CardColors(
            MaterialTheme.colorScheme.inverseOnSurface,
            MaterialTheme.colorScheme.inverseSurface,
            Color.Transparent,
            Color.Transparent
        )
    ) {
        Column(modifier = Modifier
            .clickable {
                onQuestionClicked(question)
            }
            .padding(8.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(// Nombre del usuario
                    text = question.userName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                // Fecha de creación
                Text(
                    text = question.timestamp.toDate().toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseSurface
                )


            }
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Text(// Título de la pregunta
                    text = question.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(// Contenido de la pregunta
                    text = question.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            } // AddResponseButton with onUpdateResponses function passed
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .height(if (isSelected && answers.isNotEmpty()) 150.dp else 0.dp)
                    .scrollable(
                        state = rememberScrollableState { delta -> delta },
                        orientation = Orientation.Vertical,
                        enabled = true)
            ) {item {
                if (answers.isNotEmpty()) {
                    answers.forEach { answer ->
                        Column {
                            Text(
                                text = answer.userName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "-   " + answer.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }


            }
            if (isSelected) {
                // AddResponseButton with onUpdateResponses function passed
                AddResponseButton(
                    questionId = question.id, onUpdateResponses = {
                        // Reload responses after adding a new response
                        forumViewModel.reloadForumAnswers(question.id)
                    }, forumViewModel = forumViewModel
                )

            }

        }
    }
}


@Composable
fun AddResponseButton(
    questionId: String, onUpdateResponses: () -> Unit, forumViewModel: ForumViewModel
) {

    var answerText by remember { mutableStateOf("") }
    var addingAnswer by remember { mutableStateOf(false) }

    if (!addingAnswer) {
        Button(onClick = { addingAnswer = true }) {
            Text(stringResource(R.string.add_response))
        }
        forumViewModel.reloadForumAnswers(questionId)
    }

    if (addingAnswer) {
        TextField(value = answerText,
            onValueChange = { answerText = it },
            label = { Text(stringResource(R.string.your_response)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = {
                if (answerText.isNotBlank()) {
                    val newResponse = ForumAnswer(
                        id = UUID.randomUUID().toString(),
                        questionId = questionId,
                        content = answerText,
                        userId = currentUser?.uid ?: "",
                        timestamp = Timestamp.now(),
                        userName = currentUser?.email?.split("@")?.get(0) ?: "unknown",
                    )
                    forumViewModel.postAnswer(newResponse)
                    addingAnswer = false
                    forumViewModel.reloadForumAnswers(questionId)
                    onUpdateResponses()
                    answerText = ""
                }
            }) {
                Text(stringResource(R.string.submit_response))
            }
            Button(onClick = { addingAnswer = false }) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}