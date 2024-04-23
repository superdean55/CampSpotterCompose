package hr.ferit.dejanmihic.campspottercompose.ui.utils

import androidx.annotation.StringRes
import hr.ferit.dejanmihic.campspottercompose.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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