package hr.ferit.dejanmihic.campspottercompose.ui.utils

import androidx.annotation.StringRes
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.model.Message
import hr.ferit.dejanmihic.campspottercompose.ui.screens.dateToString
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

fun stringToLocalDate(dateString: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
    return LocalDate.parse(dateString, formatter)
}

fun localDateToString(localDate: LocalDate): String{
    return DateTimeFormatter.ofPattern("dd.MM.yyyy.").format(localDate)
}

sealed class CampSpotFormMode(
    val mode: String,
    @StringRes val leftButtonLabelId: Int,
    @StringRes val rightButtonLabelId: Int
){
    object Add : CampSpotFormMode(
        mode = "ADD",
        leftButtonLabelId = R.string.camp_spot_edit_label_save_sketch,
        rightButtonLabelId = R.string.camp_spot_edit_label_publish
    )
    object Update: CampSpotFormMode(
        mode = "UPDATE",
        leftButtonLabelId = R.string.camp_spot_add_label_update_sketch,
        rightButtonLabelId = R.string.camp_spot_add_label_save
    )
}

fun mapToObject(messages: MutableMap<String,Any?>): List<Message>{
    val messageList = mutableListOf<Message>()
    messages.forEach { (key, value) ->
        if (value != null) {
            val messageMap = value as Map<String, String?>
            val message = Message(
                id = messageMap["id"],
                userId = messageMap["userId"],
                message = messageMap["message"],
                postDate = messageMap["postDate"],
                status = messageMap["status"]
            )
            messageList.add(message)
        }
    }
    messageList.sortBy { it.id }
    return messageList
}
fun <T>dataToString(data: T) :String{
    val text = when(data){
        is LocalDate -> localDateToString(data)
        is Date -> dateToString(data)
        is Int -> data.toString()
        is Float -> data.toString()
        is Double -> data.toString()
        is String -> data
        else -> "unknown"
    }
    return text
}
sealed class MessageStatus(val text: String){
    object Deleted : MessageStatus(text = "DELETED")
    object Viral : MessageStatus(text = "VIRAL")
}