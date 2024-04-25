package hr.ferit.dejanmihic.campspottercompose.model
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import hr.ferit.dejanmihic.campspottercompose.ui.utils.MessageStatus

@IgnoreExtraProperties
data class Message(
    val id: String? = "",
    val userId: String? = "",
    val message: String? = "",
    val postDate: String? = "",
    val status: String? = MessageStatus.Viral.text
){
    constructor() : this("","","","", MessageStatus.Viral.text)
    @Exclude
    fun toMap(): Map<String, String?> {
        return mapOf(
                "id" to id,
                "userId" to userId,
                "message" to message,
                "postDate" to postDate,
                "status" to status
            )
    }

}
