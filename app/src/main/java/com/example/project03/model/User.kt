package com.example.project03.model

data class User(
    var id: String="",
    var username: String="",
    var email: String="",
    var password: String=""
)
{
    fun toMap(): MutableMap<String, String> {
        return mutableMapOf(
            "user_id" to this.id,
            "username" to  this.username,
            "email" to this.email,
            "password" to this.password
        )
    }
}
