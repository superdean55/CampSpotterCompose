package hr.ferit.dejanmihic.campspottercompose

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AppViewModel(): ViewModel() {
    //var state by mutableStateOf(AuthenticationState())
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    lateinit var navController: NavHostController
    var context: Context? = null
    val user = mutableStateOf("")
    private val firebaseAuth = FirebaseAuth.getInstance()
    var loading = mutableStateOf(false)

    fun onAction(action: AppActions) {
        when(action) {
            is AppActions.LogIn -> logIn(email = email, password = password)
            is AppActions.CreateAccount -> createAccount(email = email, password = password)
            is AppActions.UserLogOut -> userLogOut()
            is AppActions.NavigateToRegisterScreen -> navToRegisterScreen()
        }
    }

    private fun navToRegisterScreen() {
        navController.navigate(route = Screen.RegisterScreen.route)
        email.value = ""
        password.value = ""
    }

    private fun userLogOut() {
        Firebase.auth.signOut()
        email.value = ""
        password.value = ""
        navController.navigate(route = Screen.LoginScreen.route)
    }

    private fun createAccount(email: MutableState<String>, password: MutableState<String>) {
        if (email.value.isEmpty() ||password.value.isEmpty()){
            Toast.makeText(
                context,
                "email or password empty",
                Toast.LENGTH_SHORT,
            ).show()
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                //progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Toast.makeText(
                        context,
                        "Acount Created Successful",
                        Toast.LENGTH_SHORT,
                    ).show()

                } else {
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun logIn(email: MutableState<String>, password: MutableState<String>) {
        if (email.value.isEmpty()||password.value.isEmpty()){
            Toast.makeText(
                context,
                "email or password empty",
                Toast.LENGTH_SHORT,
            ).show()
            return
        }
        loading.value = true
        firebaseAuth.signInWithEmailAndPassword(email.value, password.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.value = firebaseAuth.currentUser?.email.toString()
                    Toast.makeText(
                        context,
                        "Login Successful",
                        Toast.LENGTH_SHORT,
                    ).show()
                    loading.value = false
                    navController.navigate(route = Screen.MainScreen.route)
                } else {
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    loading.value = false
                }
            }
    }


}