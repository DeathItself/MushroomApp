package com.example.project03.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project03.model.ForumAnswer
import com.example.project03.model.ForumQuestion
import com.example.project03.repository.ForumRepository
import com.example.project03.ui.components.Loading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ForumViewModel(private val forumRepository: ForumRepository) : ViewModel() {


    private val _forumQuestions = MutableStateFlow<List<ForumQuestion>>(emptyList())
    val forumQuestions: MutableStateFlow<List<ForumQuestion>> = _forumQuestions

    fun loadForumQuestions(): List<ForumQuestion> {// Cargar preguntas del foro
        viewModelScope.launch {
            val questions = forumRepository.getForumQuestions()
            _forumQuestions.value = questions
        }
        return _forumQuestions.value
    }

    fun getForumQuestionById(id: String): ForumQuestion? {
        return _forumQuestions.value.find { it.id == id }
    }


    // Publicar una nueva pregunta en el foro
    fun postQuestion(question: ForumQuestion) {
        viewModelScope.launch {
            forumRepository.postQuestion(question)
            // Recargar las preguntas para reflejar la nueva
            loadForumQuestions()
        }
    }

    // Responder a una pregunta o respuesta en el foro
    fun postAnswer(answer: ForumAnswer) {
        viewModelScope.launch {
            forumRepository.postAnswer(answer)
            // Recargar las preguntas para reflejar la nueva respuesta
            // Nota: Esto puede necesitar ser optimizado para no recargar todas las preguntas
            loadForumQuestions()
        }
    }

    // Función para cargar respuestas de una pregunta específica
    @Composable
    fun loadAnswersForQuestion(questionId: String): MutableList<ForumAnswer> {
        var isLoading by remember { mutableStateOf(true) }
        var answers by remember { mutableStateOf(emptyList<ForumAnswer>()) }
        LaunchedEffect(key1 = Unit) {
            answers = forumRepository.getAnswersForQuestion(questionId)
            isLoading = false
        }
        if (isLoading) {
            Loading.LoadingState()
        }
        return answers.toMutableList()
    }

    //Reload forum answers after posting a new answer
    fun reloadForumAnswers(questionId: String) {
        viewModelScope.launch {
            val answers = forumRepository.getAnswersForQuestion(questionId)
            Log.d("ForumViewModel", "Answers: $answers")
            loadForumQuestions()
        }
    }
}


