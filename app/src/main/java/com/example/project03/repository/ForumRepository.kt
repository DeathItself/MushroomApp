package com.example.project03.repository

import android.util.Log
import com.example.project03.model.ForumAnswer
import com.example.project03.model.ForumQuestion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ForumRepository {
    private val db = FirebaseFirestore.getInstance()

    // Cargar todas las preguntas del foro de forma asíncrona
    suspend fun getForumQuestions(): List<ForumQuestion> {
        val questions = mutableListOf<ForumQuestion>()

        try {
            val querySnapshot = db.collection("questions").get().await()
            for (document in querySnapshot.documents) {
                val question = document.toObject(ForumQuestion::class.java)
                question?.let {
                    questions.add(it)
                }
            }
            Log.d("ForumRepo", "Questions: $questions")
        } catch (e: Exception) {
            Log.e("ForumRepo", "Error getting forum questions: ${e.message}")
        }

        return questions
    }

    // Publicar una nueva pregunta en el foro
    suspend fun postQuestion(question: ForumQuestion) {
        try {
            db.collection("questions").document(question.id).set(question).await()
        } catch (e: Exception) {
            Log.e("ForumRepo", "Error posting question: ${e.message}")
        }
    }

    // Publicar una respuesta a una pregunta existente
    suspend fun postAnswer( answer: ForumAnswer) {
        try {
            db.collection("answers").document(answer.id).set(answer).await()
        } catch (e: Exception) {
            Log.e("ForumRepo", "Error posting answer: ${e.message}")
        }
    }

    // Cargar respuestas para una pregunta específica de forma asíncrona...0323363
    suspend fun getAnswersForQuestion(questionId: String): MutableList<ForumAnswer> {
        val responses = mutableListOf<ForumAnswer>()
        try {
            Log.d("idques", questionId)
            val querySnapshot = db.collection("answers")
                .whereEqualTo("questionId", questionId)
                .get()
                .await()
                .documents
            for (document in querySnapshot) {
                val response = document.toObject(ForumAnswer::class.java)
                Log.d("idquesto", response.toString())
                response?.let {
                    responses.add(it)
                }

                Log.d("ForumRepoR", "Responses: $responses")
            }
            return responses
        } catch (e: Exception) {
            Log.e("error getting answers for this question: $questionId", e.toString())
        }
        return responses
    }
}
