package hr.ferit.dejanmihic.campspottercompose.model

import android.net.Uri
import java.time.LocalDate

data class CampSpot(
    val title: String,
    val description: String,
    val locationDetails: LocationDetails,
    val numberOfPeople: String,
    val startEventDate: LocalDate,
    val endEventDate: LocalDate,
    val imageUri: Uri,
    val campSpotFormErrors: CampSpotFormErrors
)
