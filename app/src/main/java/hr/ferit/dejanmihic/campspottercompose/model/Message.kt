package hr.ferit.dejanmihic.campspottercompose.model
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val id: String? = "",
    val userId: String? = "",
    val message: String? = "",
    val postDate: String? = ""
){
    constructor() : this("","","","")
    @Exclude
    fun toMap(): Map<String, String?> {
        return mapOf(
                "id" to id,
                "userId" to userId,
                "message" to message,
                "postDate" to postDate
            )
    }

}
