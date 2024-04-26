package hr.ferit.dejanmihic.campspottercompose.data.network

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.Message
import hr.ferit.dejanmihic.campspottercompose.privateData.FIREBASE_REALTIME_DB_URL
import hr.ferit.dejanmihic.campspottercompose.privateData.FIREBASE_STORAGE_URL
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotType
import hr.ferit.dejanmihic.campspottercompose.ui.utils.MessageStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CampSpotsRepositoryState(
    val campSpots: MutableList<CampSpot> = mutableListOf(),
    val myCampSpots: MutableList<CampSpot> = mutableListOf(),
    val myCampSpotSketches: MutableList<CampSpot> = mutableListOf(),
    val latestChangedCampSpot: CampSpot = CampSpot()
)
object CampSpotsRepository{
    private const val TAG = "CAMP SPOT REPOSITORY"
    private val _repositoryState = MutableStateFlow(CampSpotsRepositoryState())
    val repositoryState: StateFlow<CampSpotsRepositoryState> = _repositoryState
    private const val sketchesPath = "camp_spot_sketches"
    private const val campSpotsPath = "camp_spots"
    private const val campSpotImages = "camp_spot_images"

    private val database = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DB_URL)
    private val storage = Firebase.storage(FIREBASE_STORAGE_URL)

    private val myCampSpotSketchesReference = database.reference.child(sketchesPath)
    private val campSpotsReference = database.reference.child(campSpotsPath)
    private var myCampSpotSketchesListener: ChildEventListener? = null
    private var campSpotsListener: ChildEventListener? = null

    fun resetRepositoryToInitialState(){
        _repositoryState.update {
            it.copy(
                campSpots = mutableListOf(),
                myCampSpots = mutableListOf(),
                myCampSpotSketches = mutableListOf()
            )
        }
        if (myCampSpotSketchesListener != null){
            myCampSpotSketchesReference.removeEventListener(myCampSpotSketchesListener!!)
            Log.d(TAG,"MY_CAMP_SPOT_SKETCHES_LISTENER_REMOVED")
        }
        if (campSpotsListener != null){
            campSpotsReference.removeEventListener(campSpotsListener!!)
            Log.d(TAG,"CAMP_SPOTS_LISTENER_REMOVED")
        }
        Log.d(TAG, "RESET TO INITIAL STATE")
    }
    fun updateLatestChangedCampSpot(campSpot: CampSpot){
        _repositoryState.update {
            it.copy(
                latestChangedCampSpot = campSpot
            )
        }
    }
    fun updateCampSpots(campSpots: MutableList<CampSpot>){
        _repositoryState.update { repoState ->
            repoState.copy(
                campSpots = campSpots
            )
        }
    }
    fun updateMyCampSpots(campSpots: MutableList<CampSpot>){
        _repositoryState.update { repoState ->
            repoState.copy(
                myCampSpots = campSpots
            )
        }
    }
    fun updateMyCampSpotsSketches(campSpots: MutableList<CampSpot>){
        _repositoryState.update { repoState ->
            repoState.copy(
                myCampSpotSketches = campSpots
            )
        }
    }

    fun getMyCampSpotSketches(){
        Log.d(TAG,"GET_MY_CAMP_SPOT_SKETCHES")
        myCampSpotSketchesListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val campSpot = dataSnapshot.getValue<CampSpot>()
                if (campSpot?.userId == FirebaseAuth.getInstance().currentUser?.uid) {
                    var campSpots = repositoryState.value.myCampSpotSketches.toMutableList()
                    campSpot?.let { campSpots.add(it) }
                    updateMyCampSpotsSketches(campSpots)
                    Log.d(TAG,"CHILD_ADDED_IN_MY_CAMP_SPOT_SKETCHES_LIST")
                }
            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val campSpot = dataSnapshot.getValue<CampSpot>()
                campSpot?.let {
                    updateLatestChangedCampSpot(campSpot)
                }
                if (campSpot?.userId == FirebaseAuth.getInstance().currentUser?.uid) {
                    var campSpots = repositoryState.value.myCampSpotSketches.toMutableList()
                    val oldCampSpot = campSpots.find { it.id == campSpot?.id }
                    oldCampSpot?.let { campSpots.remove(it) }
                    campSpot?.let { campSpots.add(it) }
                    updateMyCampSpotsSketches(campSpots)
                }
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val myCampSpotSketches = repositoryState.value.myCampSpotSketches.toMutableList()
                val sketch = myCampSpotSketches.find { it.id == dataSnapshot.key }
                sketch?.let { myCampSpotSketches.remove(it) }
                updateMyCampSpotsSketches(myCampSpotSketches)
                Log.d(TAG,"CHILD_REMOVED_FROM_MY_CAMP_SPOT_SKETCHES_LIST")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + dataSnapshot.key!!)
                val movedComment = dataSnapshot.getValue<CampSpot>()
                val commentKey = dataSnapshot.key
                println("CHILD_MOVED_IN_LIST")
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("DB_ERROR")
            }
        }
        myCampSpotSketchesReference.addChildEventListener(myCampSpotSketchesListener as ChildEventListener)
    }
    fun getPublishedCampSpots(){
        Log.d(TAG,"GET_CAMP_SPOT")
        campSpotsListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val campSpot = dataSnapshot.getValue<CampSpot>()
                if (campSpot?.userId == FirebaseAuth.getInstance().currentUser?.uid) {
                    val myCampSpots = repositoryState.value.myCampSpots.toMutableList()
                    val campSpots = repositoryState.value.campSpots.toMutableList()
                    campSpot?.let {
                        myCampSpots.add(it)
                        campSpots.add(it)
                    }
                    updateMyCampSpots(myCampSpots)
                    updateCampSpots(campSpots)
                    Log.d(TAG,"CHILD ADDED IN MY PUBLISHED LIST\nCHILD ADDED IN CAMP SPOTS LIST")
                }else{
                    val campSpots = repositoryState.value.campSpots.toMutableList()
                    campSpot?.let { campSpots.add(it) }
                    updateCampSpots(campSpots)
                    Log.d(TAG,"CHILD ADDED IN CAMP SPOTS LIST")
                }
            }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val campSpot = dataSnapshot.getValue<CampSpot>()
                campSpot?.let { updateLatestChangedCampSpot(campSpot) }
                if (campSpot?.userId == FirebaseAuth.getInstance().currentUser?.uid) {
                    val myCampSpots = repositoryState.value.myCampSpots.toMutableList()
                    val campSpots = repositoryState.value.campSpots.toMutableList()
                    val myOldCampSpotIndex = myCampSpots.indexOfFirst { it.id == campSpot?.id }
                    val oldCampSpotIndex = campSpots.indexOfFirst { it.id == campSpot?.id }
                    if (campSpot != null) {
                        if (myOldCampSpotIndex != -1) {
                            myCampSpots[myOldCampSpotIndex] = campSpot
                        }
                        if (oldCampSpotIndex != -1) {
                            campSpots[oldCampSpotIndex] = campSpot
                        }
                    }
                    updateMyCampSpots(myCampSpots)
                    updateCampSpots(campSpots)
                    Log.d(TAG,"CHILD CHANGED IN MY PUBLISHED LIST\nCHILD CHANGED IN CAMP SPOTS LIST")
                }else{
                    val campSpots = repositoryState.value.campSpots.toMutableList()
                    val oldCampSpotIndex = campSpots.indexOfFirst { it.id == campSpot?.id }
                    if (campSpot != null) {
                        if (oldCampSpotIndex != -1) {
                            campSpots[oldCampSpotIndex] = campSpot
                        }
                    }
                    updateCampSpots(campSpots)
                    Log.d(TAG,"CHILD CHANGED IN CAMP SPOTS LIST")
                }
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val myCampSpots = repositoryState.value.myCampSpots.toMutableList()
                val campSpots = repositoryState.value.campSpots.toMutableList()
                val myOldCampSpot = myCampSpots.find { it.id == dataSnapshot.key }
                val oldCampSpot = campSpots.find { it.id == dataSnapshot.key }
                myOldCampSpot?.let { myCampSpots.remove(it) }
                oldCampSpot?.let { campSpots.remove(it) }
                updateMyCampSpots(myCampSpots)
                updateCampSpots(campSpots)
                Log.d(TAG,"CHILD REMOVED FROM CAMP SPOTS LIST")
            }
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + dataSnapshot.key!!)
                val movedComment = dataSnapshot.getValue<CampSpot>()
                val commentKey = dataSnapshot.key
                println("CHILD_MOVED_IN_LIST")
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("DB_ERROR")
            }
        }
        campSpotsReference.addChildEventListener(campSpotsListener as ChildEventListener)
    }

    fun addCampSpotInDB(campSpot: CampSpot, bitmap: Bitmap?, imageName: String?, isTransfer: Boolean, context: Context){
        var dataPath = campSpotsPath
        if (!isTransfer) {
            if (campSpot.campSpotType == CampSpotType.Sketch.text) {
                dataPath = sketchesPath
            }
        }
        if(bitmap != null && imageName != null) {
            Log.d(TAG, "SAVE IMAGE TO STORAGE $campSpot")
            saveImageToStorage(campSpot, bitmap, imageName, dataPath, isTransfer, context)
        }else{
            Log.d(TAG, "JUMP TO PUSH CAMP SPOT DATA $campSpot")
            pushCampSpotData(campSpot,campSpot.imageName!!,campSpot.imageUrl!!,dataPath, isTransfer, context)
        }
    }
    private fun saveImageToStorage(campSpot: CampSpot, bitmap: Bitmap, imageName: String, dataPath: String, isTransfer: Boolean, context: Context){
        val imagePath = "$campSpotImages/$imageName"
        val pathRef = storage.reference.child(imagePath)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val data = outputStream.toByteArray()

        pathRef.putBytes(data)
            .addOnSuccessListener {
                Log.d(TAG,"CAMP_SPOT_IMAGE_IS_STORED_IN_STORAGE")
                getImageUrl(campSpot, imageName, imagePath, dataPath, isTransfer, context)
            }
            .addOnFailureListener {
                Log.e(TAG,"CAMP_SPOT_IMAGE_STORAGE_ERROR")
            }
    }
    private fun getImageUrl(campSpot: CampSpot, imageName: String, imagePath: String, dataPath: String, isTransfer: Boolean, context: Context){
        val pathReference = storage.reference.child(imagePath)
        pathReference.downloadUrl
            .addOnSuccessListener { uri ->
                Log.d(TAG,"CAMP_SPOT_IMAGE_URL_SUCCESS: ${uri.toString()}")
                if(campSpot.imageUrl != "" && campSpot.imageName != ""){
                    deleteImageFromStorage(campSpot.imageName!!)
                }
                pushCampSpotData(campSpot, imageName, uri.toString(), dataPath, isTransfer, context)
            }.addOnFailureListener { exception ->
                Log.e(TAG,"IMAGE_URI_EXCEPTION: ${exception.message}")
            }
    }
    fun removeCampSpot(id: String, campSpotType: String){
        val dataPath = if (campSpotType == CampSpotType.Published.text) campSpotsPath else sketchesPath
        database.getReference(dataPath).child(id).removeValue()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d(TAG,"CAMP SPOT DELETED")
                }
            }
    }
    private fun pushCampSpotData(campSpot: CampSpot, imageName: String, imageUrl: String, dataPath: String, isTransfer: Boolean, context: Context){
        Log.d(TAG,"CAMP SPOT IN PUSH FUN: $campSpot")
        if (isTransfer && campSpot.id != ""){
            removeCampSpot(campSpot.id!!, sketchesPath)
        }
        val id = if ( isTransfer || campSpot.id == "" ) { database.reference.child(dataPath).push().key } else { campSpot.id }
        if (id != null) {
            val campSpot = CampSpot(
                id = id,
                imageUrl = imageUrl,
                imageName = imageName,
                userId = campSpot.userId,
                title = campSpot.title,
                description = campSpot.description,
                numberOfPeople = campSpot.numberOfPeople,
                startEventDate = campSpot.startEventDate,
                endEventDate = campSpot.endEventDate,
                publishDate = campSpot.publishDate,
                campSpotType = campSpot.campSpotType,
                locationDetails = campSpot.locationDetails,
                messages = campSpot.messages
            )
            Log.d(TAG,"CAMP_SPOT_TO_ADD_OR_UPDATE $campSpot")
            val postValues = campSpot.toMap()

            val childUpdates = hashMapOf<String, Any>(
                "$dataPath/$id" to postValues
            )
            database.reference.updateChildren(childUpdates)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        toastMessage("CAMP SPOT FULLY ADDED OR UPDATED", context)
                    }
                }
        }
    }

    fun deleteImageFromStorage(imageName: String) {
        val storageRef = storage.reference.child("$campSpotImages/$imageName")
        storageRef.delete().addOnSuccessListener {
            Log.d(TAG,"OLD_CAMP_SPOT_IMAGE_SUCCESSFULLY_DELETED")
        }.addOnFailureListener {
            Log.e(TAG,"OLD_CAMP_SPOT_IMAGE_DELETING_ERROR")
        }
    }
    fun addMessageToCampSpot(campSpot: CampSpot, messageText: String, userId: String){
        val dataPath = if (campSpot.campSpotType == CampSpotType.Published.text) campSpotsPath else sketchesPath
        val postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"))
        val id = database.reference.child(dataPath).push().key
        /*val id = database.reference.child(dataPath).child(campSpot.id!!).child("messages").push().key*/
        if (id != null) {
            val message = Message(
                id = id,
                userId = userId,
                message = messageText,
                postDate = postDate
            )
            val postValues = message.toMap()
            Log.d(TAG, "postValues: $postValues")
            val childUpdates = hashMapOf<String, Any>(
                "$dataPath/${campSpot.id}/messages/$id" to postValues
            )
            database.reference.updateChildren(childUpdates)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Log.d(TAG,"MESSAGE IS POSTED")
                    }
                }
        }

    }
    fun updateMessage(message: Message, campSpot: CampSpot){
        val dataPath = if (campSpot.campSpotType == CampSpotType.Published.text) campSpotsPath else sketchesPath
        val postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm"))
        val updatedMessage = Message(
            id = message.id,
            userId = message.userId,
            message = message.message,
            postDate = postDate,
            status = MessageStatus.Deleted.text
        )
        val postValues = updatedMessage.toMap()
        Log.d(TAG, "postValues: $postValues")
        val childUpdates = hashMapOf<String, Any>(
            "$dataPath/${campSpot.id}/messages/${message.id}" to postValues
        )
        database.reference.updateChildren(childUpdates)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.d(TAG,"MESSAGE IS UPDATED LIKE DELETED")
                }
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