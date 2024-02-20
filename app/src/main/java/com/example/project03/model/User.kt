package com.example.project03.model

data class User(
    val id: String?,
    val username: String,
    val email: String,
){
    fun toMap(): MutableMap<String, String?> {
        return mutableMapOf(
            "user_id" to this.id,
            "username" to  this.username,
            "email" to this.email
        )
    }
}
