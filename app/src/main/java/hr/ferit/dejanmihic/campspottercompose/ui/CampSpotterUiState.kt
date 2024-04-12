package hr.ferit.dejanmihic.campspottercompose.ui

import hr.ferit.dejanmihic.campspottercompose.data.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.EditUserForm

data class CampSpotterUiState(
    val campSpot: CampSpot = LocalCampSpotDataProvider.DefaultCampSpot,
    val user: EditUserForm = LocalUserDataProvider.defaultUser
)
