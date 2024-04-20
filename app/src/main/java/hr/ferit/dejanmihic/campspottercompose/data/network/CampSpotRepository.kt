package hr.ferit.dejanmihic.campspottercompose.data.network

import com.google.firebase.database.FirebaseDatabase

class CampSpotNetworkRepository{
    private val database = FirebaseDatabase.getInstance("https://camp-spotter-compose-default-rtdb.europe-west1.firebasedatabase.app")
}