package com.example.project03.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

class loginScreenViewModel: ViewModel(){
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun checkLoggedIn(navController: NavController){
        if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate(AppScreens.HomeScreen.route){
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        home()
                    }else{
                        Log.d("Mushtool", "signInWithEmailAndPassword: ${task.result.toString()}")
                    }
                }
        }catch (ex:Exception){
            Log.d("Mushtool", "signInWithEmailAndPassword: ${ex.message}")
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ){
        if (_loading.value == false){
            _loading.value = true
            val userPassword = password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        val displayName = task.result.user?.email?.split("@")?.get(0)
                        val userEmail = task.result.user?.email
                        createUser(displayName, userEmail, userPassword)
                        home()
                    }

                    else{
                        Log.d("Mushtool", "createUserWithEmailAndPassword: ${task.result.toString()}")
                    }

                    _loading.value = false
                }
        }
    }

    private fun createUser(
        displayName: String?,
        email: String?,
        password: String?
    ) {

        val userId = auth.currentUser?.uid
        val user = User(
            id = userId.toString(),
            username = displayName.toString(),
            email = email.toString(),
            password = password.toString()
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("Mushtool", "Creando ${it.id}")
            }.addOnFailureListener{
                Log.d("Mushtool", "Ocurrió un error ${it}")
            }
    }


    //logica para hacer el CRUD firebase
//  Read users from firebase and update the User object
    suspend fun readUsersFromFirebase(userId: String, user: User, navController: NavController): User{
        val db = FirebaseFirestore.getInstance()
        var isLoading = true
        db.collection("users").document(userId).get().addOnSuccessListener { documentSnapshot ->
            user.username = documentSnapshot.getString("username").toString()
            user.email = documentSnapshot.getString("email").toString()
            user.password = documentSnapshot.getString("password").toString()
            user.id = documentSnapshot.getString("user_id").toString()
            isLoading = false
        }
        if (isLoading){
            Log.d("Mushtool", "Cargando datos")
        }else{
            Log.d("Mushtool", "Datos cargados")
        }
        return user
    }


}