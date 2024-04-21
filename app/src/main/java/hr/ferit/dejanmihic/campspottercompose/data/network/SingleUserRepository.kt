package hr.ferit.dejanmihic.campspottercompose.data.network

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.screens.localDateToString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.LocalDate

data class SingleUserRepositoryState(
    val isUserDataRetrieved: Boolean = false,
    val user: User? = User(),
)
object SingleUserRepository{
    private val _repositoryState = MutableStateFlow(SingleUserRepositoryState())
    val repositoryState: StateFlow<SingleUserRepositoryState> = _repositoryState

    private val database = FirebaseDatabase.getInstance("https://camp-spotter-compose-default-rtdb.europe-west1.firebasedatabase.app")
    private val storage = Firebase.storage("gs://camp-spotter-compose.appspot.com")

    fun createUserData(email: String, uid: String, context: Context){
        var username = ""
        var creationDate = ""
        if (email.contains('@')) {
            username = email.substringBefore('@')
        }
        creationDate = localDateToString(LocalDate.now())
        val user = User(
            uid = uid,
            imageUrl = "",
            imageName = "",
            username = username,
            firstName = "",
            lastName = "",
            email = email,
            birthDate = "",
            creationDate = creationDate,
        )
        database.reference.child("users").child(uid).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("USER_DATA_ADDED_IN_DB")
                    toastMessage("USER_DATA_ADDED_IN_DB", context)
                } else {
                    println( "ADDING_USER_DATA_ERROR: ${task.exception}")
                    toastMessage("ADDING_USER_DATA_ERROR", context)
                }
            }
    }

    fun getUserDataByUid(uid: String){
        database.reference.child("users").child(uid).get().addOnSuccessListener {
            println("ONE_DATA_RETRIEVING_SUCCESS")
            val user = it.getValue(User::class.java)
            user?.let {
                _repositoryState.update { repositoryState ->
                    repositoryState.copy(
                        user = it
                    )
                }
            }
            _repositoryState.update { repoState ->
                repoState.copy(
                    isUserDataRetrieved = true
                )
            }
            println("RETRIEVED_USER_DATA")
            println(repositoryState.value.user)
        }.addOnFailureListener{
            println("ONE_DATA_RETRIEVING_ERROR")
        }
    }

    fun updateUser(user: User, imageBitmap: Bitmap? = null, imageName: String? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            if (imageBitmap != null && imageName != null){
                uploadUserDataAndImage(user, imageBitmap, imageName)
            }else{
                updateUserData(user)
            }
        }

    }

    private fun updateUserData(user: User){
        if (
            user.uid != null && user.imageUrl != null && user.username != null &&
            user.firstName != null && user.lastName != null && user.email != null &&
            user.birthDate != null && user.creationDate != null && user.imageName != null
        ) {
            val postValues = user.toMap()
            val childUpdates = hashMapOf<String, Any>(
                "/users/${user.uid}" to postValues,
            )
            database.reference.updateChildren(childUpdates)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        println("USER_IS_UPDATED")
                        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                        if (currentUserUid != null){
                            getUserDataByUid(currentUserUid)
                        }
                    }else{
                        println("UPDATE_USER_ERROR")
                    }
                }
        }
    }
    private fun uploadUserDataAndImage(user :User, bitmap: Bitmap, imageName: String) {
        val imagesRef = storage.reference.child("images/$imageName")

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val data = outputStream.toByteArray()

        imagesRef.putBytes(data)
            .addOnSuccessListener {
                println("IMAGE_IS_STORED_IN_STORAGE")
                getImageUrl(user,imageName)
            }
            .addOnFailureListener {
                println("IMAGE_STORAGE_ERROR")
            }
    }

    private fun getImageUrl(user: User, imageName: String){
        println("images/$imageName")
        val pathReference = storage.reference.child("images/$imageName")

        pathReference.downloadUrl
            .addOnSuccessListener { uri ->
                val userUpdate = User(
                    uid = user.uid,
                    imageUrl = uri.toString(),
                    imageName = imageName,
                    username = user.username,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    birthDate = user.birthDate,
                    creationDate = user.creationDate
                )
                if(user.imageName!! != ""){
                    println("CALLING_USER_IMAGE_URL")
                    println(user.imageUrl)

                    deleteOldImageFromDb(user.imageName!!)
                }
                updateUserData(userUpdate)
                println("IMAGE_URI = ${uri.toString()}")
            }.addOnFailureListener { exception ->
                    println("IMAGE_URI_EXCEPTION: ${exception.message}")
            }
    }

    private fun deleteOldImageFromDb(imageName: String){
        val storageRef = storage.reference
        val desertRef = storageRef.child("images/$imageName")
        desertRef.delete().addOnSuccessListener {
            println("OLD_USER_IMAGE_SUCCESSFULLY_DELETED")
        }.addOnFailureListener {
            println("OLD_USER_IMAGE_DELETING_ERROR")
        }
    }
    private fun toastMessage(message: String, context: Context){
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG,
        ).show()
    }

}