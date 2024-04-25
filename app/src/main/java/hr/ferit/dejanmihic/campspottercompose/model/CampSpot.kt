package hr.ferit.dejanmihic.campspottercompose.model

import com.google.firebase.database.Exclude


data class CampSpot(
    val id: String? = "",
    val imageUrl: String? = "",
    val imageName: String? = "",
    val userId: String? = "",
    val title: String? = "",
    val description: String? = "",
    val numberOfPeople: String? = "",
    val startEventDate: String? = "",
    val endEventDate: String? = "",
    val publishDate: String? = "",
    val campSpotType: String? = "",
    val locationDetails: MutableMap<String,String?> = mutableMapOf("latitude" to "", "longitude" to ""),
    val messages: MutableMap<String, Any?> = mutableMapOf()
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
            "messages" to messages
        )
    }
}
