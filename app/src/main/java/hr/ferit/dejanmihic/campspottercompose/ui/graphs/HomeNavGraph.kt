package hr.ferit.dejanmihic.campspottercompose.ui.graphs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotForm
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotNavigationType
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotType
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotsList
import hr.ferit.dejanmihic.campspottercompose.ui.screens.DetailCampSpotCard
import hr.ferit.dejanmihic.campspottercompose.ui.screens.DetailProfileCard
import hr.ferit.dejanmihic.campspottercompose.ui.screens.EditProfileCard

@Composable
fun HomeNavGraph(
    onLogOutClicked: () -> Unit,
    navController: NavHostController,
    campSpotterViewModel: CampSpotterViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by campSpotterViewModel.uiState.collectAsState()
    val context = LocalContext.current
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreen.BottomNavigation.route,
        modifier = modifier
    ) {
        composable(route = HomeScreen.BottomNavigation.route){
            when (uiState.currentlySelectedNavType) {
                CampSpotNavigationType.ALL_CAMP_SPOTS -> {
                    CampSpotsList(
                        campSpots = uiState.campSpots.filter { it.campSpotType == CampSpotType.PUBLISHED },
                        users = uiState.users,
                        onCampSpotClick = {
                            campSpotterViewModel.updateBottomNavigationVisibility(false)
                            campSpotterViewModel.updateCurrentlySelectedCampSpot(it)
                            navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                        }
                    )
                }
                CampSpotNavigationType.MY_CAMP_SPOTS -> {
                    CampSpotsList(
                        campSpots = uiState.campSpots.filter { it.campSpotType == CampSpotType.PUBLISHED && it.userId == LocalUserDataProvider.defaultUser.id },
                        users = uiState.users,
                        onCampSpotClick = {
                            campSpotterViewModel.updateBottomNavigationVisibility(false)
                            campSpotterViewModel.updateCurrentlySelectedCampSpot(it)
                            navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                        }
                    )
                }
                CampSpotNavigationType.SKETCHES -> {
                    CampSpotsList(
                        campSpots = uiState.campSpots.filter { it.campSpotType == CampSpotType.SKETCH && it.userId == LocalUserDataProvider.defaultUser.id },
                        users = uiState.users,
                        onCampSpotClick = {
                            campSpotterViewModel.updateBottomNavigationVisibility(false)
                            campSpotterViewModel.updateCurrentlySelectedCampSpot(it)
                            navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                        }
                    )
                }
            }

        }
        composable(route = HomeScreen.AddCampSpot.route){
            CampSpotForm(
                campSpot = uiState.campSpotForm,
                campSpotFormErrors = uiState.campSpotFormErrors,
                onStartDateSelected = { campSpotterViewModel.updatePickedStartDate(it) },
                onEndDateSelected = { campSpotterViewModel.updatePickedEndDate(it, context) },
                onTitleChanged = { campSpotterViewModel.updateCampSpotTitle(it) },
                onDescriptionChanged = { campSpotterViewModel.updateCampSpotDescription(it) },
                onNumberOfPeopleChanged = { campSpotterViewModel.updateCampSpotNumberOfPeople(it, context) },
                onSaveSketchClicked = {
                    if(campSpotterViewModel.addCampSpot(context)){
                        navController.popBackStack()
                    }
                },
                onPublishClicked = {},
                viewModel = campSpotterViewModel
            )
        }
        navigation(
            route = Graph.CAMP_SPOT_DETAILS,
            startDestination = CampSpotDetailScreen.CampSpotDetails.route
        ){
            composable(route = CampSpotDetailScreen.CampSpotDetails.route){
                DetailCampSpotCard(
                    campSpot = uiState.campSpots.find { it.id == uiState.currentlySelectedCampSpot.id } ?: LocalCampSpotDataProvider.DefaultCampSpot,
                    user = uiState.users.find { it.id == uiState.currentlySelectedCampSpot.userId } ?: LocalUserDataProvider.defaultUser,
                    onEditClicked = {
                        campSpotterViewModel.updateCampSpotForm(it)
                        navController.navigate(route = CampSpotDetailScreen.EditCampSpot.route)
                                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            composable(route = CampSpotDetailScreen.EditCampSpot.route){
                CampSpotForm(
                    campSpot = uiState.campSpotForm,
                    campSpotFormErrors = uiState.campSpotFormErrors,
                    onStartDateSelected = { campSpotterViewModel.updatePickedStartDate(it) },
                    onEndDateSelected = { campSpotterViewModel.updatePickedEndDate(it, context) },
                    onTitleChanged = { campSpotterViewModel.updateCampSpotTitle(it) },
                    onDescriptionChanged = { campSpotterViewModel.updateCampSpotDescription(it) },
                    onNumberOfPeopleChanged = { campSpotterViewModel.updateCampSpotNumberOfPeople(it, context) },
                    onSaveSketchClicked = {
                        if(campSpotterViewModel.saveCampSpot(context)){
                            navController.popBackStack(route = Graph.CAMP_SPOT_DETAILS, inclusive = true)
                            navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                        }
                                          },
                    onPublishClicked = {},
                    viewModel = campSpotterViewModel
                )
            }
        }
        navigation(
            route = Graph.USER_DETAILS,
            startDestination = UserDetailsScreen.UserDetails.route
        ) {
            composable(route = UserDetailsScreen.UserDetails.route){
                DetailProfileCard(
                    user = uiState.user,
                    onLogOutClicked = onLogOutClicked,
                    onEditClicked = { navController.navigate(route = UserDetailsScreen.EditUser.route)}
                )
            }
            composable(route = UserDetailsScreen.EditUser.route){
                EditProfileCard(
                    user = uiState.user,
                    userFormErrors = uiState.userFormErrors,
                    onResultUpdateImage = { campSpotterViewModel.updateUserImageUri(it) },
                    onBirthDateSelected = { campSpotterViewModel.updateBirthDate(it) },
                    onFirstNameChanged = { campSpotterViewModel.updateFirstName(it) },
                    onLastNameChanged = { campSpotterViewModel.updateLastName(it) },
                    onSaveClicked = {
                        if(campSpotterViewModel.saveEditedUser(it,context)){
                                navController.popBackStack(route = Graph.USER_DETAILS, inclusive = true)
                                navController.navigate(route = Graph.USER_DETAILS)
                        }
                    }
                )
            }
        }

    }
}

sealed class HomeScreen(val route: String){
    object BottomNavigation : HomeScreen(route = "BOTTOM_NAVIGATION")
    object AddCampSpot : HomeScreen(route = "ADD_CAMP_SPOT")
}

sealed class UserDetailsScreen(val route: String) {
    object UserDetails : UserDetailsScreen(route = "USER_DETAILS_")
    object EditUser : UserDetailsScreen(route = "EDIT_USER")
}

sealed class CampSpotDetailScreen(val route: String){
    object CampSpotDetails : CampSpotDetailScreen(route = "CAMP_SPOT_DETAILS_")
    object EditCampSpot : CampSpotDetailScreen(route = "EDIT_CAMP_SPOT")
}