package hr.ferit.dejanmihic.campspottercompose.data.network

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.privateData.FIREBASE_REALTIME_DB_URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class UsersRepositoryState(
    val isUsersDataRetrieved: Boolean = false,
    val users: MutableList<User> = mutableListOf()
)


object UsersRepository {
    private const val TAG = "USERS REPOSITORY"
    private val _repositoryState = MutableStateFlow(UsersRepositoryState())
    val repositoryState: StateFlow<UsersRepositoryState> = _repositoryState

    private val database = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DB_URL)
    private val usersRef = database.getReference("users")
    private var usersListener: ValueEventListener? = null

    fun resetRepositoryToInitialState(){
        _repositoryState.update {
            it.copy(
                isUsersDataRetrieved = false,
                users = mutableListOf()
            )
        }
        if (usersListener!= null){
            usersRef.removeEventListener(usersListener!!)
            Log.d(TAG,"USERS_EVENT_LISTENER_REMOVED")
        }

    }

    fun getUsers(){
        Log.d(TAG,"GET_USERS")
        usersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usersList = mutableListOf<User>()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    user?.let { usersList.add(it) }
                }
                _repositoryState.update {
                    it.copy(
                        users = usersList,
                        isUsersDataRetrieved = true
                    )
                }
                println("USERS_ARE_IN_REPOSITORY")
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("RETRIEVING_USERS_FROM_DB_ERROR: ${databaseError.message}")
            }
        }
        usersRef.addValueEventListener(usersListener as ValueEventListener)
    }

}