package hr.ferit.dejanmihic.campspottercompose.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun stringToLocalDate(dateString: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
    return LocalDate.parse(dateString, formatter)
}