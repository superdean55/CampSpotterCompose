package hr.ferit.dejanmihic.campspottercompose.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.dejanmihic.campspottercompose.data.network.SingleUserRepository
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.Graph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel(): ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun logOut() {
        while (firebaseAuth.currentUser != null){
            firebaseAuth.signOut()
        }
    }
    fun updatePassword(password: String){
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun updateEmail(email: String){
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }
    fun createAccount(context: Context) :Boolean {
        if (!isEmailAndPasswordValid(context)){
            return false
        }
        updateLoadingIndicator(true)
        var isSuccess = false
        firebaseAuth.createUserWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnCompleteListener { task ->
                //progressBar.visibility = View.GONE
                if (task.isSuccessful) {

                    toastMessage("Account Created Successful",context)
                    isSuccess = true

                    setUiStateToDefault()
                    val email = task.result.user?.email
                    val uid = task.result.user?.uid
                    if (email != null && uid != null){
                        SingleUserRepository.createUserData(email = email, uid = uid, context)
                    }
                    firebaseAuth.signOut()
                    updateLoadingIndicator(false)
                } else {
                    toastMessage("Authentication failed.",context)
                    isSuccess = false
                    updateLoadingIndicator(false)
                }
            }
        return isSuccess
    }

    fun logIn(context: Context, navController: NavHostController){
        if (isEmailAndPasswordEmpty()){
            toastMessage("email or password empty",context)
            return
        }
        updateLoadingIndicator(true)

        firebaseAuth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result.user?.uid
                    if (uid != null){
                        SingleUserRepository.getUserDataByUid(uid)
                        toastMessage("Login Successful",context)
                        setUiStateToDefault()
                        updateLoadingIndicator(false)
                        navController.navigate(route = Graph.HOME){
                            popUpTo(navController.graph.startDestinationId) //removes login back stack
                        }
                    }else{
                        firebaseAuth.signOut()
                    }
                } else {
                    toastMessage("Authentication failed.",context)
                    updateLoadingIndicator(false)
                }
            }
    }

    private fun updateLoadingIndicator(boolean: Boolean){
        _uiState.update {
            it.copy(
                isLoadingData = boolean
            )
        }
    }
    private fun toastMessage(message: String, context: Context){
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG,
        ).show()
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
                isPasswordVisible = !uiState.value.isPasswordVisible,
            )
        }

        _uiState.update {
            it.copy(
                visualTransformation =
                if(uiState.value.visualTransformation == VisualTransformation.None){
                    PasswordVisualTransformation()
                }else{
                    VisualTransformation.None
                }
            )
        }

    }
    private fun isEmailAndPasswordValid(context: Context): Boolean{
        var errors = ""
        if(!isValidEmail(uiState.value.email)){
            errors += "Invalid email\n"
        }
        if (!isValidPassword(uiState.value.password)){
            errors += "Invalid password"
        }
        if(errors != ""){
            toastMessage(errors,context)
            return false
        }
        return true
    }
    fun setUiStateToDefault(){
        _uiState.update {
            it.copy(
                email = "",
                password = "",
                isPasswordVisible = false,
                isLoadingData = false,
                visualTransformation = PasswordVisualTransformation()
            )
        }
    }
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }
    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
        return passwordRegex.matches(password)
    }
}