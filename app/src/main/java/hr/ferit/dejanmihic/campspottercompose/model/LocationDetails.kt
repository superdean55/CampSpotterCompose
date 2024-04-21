package hr.ferit.dejanmihic.campspottercompose.model

import com.google.firebase.database.Exclude

data class LocationDetails(
    val latitude: String? = null,
    val longitude: String? = null
){
    @Exclude
    fun toMap(): Map<String, String?> {
        return mapOf(
            "latitude" to latitude,
            "longitude" to longitude,
        )
    }
}
