package com.example.project03.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
    fun userObject(): User{
        var userId = auth.currentUser?.uid.toString()
        var username  = auth.currentUser?.email?.split("@")?.get(0).toString()
        var email = auth.currentUser?.email.toString()
        var password = ""
        var user = User(
            id = userId,
            username = username,
            email = email,
            password = password
        )
        //    log the user on console

        println("User: $user")

        return user
    }
    var userObject = userObject()

    @Composable
    fun checkLoggedIn(navController: NavController){
        val currentUser = FirebaseAuth.getInstance().currentUser

        val viewModel: loginScreenViewModel = viewModel()
        if (currentUser != null) {
            // Aquí, por ejemplo, podrías cargar los datos del usuario de Firestore antes de navegar
            loadUserData(currentUser.uid) { user ->
                viewModel.userObject.id = user.id
                viewModel.userObject.username = user.username
                viewModel.userObject.email= user.email
                viewModel.userObject.password= user.password

                // Navegar a la pantalla principal con los datos del usuario
                // Asegúrate de pasar los datos del usuario como argumento o usar un ViewModel compartido
                navController.navigate(AppScreens.HomeScreen.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
    private fun loadUserData(userId: String, onUserDataLoaded: (User) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            var user = document.toObject(User::class.java)
            user?.let {
                onUserDataLoaded(it)
            }
        }.addOnFailureListener { exception ->
            Log.d("Mushtool", "Error getting user data: ", exception)
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) = viewModelScope.launch{
        try {

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        home()
                        Log.d("Mushtool", "signInWithEmailAndPassword: ${userObject.username}")
                    }else{
                        Log.d("Mushtool", "signInWithEmailAndPassword: ${task.result}")
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
                        val username = task.result.user?.email?.split("@")?.get(0)
                        val userEmail = task.result.user?.email
                        if (username != null && userEmail != null){
                            createUser(username, userEmail, userPassword)
                        }
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
        username: String,
        email: String,
        password: String
    ) {

        val userId = auth.currentUser?.uid
        var user = User(
            id = userId!!,
            username = username,
            email = email,
            password = password
        )

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("Mushtool", "Creando ${it.id}")
            }.addOnFailureListener{
                Log.d("Mushtool", "Ocurrió un error $it")
            }
    }

/*    @Composable
    fun toObject(user: User) {
        val viewModel: loginScreenViewModel = viewModel()
        viewModel.user.id = user.id
        viewModel.user.username = user.username
        viewModel.user.email= user.email
        viewModel.user.password= user.password
    }*/

    fun signOut(navController: NavController){
        auth.signOut()
        navController.navigate(AppScreens.LoginScreen.route)
    }

    fun deleteUser(){
        val user = auth.currentUser
        user?.delete()
    }

    fun editUser(
        email: String
    ){
        val user = auth.currentUser
        user?.updateEmail(email)
    }

    private fun getCurrentUserDB(password: String):User{
        val currentUser = auth.currentUser
        if (currentUser != null){
            userObject.email= currentUser.email.toString()
            userObject.id = currentUser.uid
            userObject.username = currentUser.email?.split("@")?.get(0).toString()
            userObject.password = password
        }
        return userObject
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
