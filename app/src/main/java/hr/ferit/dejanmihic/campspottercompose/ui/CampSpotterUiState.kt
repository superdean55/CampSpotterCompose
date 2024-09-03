package hr.ferit.dejanmihic.campspottercompose.ui

import android.net.Uri
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.CampSpotFormErrors
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.model.UserFormErrors
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotNavigationType

data class CampSpotterUiState(
    val currentlySelectedCampSpot: CampSpot = LocalCampSpotDataProvider.getCampSpots()[0],
    val campSpotForm: CampSpot = LocalCampSpotDataProvider.defaultCampSpot,
    val campSpotImageUri: Uri = Uri.EMPTY,
    val campSpotFormErrors: CampSpotFormErrors = CampSpotFormErrors(),

    val user: User = LocalUserDataProvider.defaultUser,
    val userImageUri: Uri = Uri.EMPTY,
    val userFormErrors: UserFormErrors = UserFormErrors(),

    val currentlySelectedNavType: CampSpotNavigationType = CampSpotNavigationType.ALL_CAMP_SPOTS,
    val isBottomNavigationVisible: Boolean = true,
    val isTopAppBarUserImageVisible: Boolean = true,

    val sendMessageText: String = "",

    val isAdditionalOptionsVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,
    val isLocationDialogVisible: Boolean = false,

    val selectedLanguage: String = ""
)
