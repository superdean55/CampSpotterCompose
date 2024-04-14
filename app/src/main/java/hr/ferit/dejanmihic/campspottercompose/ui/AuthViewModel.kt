package hr.ferit.dejanmihic.campspottercompose.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.ferit.dejanmihic.campspottercompose.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    private val uiState: StateFlow<AuthUiState> = _uiState
    private val firebaseAuth: FirebaseAuth = Firebase.auth


    private fun userLogOut() {
        Firebase.auth.signOut()
    }

    private fun createAccount(context: Context) {
        if (isEmailAndPasswordEmpty()){
            Toast.makeText(
                context,
                "email or password empty",
                Toast.LENGTH_SHORT,
            ).show()
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
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

    private fun logIn( context: Context) {
        if (isEmailAndPasswordEmpty()){
            Toast.makeText(
                context,
                "email or password empty",
                Toast.LENGTH_LONG,
            ).show()
            return
        }
        _uiState.update {
            it.copy(
                isLoadingData = true
            )
        }
        firebaseAuth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    /*user.value = firebaseAuth.currentUser?.email.toString()*/
                    Toast.makeText(
                        context,
                        "Login Successful",
                        Toast.LENGTH_SHORT,
                    ).show()
                    _uiState.update {
                        it.copy(
                            isLoadingData = false
                        )
                    }
                    /*navController.navigate(route = Screen.MainScreen.route)*/
                } else {
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    _uiState.update {
                        it.copy(
                            isLoadingData = false
                        )
                    }
                }
            }
    }
    private fun isEmailAndPasswordEmpty():Boolean{
        if (uiState.value.email.isEmpty()||uiState.value.password.isEmpty()) {
         return true
        }
        return false
    }
    fun changePasswordVisibility(){
        _uiState.update {
            it.copy(
                isPasswordVisible = !uiState.value.isPasswordVisible
            )
        }

        if(uiState.value.isPasswordVisible){
            _uiState.update {
                it.copy(
                    visualTransformation = VisualTransformation.None as PasswordVisualTransformation
                )
            }
        }else{
            _uiState.update {
                it.copy(
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }
    }
}