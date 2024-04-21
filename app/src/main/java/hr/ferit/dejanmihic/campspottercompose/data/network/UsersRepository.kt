package hr.ferit.dejanmihic.campspottercompose.data.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import hr.ferit.dejanmihic.campspottercompose.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class UsersRepositoryState(
    val isUsersDataRetrieved: Boolean = false,
    val users: MutableList<User> = mutableListOf()
)
object UsersRepository {
    private val _repositoryState = MutableStateFlow(UsersRepositoryState())
    val repositoryState: StateFlow<UsersRepositoryState> = _repositoryState

    private val database = FirebaseDatabase.getInstance("https://camp-spotter-compose-default-rtdb.europe-west1.firebasedatabase.app")

    private val usersListener = object : ValueEventListener {
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
        }
        override fun onCancelled(databaseError: DatabaseError) {
            println("RETRIEVING_USERS_FROM_DB_ERROR: ${databaseError.message}")
        }
    }
    val usersRef = database.getReference("users").addValueEventListener(usersListener)
}