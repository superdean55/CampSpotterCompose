package hr.ferit.dejanmihic.campspottercompose.data.local

import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.LocationDetails
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotType
import hr.ferit.dejanmihic.campspottercompose.ui.utils.localDateToString
import java.time.LocalDate

object LocalCampSpotDataProvider {
    val DefaultCampSpot = CampSpot(
        id = "",
        imageUrl = "",
        imageName = "",
        userId = "",
        title = "",
        description = "",
        locationDetails = LocationDetails("0","0").toMap().toMutableMap(),
        numberOfPeople = "",
        startEventDate = localDateToString(LocalDate.now()),
        endEventDate = localDateToString(LocalDate.now()),
        publishDate = localDateToString(LocalDate.now()),
        campSpotType = CampSpotType.Sketch.text
    )
    fun getCampSpots(): MutableList<CampSpot>{
        return mutableListOf(
            CampSpot(
                id = "1",
                imageUrl = "",
                imageName = "",
                userId = "1",
                title = "Kamp Borovik",
                description = "Nešto",
                locationDetails = LocationDetails("1.745","4.456").toMap().toMutableMap(),
                numberOfPeople = "2",
                startEventDate = localDateToString(LocalDate.now()),
                endEventDate = localDateToString(LocalDate.now()),
                publishDate = localDateToString(LocalDate.now()),
                campSpotType = CampSpotType.Published.text
            ),
            CampSpot(
                id = "2",
                imageUrl = "",
                imageName = "",
                userId = "2",
                title = "Jošava",
                description = "Zabavljamo se",
                locationDetails = LocationDetails("34.745","45.456").toMap().toMutableMap(),
                numberOfPeople = "12",
                startEventDate = localDateToString(LocalDate.now()),
                endEventDate = localDateToString(LocalDate.now()),
                publishDate = localDateToString(LocalDate.now()),
                campSpotType = CampSpotType.Published.text
            )
        )
    }
}