package com.example.project03.model

import com.google.firebase.Timestamp

data class ForumAnswer(
    val id: String = "",
    val questionId: String = "",
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
