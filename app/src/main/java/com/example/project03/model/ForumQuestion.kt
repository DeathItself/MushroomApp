package com.example.project03.model

import com.google.firebase.Timestamp

data class ForumQuestion(
    val id: String = "",
    val title: String = "",
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now(),
)