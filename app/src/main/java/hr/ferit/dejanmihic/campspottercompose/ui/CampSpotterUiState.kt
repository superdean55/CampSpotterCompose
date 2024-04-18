package hr.ferit.dejanmihic.campspottercompose.ui

import hr.ferit.dejanmihic.campspottercompose.data.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.CampSpotFormErrors
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.model.UserFormErrors
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotNavigationType

data class CampSpotterUiState(
    val currentlySelectedCampSpot: CampSpot = LocalCampSpotDataProvider.getCampSpots()[0],
    val campSpotForm: CampSpot = LocalCampSpotDataProvider.DefaultCampSpot,
    val campSpotFormErrors: CampSpotFormErrors = CampSpotFormErrors(),
    val user: User = LocalUserDataProvider.getUsersData()[0],
    val userFormErrors: UserFormErrors = UserFormErrors(),
    val currentlySelectedNavType: CampSpotNavigationType = CampSpotNavigationType.ALL_CAMP_SPOTS,
    val isBottomNavigationVisible: Boolean = true,
    val isTopAppBarUserImageHidden: Boolean = true,
    val users: MutableList<User> = LocalUserDataProvider.getUsersData(),
    val campSpots: MutableList<CampSpot> = LocalCampSpotDataProvider.getCampSpots()
)
