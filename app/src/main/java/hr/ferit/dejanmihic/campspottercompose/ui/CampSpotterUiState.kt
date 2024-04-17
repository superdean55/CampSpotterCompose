package hr.ferit.dejanmihic.campspottercompose.ui

import hr.ferit.dejanmihic.campspottercompose.data.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.CampSpotForm
import hr.ferit.dejanmihic.campspottercompose.model.EditUserForm
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.model.UserFormErrors
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotType

data class CampSpotterUiState(
    val campSpot: CampSpotForm = LocalCampSpotDataProvider.DefaultCampSpot,
    val user: User = LocalUserDataProvider.getUsersData()[0],
    val userFormErrors: UserFormErrors = UserFormErrors(),
    val currentlySelectedNavType: CampSpotType = CampSpotType.ALL_CAMP_SPOTS
)
