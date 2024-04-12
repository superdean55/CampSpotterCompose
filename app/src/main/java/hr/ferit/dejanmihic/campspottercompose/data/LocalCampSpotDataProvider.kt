package hr.ferit.dejanmihic.campspottercompose.data

import android.net.Uri
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.CampSpotFormErrors
import hr.ferit.dejanmihic.campspottercompose.model.LocationDetails
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalCampSpotDataProvider {
    val DefaultCampSpot = CampSpot(
        title = "",
        description = "",
        locationDetails = LocationDetails(0.toDouble(), 0.toDouble()),
        numberOfPeople = "",
        startEventDate =  LocalDate.now(),
        endEventDate =  LocalDate.now(),
        imageUri = Uri.EMPTY,
        campSpotFormErrors = CampSpotFormErrors()
    )
}