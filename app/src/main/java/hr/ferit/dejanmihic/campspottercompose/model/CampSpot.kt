package hr.ferit.dejanmihic.campspottercompose.model


import android.net.Uri
import androidx.annotation.DrawableRes
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotType
import java.time.LocalDate

data class CampSpot(
    val id: Long,
    val imageUri: Uri,
    val userId: Long,
    val title: String,
    val description: String,
    val locationDetails: LocationDetails,
    val numberOfPeople: String,
    val startEventDate: LocalDate,
    val endEventDate: LocalDate,
    val publishDate: LocalDate,
    val campSpotType: CampSpotType
    )
