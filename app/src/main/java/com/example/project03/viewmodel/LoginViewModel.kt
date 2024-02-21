package com.example.project03.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.project03.model.User
import com.example.project03.ui.navigation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class loginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    suspend fun loadUserData(userId: String): User {
        val db = FirebaseFirestore.getInstance()
        val document = db.collection("users").document(userId).get().await()
        return document.toObject(User::class.java)!!
    }


    suspend fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                if (result.user != null) {
                    home()
                }
            } catch (ex: Exception) {
                Log.e("Mushtool", "signInWithEmailAndPassword: ${ex.message}")
            }
        }

    suspend fun createUserWithEmailAndPassword(
        username: String,
        email: String,
        password: String,
        home: () -> Unit
    ) {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                createUser(result.user!!.uid, username, email, password)
                home()
            }
        } catch (ex: Exception) {
            Log.e("Mushtool", "createUserWithEmailAndPassword: ${ex.message}", ex)
        }

    }


    private suspend fun createUser(
        id: String,
        username: String,
        email: String,
        password: String
    ) {
        val user = User(
            id = id,
            username = username,
            email = email,
            password = password
        )

        FirebaseFirestore.getInstance().collection("users")
            .document(user.id).set(user).await()
    }

    fun signOut(navController: NavController) {
        auth.signOut()
        navController.navigate(AppScreens.LoginScreen.route)
    }

    suspend fun deleteUser(id: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(id).delete().await()
        val auth = Firebase.auth
        auth.currentUser!!.delete()
    }
}
