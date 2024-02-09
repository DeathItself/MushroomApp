package com.example.project03.model

data class Question(
    val type: QuestionType,
    val image: String?,
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)

enum class QuestionType {
    Image,
    Text,
    Description,
    Comparison
}
