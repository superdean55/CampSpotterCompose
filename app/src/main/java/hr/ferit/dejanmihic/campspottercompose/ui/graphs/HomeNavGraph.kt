package hr.ferit.dejanmihic.campspottercompose.ui.graphs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.network.CampSpotsRepository
import hr.ferit.dejanmihic.campspottercompose.data.network.SingleUserRepository
import hr.ferit.dejanmihic.campspottercompose.data.network.UsersRepository
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotForm
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotNavigationType
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotType
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotsList
import hr.ferit.dejanmihic.campspottercompose.ui.screens.DetailCampSpotCard
import hr.ferit.dejanmihic.campspottercompose.ui.screens.DetailProfileCard
import hr.ferit.dejanmihic.campspottercompose.ui.screens.EditProfileCard
import hr.ferit.dejanmihic.campspottercompose.ui.utils.CampSpotFormMode

@Composable
fun HomeNavGraph(
    onLogOutClicked: () -> Unit,
    navController: NavHostController,
    campSpotterViewModel: CampSpotterViewModel,
    modifier: Modifier = Modifier
) {
    val TAG = "HOME NAV GRAPH"
    val uiState by campSpotterViewModel.uiState.collectAsState()
    val singleUserRepositoryState by SingleUserRepository.repositoryState.collectAsState()
    val usersRepositoryState by UsersRepository.repositoryState.collectAsState()
    val campSpotsRepositoryState by CampSpotsRepository.repositoryState.collectAsState()
    val context = LocalContext.current
    if(campSpotsRepositoryState.latestChangedCampSpot.id == uiState.currentlySelectedCampSpot.id){
        campSpotterViewModel.updateCurrentlySelectedCampSpot(campSpotsRepositoryState.latestChangedCampSpot)
    }
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreen.BottomNavigation.route,
        modifier = modifier
    ) {
        composable(route = HomeScreen.BottomNavigation.route){
            when (uiState.currentlySelectedNavType) {
                CampSpotNavigationType.ALL_CAMP_SPOTS -> {
                    when(usersRepositoryState.isUsersDataRetrieved){
                        false -> {
                            LoadingData(modifier = Modifier.fillMaxSize())
                        }
                        true -> {
                            CampSpotsList(
                                campSpots = campSpotsRepositoryState.campSpots,
                                users = usersRepositoryState.users,
                                onCampSpotClick = {
                                    campSpotterViewModel.updateBottomNavigationVisibility(false)
                                    campSpotterViewModel.updateCurrentlySelectedCampSpot(it)
                                    campSpotterViewModel.resetSendMessageText()
                                    navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                                }
                            )
                        }
                    }
                }
                CampSpotNavigationType.MY_CAMP_SPOTS -> {
                    when(usersRepositoryState.isUsersDataRetrieved) {
                        false -> {
                            LoadingData(modifier = Modifier.fillMaxSize())
                        }
                        true -> {
                            CampSpotsList(
                                campSpots = campSpotsRepositoryState.myCampSpots,
                                users = usersRepositoryState.users,
                                onCampSpotClick = {
                                    campSpotterViewModel.updateBottomNavigationVisibility(false)
                                    campSpotterViewModel.updateCurrentlySelectedCampSpot(it)
                                    campSpotterViewModel.resetSendMessageText()
                                    navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                                }
                            )
                        }
                    }
                }
                CampSpotNavigationType.SKETCHES -> {
                    when(usersRepositoryState.isUsersDataRetrieved) {
                        false -> {
                            LoadingData(modifier = Modifier.fillMaxSize())
                        }
                        true -> {
                            CampSpotsList(
                                campSpots = campSpotsRepositoryState.myCampSpotSketches,
                                users = usersRepositoryState.users,
                                onCampSpotClick = {
                                    campSpotterViewModel.updateBottomNavigationVisibility(false)
                                    campSpotterViewModel.updateCurrentlySelectedCampSpot(it)
                                    campSpotterViewModel.resetSendMessageText()
                                    navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                                }
                            )
                        }
                    }
                }
            }

        }
        composable(route = HomeScreen.AddCampSpot.route){
            CampSpotForm(
                campSpot = uiState.campSpotForm,
                campSpotImageUri = uiState.campSpotImageUri,
                campSpotFormErrors = uiState.campSpotFormErrors,
                campSpotFormMode = CampSpotFormMode.Add,
                onStartDateSelected = { campSpotterViewModel.updatePickedStartDate(it) },
                onEndDateSelected = { campSpotterViewModel.updatePickedEndDate(it, context) },
                onTitleChanged = { campSpotterViewModel.updateCampSpotTitle(it) },
                onDescriptionChanged = { campSpotterViewModel.updateCampSpotDescription(it) },
                onNumberOfPeopleChanged = { campSpotterViewModel.updateCampSpotNumberOfPeople(it, context) },
                onSaveSketchClicked = {
                    if(campSpotterViewModel.addAndUpdateCampSpot(CampSpotType.Sketch.text, context, CampSpotFormMode.Add)){
                        navController.popBackStack()
                    }
                },
                onPublishClicked = {
                    if(campSpotterViewModel.addAndUpdateCampSpot(CampSpotType.Published.text,context, CampSpotFormMode.Add)){
                        navController.popBackStack()
                    }
                },
                viewModel = campSpotterViewModel
            )
        }
        navigation(
            route = Graph.CAMP_SPOT_DETAILS,
            startDestination = CampSpotDetailScreen.CampSpotDetails.route
        ){
            composable(route = CampSpotDetailScreen.CampSpotDetails.route){
                DetailCampSpotCard(
                    campSpot = uiState.currentlySelectedCampSpot,
                    user = usersRepositoryState.users.find { it.uid == uiState.currentlySelectedCampSpot.userId } ?: LocalUserDataProvider.defaultUser,
                    users = usersRepositoryState.users,
                    sendMessageText = uiState.sendMessageText,
                    onSendMessageTextChanged = { campSpotterViewModel.updateSendMassageText(it)},
                    onSendMessageClicked = { campSpotterViewModel.addMessageToDb(context) },
                    onRemoveMessageClicked = { campSpotterViewModel.removeMessage(it) },
                    onEditClicked = {
                        campSpotterViewModel.updateCampSpotForm(it)
                        navController.navigate(route = CampSpotDetailScreen.EditCampSpot.route)
                                    },
                    onDeleteClicked = {
                        campSpotterViewModel.deleteCampSpotFromDb(it)
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                )
            }
            composable(route = CampSpotDetailScreen.EditCampSpot.route){
                CampSpotForm(
                    campSpot = uiState.campSpotForm,
                    campSpotImageUri = uiState.campSpotImageUri,
                    campSpotFormErrors = uiState.campSpotFormErrors,
                    campSpotFormMode = CampSpotFormMode.Update,
                    onStartDateSelected = { campSpotterViewModel.updatePickedStartDate(it) },
                    onEndDateSelected = { campSpotterViewModel.updatePickedEndDate(it, context) },
                    onTitleChanged = { campSpotterViewModel.updateCampSpotTitle(it) },
                    onDescriptionChanged = { campSpotterViewModel.updateCampSpotDescription(it) },
                    onNumberOfPeopleChanged = { campSpotterViewModel.updateCampSpotNumberOfPeople(it, context) },
                    onSaveSketchClicked = {
                        Log.d(TAG,"camp spot type on save sketch: $it")
                        if(campSpotterViewModel.addAndUpdateCampSpot(it, context, CampSpotFormMode.Update)){
                            navController.popBackStack(route = Graph.CAMP_SPOT_DETAILS, inclusive = true)
                            navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                        }
                                          },
                    onPublishClicked = {
                        Log.d(TAG,"camp spot type on publish: $it")
                        if(it == CampSpotType.Sketch.text){
                            campSpotterViewModel.addAndUpdateCampSpot(it, context, CampSpotFormMode.Update, isTransfer = true)
                            navController.popBackStack(route = Graph.CAMP_SPOT_DETAILS, inclusive = true)
                            navController.navigate(route = Graph.HOME)
                        }else{
                            campSpotterViewModel.addAndUpdateCampSpot(it, context, CampSpotFormMode.Update)
                            navController.popBackStack(route = Graph.CAMP_SPOT_DETAILS, inclusive = true)
                            navController.navigate(route = Graph.CAMP_SPOT_DETAILS)
                        }

                    },
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
                    user = singleUserRepositoryState.user!!,
                    onLogOutClicked = {
                            campSpotterViewModel.resetUiToInitialState()
                            CampSpotsRepository.resetRepositoryToInitialState()
                            UsersRepository.resetRepositoryToInitialState()
                            SingleUserRepository.resetRepositoryToInitialState()
                            onLogOutClicked()
                                      },
                    onEditClicked = {
                        campSpotterViewModel.updateEditUserForm(singleUserRepositoryState.user!!)
                        navController.navigate(route = UserDetailsScreen.EditUser.route)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = UserDetailsScreen.EditUser.route){
                EditProfileCard(
                    user = uiState.user,
                    userImageUri = uiState.userImageUri,
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
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }
}

@Composable
fun LoadingData(
    modifier: Modifier = Modifier
){
    Box(modifier = modifier){
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
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