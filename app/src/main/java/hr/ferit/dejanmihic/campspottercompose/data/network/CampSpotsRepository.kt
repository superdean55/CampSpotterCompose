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
import hr.ferit.dejanmihic.campspottercompose.privateData.FIREBASE_REALTIME_DB_URL
import hr.ferit.dejanmihic.campspottercompose.privateData.FIREBASE_STORAGE_URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.io.ByteArrayOutputStream

data class CampSpotsRepositoryState(
    val campSpots: MutableList<CampSpot> = mutableListOf(),
    val myCampSpots: MutableList<CampSpot> = mutableListOf(),
    val myCampSpotSketches: MutableList<CampSpot> = mutableListOf()
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
    }

    fun getMyCampSpotSketches(){
        Log.d(TAG,"GET_MY_CAMP_SPOT_SKETCHES")
        myCampSpotSketchesListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val campSpot = dataSnapshot.getValue<CampSpot>()
                if (campSpot?.userId == FirebaseAuth.getInstance().currentUser?.uid) {
                    println(campSpot)
                    var campSpots = repositoryState.value.myCampSpotSketches.toMutableList()
                    campSpot?.let {
                        campSpots.add(it)
                    }
                    _repositoryState.update { repoState ->
                        repoState.copy(
                            myCampSpotSketches = campSpots
                        )
                    }
                    Log.d(TAG,"CHILD_ADDED_IN_MY_CAMP_SPOT_SKETCHES_LIST")
                }

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val campSpot = dataSnapshot.getValue<CampSpot>()
                println("CHILD_CHANGED_IN_LIST")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                println("CHILD_REMOVED_FROM_LIST")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
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
                    _repositoryState.update { repoState ->
                        repoState.copy(
                            myCampSpots = myCampSpots,
                            campSpots = campSpots
                        )
                    }
                    println("CHILD_ADDED_IN_MY_PUBLISHED_LIST")
                }else{
                    val campSpots = repositoryState.value.campSpots.toMutableList()
                    campSpot?.let {
                        campSpots.add(it)
                    }
                    _repositoryState.update { repoState ->
                        repoState.copy(
                            campSpots = campSpots
                        )
                    }

                    println("CHILD_ADDED_IN_CAMP_SPOTS_LIST")
                }

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val campSpot = dataSnapshot.getValue<CampSpot>()
                println("CHILD_CHANGED_IN_LIST")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                println("CHILD_REMOVED_FROM_LIST")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
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

    fun addCampSpotInDB(campSpot: CampSpot, bitmap: Bitmap, imageName: String, isSketch: Boolean, context: Context){
        var path = campSpotsPath
        if (isSketch) {
            path = sketchesPath
        }
        val imagePath = "$campSpotImages/$imageName"

        val pathRef = storage.reference.child(imagePath)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val data = outputStream.toByteArray()

        pathRef.putBytes(data)
            .addOnSuccessListener {
                println("CAMP_SPOT_IMAGE_IS_STORED_IN_STORAGE")
                getImageUrl(campSpot, imageName, imagePath, path, context)
            }
            .addOnFailureListener {
                println("CAMP_SPOT_IMAGE_STORAGE_ERROR")
            }
    }

    private fun getImageUrl(campSpot: CampSpot,imageName: String, imagePath: String, path: String,context: Context){

        val pathReference = storage.reference.child(imagePath)

        pathReference.downloadUrl
            .addOnSuccessListener { uri ->
                pushCampSpotData(campSpot, imageName, uri.toString(), path, context)
            }.addOnFailureListener { exception ->
                println("IMAGE_URI_EXCEPTION: ${exception.message}")
            }
    }

    private fun pushCampSpotData(campSpot: CampSpot, imageName: String, imageUrl: String, path: String, context: Context){
        val id = database.reference.child(path).push().key
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
                locationDetails = campSpot.locationDetails
            )
            val postValues = campSpot.toMap()

            val childUpdates = hashMapOf<String, Any>(
                "$path/$id" to postValues
            )
            database.reference.updateChildren(childUpdates)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        toastMessage("CAMP_SPOT_FULLY_ADDED", context)
                    }
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