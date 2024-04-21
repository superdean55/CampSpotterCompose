package hr.ferit.dejanmihic.campspottercompose.model

import com.google.firebase.database.Exclude


data class CampSpot(
    val id: String? = null,
    val imageUrl: String? = null,
    val imageName: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val numberOfPeople: String? = null,
    val startEventDate: String? = null,
    val endEventDate: String? = null,
    val publishDate: String? = null,
    val campSpotType: String? = null,
    val locationDetails: MutableMap<String,String?> = mutableMapOf("latitude" to "", "longitude" to ""),
    ){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "imageUrl" to imageUrl,
            "imageName" to imageName,
            "userId" to userId,
            "title" to title,
            "description" to description,
            "numberOfPeople" to numberOfPeople,
            "startEventDate" to startEventDate,
            "endEventDate" to endEventDate,
            "publishDate" to publishDate,
            "campSpotType" to campSpotType,
            "locationDetails" to locationDetails,
        )
    }
}
