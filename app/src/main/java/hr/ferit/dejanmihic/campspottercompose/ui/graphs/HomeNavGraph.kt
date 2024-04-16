package hr.ferit.dejanmihic.campspottercompose.ui.graphs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import hr.ferit.dejanmihic.campspottercompose.data.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotsList
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
        startDestination = HomeScreen.AllCampSpots.route,
        modifier = modifier
    ) {
        composable(route = HomeScreen.AllCampSpots.route){
            CampSpotsList(
                campSpots = LocalCampSpotDataProvider.getCampSpots(),
                users = LocalUserDataProvider.getUsersData(),
                onCampSpotClick = { }
            )
        }
        navigation(
            route = Graph.USER_DETAILS,
            startDestination = UserDetailsScreen.UserDetails.route
        ) {
            composable(route = UserDetailsScreen.UserDetails.route){
                DetailProfileCard(
                    user = LocalUserDataProvider.getUsersData()[0],
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

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.USER_DETAILS,
        startDestination = UserDetailsScreen.UserDetails.route
    ) {

    }
}
sealed class HomeScreen(val route: String){
    object AllCampSpots : HomeScreen(route = "ALL_CAMP_SPOTS")
    object MyCampSpots: HomeScreen(route = "MY_CAMP_SPOTS")
    object CampSpotSketches : HomeScreen(route = "CAMP_SPOT_SKETCHES")
}

sealed class UserDetailsScreen(val route: String) {
    object UserDetails : UserDetailsScreen(route = "USER_DETAILS")
    object EditUser : UserDetailsScreen(route = "EDIT_USER")
}