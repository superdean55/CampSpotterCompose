package hr.ferit.dejanmihic.campspottercompose.ui

import android.Manifest
import android.R.id.message
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.LocationManager
import android.net.Uri
import android.os.LocaleList
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.network.CampSpotsRepository
import hr.ferit.dejanmihic.campspottercompose.data.network.SingleUserRepository
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.CampSpotFormErrors
import hr.ferit.dejanmihic.campspottercompose.model.LocationDetails
import hr.ferit.dejanmihic.campspottercompose.model.Message
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.model.UserFormErrors
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotNavigationType
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotType
import hr.ferit.dejanmihic.campspottercompose.ui.screens.dateToString
import hr.ferit.dejanmihic.campspottercompose.ui.theme.md_theme_light_error
import hr.ferit.dejanmihic.campspottercompose.ui.utils.CampSpotFormMode
import hr.ferit.dejanmihic.campspottercompose.ui.utils.languages
import hr.ferit.dejanmihic.campspottercompose.ui.utils.localDateToString
import hr.ferit.dejanmihic.campspottercompose.ui.utils.stringToLocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale


class CampSpotterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CampSpotterUiState())
    val uiState: StateFlow<CampSpotterUiState> = _uiState
    private val TAG = "CAMP_SPOTTER_VIEW_MODEL"

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (lo in p0.locations) {
                var locationDetails = LocationDetails(lo.latitude.toString(), lo.longitude.toString()).toMap().toMutableMap()
                _uiState.update {
                    it.copy(
                        campSpotForm = it.campSpotForm.copy(
                            locationDetails = locationDetails
                        )
                    )
                }
                stopLocationUpdates()
            }
        }
    }
    fun resetUiToInitialState(){
        _uiState.update {
            it.copy(
                currentlySelectedCampSpot = LocalCampSpotDataProvider.defaultCampSpot,
                campSpotForm = LocalCampSpotDataProvider.defaultCampSpot,
                campSpotImageUri = Uri.EMPTY,
                campSpotFormErrors = CampSpotFormErrors(),
                user = LocalUserDataProvider.defaultUser,
                userImageUri = Uri.EMPTY,
                userFormErrors = UserFormErrors(),
                currentlySelectedNavType = CampSpotNavigationType.ALL_CAMP_SPOTS,
                isBottomNavigationVisible = true,
                isTopAppBarUserImageVisible = true
            )
        }
        Log.d(TAG, "RESET TO INITIAL STATE")
    }
    fun updateUri(uri: Uri?, context: Context){
        _uiState.update {
            it.copy(
                campSpotImageUri = uri ?: Uri.EMPTY,
                campSpotFormErrors = it.campSpotFormErrors.copy(
                    isImageUriError = false
                )
            )
        }
        startLocationUpdates(context)
    }
    fun startLocationUpdates(
        context: Context,
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
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!isLocationEnabled) {
                updateLocationDialogVisibility(true)
            }
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
    fun updateLocationDialogVisibility(visible: Boolean){
        _uiState.update {
            it.copy(
                isLocationDialogVisible = visible
            )
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
    fun updateAdditionalOptionsVisibility(){
        _uiState.update {
            it.copy(
                isAdditionalOptionsVisible = !uiState.value.isAdditionalOptionsVisible
            )
        }
    }
    fun updateDeleteAccountDialogVisibility(visible: Boolean){
        _uiState.update {
            it.copy(
                isDeleteDialogVisible = !uiState.value.isDeleteDialogVisible
            )
        }
    }
    fun deleteUserAccount():Boolean{
        Log.d(TAG,"DELETING USER ACCOUNT")
        val myPublishedCampSpots = CampSpotsRepository.repositoryState.value.myCampSpots
        val mySketches = CampSpotsRepository.repositoryState.value.myCampSpotSketches
        val myAccountData = SingleUserRepository.repositoryState.value.user
        viewModelScope.launch(Dispatchers.Default) {
            CampSpotsRepository.deleteAllCurrentUserCampSpotRecords(myPublishedCampSpots, mySketches)
            SingleUserRepository.deleteUserRecords(myAccountData)
        }
        return true
    }

    fun updateSendMassageText(sendMessageText: String){
        _uiState.update {
            it.copy(
                sendMessageText = sendMessageText
            )
        }
    }
    fun removeMessage(message: Message){
        Log.d(TAG,"REMOVE MESSAGE: $message")
        val campSpot = uiState.value.currentlySelectedCampSpot
        if(campSpot != null && message != null) {
            if (message.id != null && message.id != "" && campSpot.id != null && campSpot.id != "") {
                CampSpotsRepository.updateMessage(message, campSpot)
            }
        }
    }
    fun addMessageToDb(context: Context){
        Log.d(TAG,"CURRENT CAMP SPOT ID: ${uiState.value.currentlySelectedCampSpot.id}")
        Log.d(TAG, "BUILDING MESSAGE\nmessage: ${uiState.value.sendMessageText}")
        Log.d(TAG,"userId: ${FirebaseAuth.getInstance().currentUser?.uid}")
        val campSpot = uiState.value.currentlySelectedCampSpot
        val messageText = uiState.value.sendMessageText
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null && messageText.isNotEmpty()) {
            resetSendMessageText()
            CampSpotsRepository.addMessageToCampSpot(campSpot, messageText, userId)
        }else{
            toastMessage("MESSAGE IS EMPTY", context)
        }
    }
    fun resetSendMessageText(){
        _uiState.update {
            it.copy(
                sendMessageText = ""
            )
        }
    }
    fun updateTopAppBarUserImageVisibility(visible: Boolean){
        _uiState.update {
            it.copy(
                isTopAppBarUserImageVisible = visible
            )
        }
    }
    fun updateCampSpotForm(campSpot: CampSpot){
        _uiState.update {
            it.copy(
                campSpotForm = campSpot,
                campSpotImageUri = Uri.EMPTY
            )
        }
        Log.d(TAG,"CAMP_SPOT_FORM_VALUES: ${uiState.value.campSpotForm}")
    }
    fun updatePickedStartDate(date: LocalDate){
        _uiState.update {
            it.copy(
                campSpotForm = it.campSpotForm.copy(
                    startEventDate = localDateToString(date),
                    endEventDate = localDateToString(date)
                )
            )
        }
    }

    fun updatePickedEndDate(endEventDate: LocalDate, context: Context){
        val startEventDate = stringToLocalDate(_uiState.value.campSpotForm.startEventDate!!)
        if(startEventDate > endEventDate){
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
                    endEventDate = localDateToString(endEventDate)
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
        if(!isImageUriNotEmpty(uiState.value.campSpotImageUri) && uiState.value.campSpotForm.imageUrl == ""){
            errors += "You need teak a picture\n"
            _uiState.update {
                it.copy(
                    campSpotFormErrors = it.campSpotFormErrors.copy(
                        isImageUriError = true
                    )
                )
            }
        }
        if(!isTextInputNotEmpty(uiState.value.campSpotForm.numberOfPeople!!)){
            errors += "Enter number of People\n"
            _uiState.update {
                it.copy(
                    campSpotFormErrors = it.campSpotFormErrors.copy(
                        isNumberOfPeopleError = true
                    )
                )
            }
        }
        if(!isTextInputNotEmpty(uiState.value.campSpotForm.title!!)){
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
            toastMessage(errors, context)
            return false
        }else {
            return true
        }
    }
    fun saveCampSpot(context: Context): Boolean {
        if (isValidCampSpotFormData(context)) {
            println("CAMP_SPOT_AFTER_VALIDATION")
            println(uiState.value.campSpotForm)
            /*
            _uiState.update { uiState ->
                val updatedCampSpot = uiState.campSpots.map {
                    if (it.id == this.uiState.value.campSpotForm.id) {
                        it.copy(
                            imageUrl = this.uiState.value.campSpotForm.imageUrl,
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
            }*/
            return true
        }
        return false
    }

    fun deleteCampSpotFromDb(campSpot: CampSpot){
        CampSpotsRepository.deleteCampSpotRecord(campSpot)
    }
    fun addAndUpdateCampSpot(campSpotType: String, context: Context, campSpotFormMode: CampSpotFormMode, isTransfer: Boolean = false) :Boolean{
        if(isValidCampSpotFormData(context)){
            Log.d(TAG,"IS USER ALREADY ADDED SPOT IN RANGE =  ${isUserAlreadyAddedCampSpotInThatDateRange()}")
            Log.d(TAG,"CAMP SPOT BEFORE ADDING USER DATA: ${uiState.value.campSpotForm}")

            if(campSpotFormMode == CampSpotFormMode.Add) {
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                _uiState.update {
                    it.copy(
                        campSpotForm = it.campSpotForm.copy(
                            userId = currentUserId,
                            campSpotType = campSpotType
                        )
                    )
                }
            }
            if(isTransfer){
                _uiState.update {
                    it.copy(
                        campSpotForm = it.campSpotForm.copy(
                            campSpotType = CampSpotType.Published.text
                        )
                    )
                }
            }
            var imageBitmap: Bitmap? = null
            var imageName: String? = null
            if(uiState.value.campSpotImageUri != Uri.EMPTY) {
                imageBitmap = getBitmapFromImageUri(context, uiState.value.campSpotImageUri)
                imageName = generateFileNameForBitmap()
            }
            Log.d(TAG, "campSpotType: ${campSpotType}\nmode: ${campSpotFormMode.mode}\ncamSpotForm: ${uiState.value.campSpotForm}")
            Log.d(TAG,"imageURI: ${uiState.value.campSpotImageUri}\nimageBitmap: $imageBitmap \nimageName: $imageName")
            CampSpotsRepository.addCampSpotInDB(uiState.value.campSpotForm, imageBitmap, imageName, isTransfer, context)
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
        if(imageUri != Uri.EMPTY){
            return true
        }
        return false
    }

    private fun isGpsLocationNotEmpty(locationDetails: MutableMap<String, String?>) :Boolean{
        if(locationDetails["latitude"] == "" || locationDetails["longitude"] == ""){
            return false
        }
        return true
    }
    private fun isUserAlreadyAddedCampSpotInThatDateRange(): Boolean{
        val currentUserCampSpots = CampSpotsRepository.repositoryState.value.myCampSpots
        val campSpotFormData = uiState.value.campSpotForm
        val newStartEventDate = stringToLocalDate(campSpotFormData.startEventDate!!)
        val newEndEventDate = stringToLocalDate(campSpotFormData.endEventDate!!)
        if (currentUserCampSpots.isNotEmpty()) {
            for (campSpot in currentUserCampSpots) {
                val startEventDate = stringToLocalDate(campSpot.startEventDate!!)
                val endEventDate = stringToLocalDate(campSpot.endEventDate!!)
                Log.d(TAG,"OLD START EVENT DATE = $startEventDate\nNEW START EVENT DATE = $newStartEventDate\n" +
                        "OLD END EVENT DATE = $endEventDate\nNEW END EVENT DATE = $newEndEventDate")
                Log.d(TAG,"START EVENT DATE > NEW END EVENT DATE ${startEventDate > newEndEventDate}\n" +
                        "END EVENT DATE > NEW START EVENT DATE ${endEventDate < newStartEventDate}")
                if (!(startEventDate > newEndEventDate || endEventDate < newStartEventDate)){
                    return true
                }
            }
        }
        return false
    }

    fun updateBirthDate(date: LocalDate){
        _uiState.update {
            it.copy(
                user = it.user.copy(
                    birthDate = localDateToString(date)
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
    fun updateEditUserForm(user: User){
        _uiState.update {
            it.copy(
                user = user,
                userImageUri = Uri.EMPTY
            )
        }
    }
    fun updateUserImageUri(imageUri: Uri?){
        _uiState.update {
            it.copy(
                userImageUri = imageUri ?: Uri.EMPTY
            )
        }
    }
    fun saveEditedUser(user: User,context: Context): Boolean{
        var errors = ""
        if(!isTextInputNotEmpty(uiState.value.user.firstName!!)){
            errors += "First Name is empty\n"
            _uiState.update {
                it.copy(
                    userFormErrors = it.userFormErrors.copy(
                        isFirstNameError = true
                    )
                )
            }
        }
        if(!isTextInputNotEmpty(uiState.value.user.lastName!!)){
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
            println("SAVING_USER_CHECK")
            println(uiState.value.user)
            viewModelScope.launch {
                try {
                    if(uiState.value.userImageUri != Uri.EMPTY){
                        val imageName = generateFileNameForBitmap()
                        val imageBitmap = getBitmapFromImageUri(context,uiState.value.userImageUri)
                        SingleUserRepository.updateUser(user,imageBitmap,imageName)
                    }else {
                        SingleUserRepository.updateUser(user)
                    }
                } catch (e: Exception) {
                    println("UPDATE_USER_EXCEPTION: ${e.message}")
                }
            }

            /*_uiState.update { uiState ->
                val updatedUsers = uiState.users.map {
                    if (it.uid == user.uid) {

                        it.copy(
                            firstName = this.uiState.value.user.firstName,
                            lastName = this.uiState.value.user.lastName,
                            birthDate = this.uiState.value.user.birthDate,
                            imageUrl = this.uiState.value.user.imageUrl
                        )
                    } else {
                        it
                    }
                }
                uiState.copy(users = updatedUsers.toMutableList())
            }
            */
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

    fun updateBottomNavigationVisibility(visible : Boolean){
        _uiState.update {
            it.copy(
                isBottomNavigationVisible = visible
            )
        }
    }
    fun resetCampSpotValues(){
        _uiState.update {
            it.copy(
                campSpotForm = LocalCampSpotDataProvider.defaultCampSpot,
                campSpotFormErrors = CampSpotFormErrors(),
                campSpotImageUri = Uri.EMPTY
            )
        }
    }

    private fun getBitmapFromImageUri(context: Context, imageUri: Uri): Bitmap?{
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            if(bitmap != null){
                return resizeImage(bitmap)
            }
            toastMessage("CAPTURED PHOTO BITMAP IS EMPTY",context)
        } catch (e: Exception) {
            toastMessage("CONVERTING IMAGE ERROR",context)
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }

    private fun resizeImage(bitmap: Bitmap): Bitmap {
        val width = bitmap.width.toFloat()
        val height = bitmap.height.toFloat()
        var aspectRatio: Float
        Log.d(TAG, "IMAGE RESOLUTION\nwidth = $width" +
                "\nheight = $height")
        var scaledWidth: Int
        var scaledHeight: Int

        if(width > height) {
            aspectRatio = height / width
            scaledWidth = 400
            scaledHeight = (scaledWidth * aspectRatio).toInt()
        }else{
            aspectRatio = width / height
            scaledHeight = 400
            scaledWidth = (scaledHeight * aspectRatio).toInt()
        }
        Log.d(TAG, "IMAGE SCALE \nscaled width = $scaledWidth" +
                "\nscaled height = $scaledHeight\naspectRatio = $aspectRatio")
        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
    }
    private fun generateFileNameForBitmap(): String {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "bitmap_$timeStamp.jpg"
    }
    private fun toastMessage(message: String, context: Context){
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG,
        ).show()
    }
    private fun saveLanguagePreference(context: Context, languageCode: String) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("selected_language", languageCode)
            apply()
        }
    }
    fun loadLanguagePreference(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("selected_language", "en") ?: "en"
    }
    fun setLocale(context: Context, languageCode: String) {
        Log.d(TAG, "language code = $languageCode")

        var selectedLanguage = getLanguageName(languages, languageCode)
        Log.d(TAG, "selected language = $selectedLanguage")
        _uiState.update {
            it.copy(
                selectedLanguage = selectedLanguage
            )
        }
        Log.d(TAG, "selected language after update = ${_uiState.value.selectedLanguage}")
        context.resources.apply {
            val locale = Locale(languageCode)
            val config = Configuration(configuration)

            context.createConfigurationContext(configuration)
            Locale.setDefault(locale)
            config.setLocale(locale)
            context.resources.updateConfiguration(config, displayMetrics)
        }
        /*val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocales(LocaleList(locale))
        context.resources.updateConfiguration(config, context.resources.displayMetrics)*/

        saveLanguagePreference(context, languageCode)

    }
    private fun getLanguageName(map: Map<String, String>, target: String): String {
        Log.d(TAG, "language code = $target")
        for (key in map.keys)
        {
            if (target == map[key]) {
                return key
            }
        }
        Log.d(TAG, "language code not found")
        return "English"
    }
}