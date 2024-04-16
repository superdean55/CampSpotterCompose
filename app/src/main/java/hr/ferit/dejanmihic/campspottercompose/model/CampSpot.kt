package hr.ferit.dejanmihic.campspottercompose.model


import androidx.annotation.DrawableRes
import java.time.LocalDate

data class CampSpot(
    val id: Long,
    @DrawableRes val imageId: Int,
    val userId: Long,
    val title: String,
    val description: String,
    val locationDetails: LocationDetails,
    val numberOfPeople: String,
    val startEventDate: LocalDate,
    val endEventDate: LocalDate,
    val publishDate: LocalDate
    )
