package hr.ferit.dejanmihic.campspottercompose.data

import android.net.Uri
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.CampSpotFormErrors
import hr.ferit.dejanmihic.campspottercompose.model.LocationDetails
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotForm
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotType
import java.time.LocalDate

object LocalCampSpotDataProvider {
    val DefaultCampSpot = CampSpot(
        id = 4L,
        imageUri = Uri.EMPTY,
        userId = 1L,
        title = "",
        description = "",
        locationDetails = LocationDetails(0.toDouble(),0.toDouble()),
        numberOfPeople = "",
        startEventDate = LocalDate.now(),
        endEventDate = LocalDate.now(),
        publishDate = LocalDate.now(),
        campSpotType = CampSpotType.SKETCH
    )
    fun getCampSpots(): MutableList<CampSpot>{
        return mutableListOf(
            CampSpot(
                id = 1L,
                imageUri = Uri.EMPTY,
                userId = 1L,
                title = "Kamp Borovik",
                description = "Nešto",
                locationDetails = LocationDetails(1.745.toDouble(),4.456.toDouble()),
                numberOfPeople = "2",
                startEventDate = LocalDate.now(),
                endEventDate = LocalDate.now(),
                publishDate = LocalDate.now(),
                campSpotType = CampSpotType.PUBLISHED
            ),
            CampSpot(
                id = 2L,
                imageUri = Uri.EMPTY,
                userId = 2L,
                title = "Jošava",
                description = "Zabavljamo se",
                locationDetails = LocationDetails(34.745.toDouble(),45.456.toDouble()),
                numberOfPeople = "12",
                startEventDate = LocalDate.now(),
                endEventDate = LocalDate.now(),
                publishDate = LocalDate.now(),
                campSpotType = CampSpotType.PUBLISHED
            )
        )
    }
}