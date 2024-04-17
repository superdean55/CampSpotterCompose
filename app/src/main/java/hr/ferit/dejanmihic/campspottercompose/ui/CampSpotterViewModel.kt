package hr.ferit.dejanmihic.campspottercompose.ui

import android.Manifest
import android.R.id.message
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.ferit.dejanmihic.campspottercompose.AppActions
import hr.ferit.dejanmihic.campspottercompose.Screen
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.LocationDetails
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotNavigationType
import hr.ferit.dejanmihic.campspottercompose.ui.theme.md_theme_light_error
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate



class CampSpotterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CampSpotterUiState())
    val uiState: StateFlow<CampSpotterUiState> = _uiState


    val email = mutableStateOf("")
    val password = mutableStateOf("")
    lateinit var navController: NavHostController
    var context: Context? = null
    val user = mutableStateOf("")
    private val firebaseAuth = FirebaseAuth.getInstance()
    var loading = mutableStateOf(false)

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (lo in p0.locations) {
                _uiState.update {
                    it.copy(
                        campSpotForm = it.campSpotForm.copy(
                            locationDetails = LocationDetails(lo.latitude, lo.longitude)
                        )
                    )
                }
                stopLocationUpdates()
            }
        }
    }
    fun updateUri(uri: Uri){
        _uiState.update {
            it.copy(
                campSpotForm = it.campSpotForm.copy(
                    imageUri = uri,
                ),
                campSpotFormErrors = it.campSpotFormErrors.copy(
                    isImageUriError = false
                )
            )
        }
    }
    fun startLocationUpdates(
        context: Context
    ) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            this.fusedLocationClient!!.requestLocationUpdates(
                locationRequest,
                this.locationCallback,
                Looper.getMainLooper()
            )
        } else {

        }
    }

    fun stopLocationUpdates() {
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
        _uiState.update {
            it.copy(
                campSpotFormErrors = it.campSpotFormErrors.copy(
                    isLocationDetailsError = false
                )
            )
        }
    }
    fun updateCampSpotForm(campSpot: CampSpot){
        _uiState.update {
            it.copy(
                campSpotForm = campSpot
            )
        }
    }
    fun updatePickedStartDate(date: LocalDate){
        _uiState.update {
            it.copy(
                campSpotForm = it.campSpotForm.copy(
                    startEventDate = date,
                    endEventDate = date
                )
            )
        }
    }
    fun updatePickedEndDate(endEventDate: LocalDate, context: Context){

        if(_uiState.value.campSpotForm.startEventDate > endEventDate){
            Toast.makeText(
                context,
                "Event must end after Event is Started",
                Toast.LENGTH_SHORT,
            ).show()
            return
        }
        _uiState.update {
            it.copy(
                campSpotForm = it.campSpotForm.copy(
                    endEventDate = endEventDate
                )
            )
        }
    }

    fun updateCampSpotTitle(title: String){
        _uiState.update {
            it.copy(
                campSpotForm = it.campSpotForm.copy(
                    title = title,
                ),
                campSpotFormErrors = it.campSpotFormErrors.copy(
                    isTitleError = false
                )
            )
        }
    }
    fun updateCampSpotDescription(description: String){
        _uiState.update {
            it.copy(
                campSpotForm = it.campSpotForm.copy(
                    description = description
                )
            )
        }
    }
    fun updateCampSpotNumberOfPeople(numberOfPeople: String, context: Context){
        if(numberOfPeople == ""){
            _uiState.update {
                it.copy(
                    campSpotForm = it.campSpotForm.copy(
                        numberOfPeople = numberOfPeople,
                    ),
                    campSpotFormErrors = it.campSpotFormErrors.copy(
                        isNumberOfPeopleError = false
                    )
                )
            }
        }else{
            var parsedInt = -1

            try {
                parsedInt = numberOfPeople.toInt()
            } catch (nfe: NumberFormatException) {
                Toast.makeText(
                    context,
                    "You need enter number",
                    Toast.LENGTH_SHORT,
                ).show()
            }

            if(parsedInt > 0) {
                _uiState.update {
                    it.copy(
                        campSpotForm = it.campSpotForm.copy(
                            numberOfPeople = numberOfPeople,
                        ),
                        campSpotFormErrors = it.campSpotFormErrors.copy(
                            isNumberOfPeopleError = false
                        )

                    )
                }
            }
        }

    }
    private fun isValidCampSpotFormData(context: Context): Boolean{
        var errors = ""
        if(!isGpsLocationNotEmpty(uiState.value.campSpotForm.locationDetails)){
            errors += "update Location, press get location\n"
            _uiState.update {
                it.copy(
                    campSpotFormErrors = it.campSpotFormErrors.copy(
                        isLocationDetailsError = true
                    )
                )
            }
        }
        if(!isImageUriNotEmpty(uiState.value.campSpotForm.imageUri)){
            errors += "You need teak a picture\n"
            _uiState.update {
                it.copy(
                    campSpotFormErrors = it.campSpotFormErrors.copy(
                        isImageUriError = true
                    )
                )
            }
        }
        if(!isTextInputNotEmpty(uiState.value.campSpotForm.numberOfPeople)){
            errors += "Enter number of People\n"
            _uiState.update {
                it.copy(
                    campSpotFormErrors = it.campSpotFormErrors.copy(
                        isNumberOfPeopleError = true
                    )
                )
            }
        }
        if(!isTextInputNotEmpty(uiState.value.campSpotForm.title)){
            errors += "Enter title\n"
            _uiState.update {
                it.copy(
                    campSpotFormErrors = it.campSpotFormErrors.copy(
                        isTitleError = true
                    )
                )
            }
        }
        if(errors != "") {
            val toast = Toast.makeText(context, errors, Toast.LENGTH_LONG)
            val view = toast.view
            view!!.background.setColorFilter(md_theme_light_error.toArgb(), PorterDuff.Mode.SRC_IN)
            val text = view!!.findViewById<TextView>(message)
            text.setTextColor(Color.BLACK)
            toast.show()
            return false
        }else {
            return true
        }
    }
    fun saveCampSpot(context: Context): Boolean {
        if (isValidCampSpotFormData(context)) {
            _uiState.update { uiState ->
                val updatedCampSpot = uiState.campSpots.map {
                    if (it.id == this.uiState.value.campSpotForm.id) {
                        it.copy(
                            imageUri = this.uiState.value.campSpotForm.imageUri,
                            title = this.uiState.value.campSpotForm.title,
                            description = this.uiState.value.campSpotForm.description,
                            locationDetails = this.uiState.value.campSpotForm.locationDetails,
                            numberOfPeople = this.uiState.value.campSpotForm.numberOfPeople,
                            startEventDate = this.uiState.value.campSpotForm.startEventDate,
                            endEventDate = this.uiState.value.campSpotForm.endEventDate
                        )
                    } else {
                        it
                    }
                }
                uiState.copy(campSpots = updatedCampSpot.toMutableList())
            }
            return true
        }
        return false
    }
    fun addCampSpot(context: Context) :Boolean{
        if(isValidCampSpotFormData(context)){
            val campSpots = uiState.value.campSpots.toMutableList()
            campSpots.add(uiState.value.campSpotForm)
            _uiState.update {
                it.copy(
                    campSpots = campSpots
                )
            }
            return true
        }
        return false
    }

    private fun isTextInputNotEmpty(text: String): Boolean {
        if(text == ""){
            return false
        }
        return true
    }

    private fun isImageUriNotEmpty(imageUri: Uri) : Boolean{
        if(imageUri.path?.isNotEmpty() == true){
            return true
        }
        return false
    }

    private fun isGpsLocationNotEmpty(locationDetails: LocationDetails) :Boolean{
        if(locationDetails.latitude == 0.toDouble() || locationDetails.longitude == 0.toDouble()){
            return false
        }
        return true
    }

    fun updateBirthDate(date: LocalDate){
        _uiState.update {
            it.copy(
                user = it.user.copy(
                    birthDate = date
                )
            )
        }
    }
    fun updateFirstName(firstName: String){
        _uiState.update {
            it.copy(
                user = it.user.copy(
                    firstName = firstName
                    ),
                    userFormErrors = it.userFormErrors.copy(
                    isFirstNameError = false
                )
            )
        }
    }
    fun updateLastName(lastName: String){
        _uiState.update {
            it.copy(
                user = it.user.copy(
                    lastName = lastName
                ),
                userFormErrors = it.userFormErrors.copy(
                    isLastNameError = false
                )
            )
        }
    }
    fun updateUserImageUri(imageUri: Uri?){
        _uiState.update {
            it.copy(
                user = it.user.copy(
                    image = imageUri ?: Uri.EMPTY
                )
            )
        }
    }
    fun saveEditedUser(user: User,context: Context): Boolean{
        var errors = ""
        if(!isTextInputNotEmpty(uiState.value.user.firstName)){
            errors += "First Name is empty\n"
            _uiState.update {
                it.copy(
                    userFormErrors = it.userFormErrors.copy(
                        isFirstNameError = true
                    )
                )
            }
        }
        if(!isTextInputNotEmpty(uiState.value.user.lastName)){
            errors += "Last Name is empty"
            _uiState.update {
                it.copy(
                    userFormErrors = it.userFormErrors.copy(
                        isLastNameError = true
                    )
                )
            }
        }
        if(errors != "") {
            val toast = Toast.makeText(context, errors, Toast.LENGTH_LONG)
            val view = toast.view
            view!!.background.setColorFilter(md_theme_light_error.toArgb(), PorterDuff.Mode.SRC_IN)
            val text = view!!.findViewById<TextView>(message)
            text.setTextColor(Color.BLACK)
            toast.show()
            return false
        }else{
            _uiState.update { uiState ->
                val updatedUsers = uiState.users.map {
                    if (it.id == user.id) {
                        // Ažuriramo samo određene vrijednosti korisnika
                        it.copy(
                            firstName = this.uiState.value.user.firstName,
                            lastName = this.uiState.value.user.lastName,
                            birthDate = this.uiState.value.user.birthDate,
                            image = this.uiState.value.user.image
                        )
                    } else {
                        it
                    }
                }
                uiState.copy(users = updatedUsers.toMutableList())
            }

            /*
            val users = LocalUserDataProvider.getUsersData()
            val userToUpdate = users.find { it.id == user.id  }
            userToUpdate?.apply {
                firstName = uiState.value.user.firstName
                lastName = uiState.value.user.lastName
                birthDate = uiState.value.user.birthDate
                image = uiState.value.user.image
            }*/
            return true
        }
    }

    fun updateCurrentCampSpotType(campSpotType: CampSpotNavigationType){
        _uiState.update {
            it.copy(
                currentlySelectedNavType = campSpotType
            )
        }
    }
    fun updateCurrentlySelectedCampSpot(campSpot: CampSpot){
        _uiState.update {
            it.copy(
                currentlySelectedCampSpot = campSpot
            )
        }
    }
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

    fun updateBottomNavigationVisibility(visible : Boolean){
        _uiState.update {
            it.copy(
                isBottomNavigationVisible = visible
            )
        }
    }
    fun updateCurrentCampSpot(campSpot: CampSpot){
        _uiState.update {
            it.copy(
                campSpotForm = campSpot
            )
        }
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