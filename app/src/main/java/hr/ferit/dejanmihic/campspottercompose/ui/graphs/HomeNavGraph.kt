package hr.ferit.dejanmihic.campspottercompose.ui.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    viewModel: CampSpotterViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = HomeScreen.AllCampSpots.route,
        modifier = modifier
    ) {
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.Information.route
    ) {

    }
}
sealed class HomeScreen(val route: String){
    object AllCampSpots : HomeScreen(route = "ALL_CAMP_SPOTS")
    object MyCampSpots: HomeScreen(route = "MY_CAMP_SPOTS")
    object CampSpotSketches : HomeScreen(route = "CAMP_SPOT_SKETCHES")
}

sealed class DetailsScreen(val route: String) {
    object Information : DetailsScreen(route = "INFORMATION")
    object Overview : DetailsScreen(route = "OVERVIEW")
}