package hr.ferit.dejanmihic.campspottercompose.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    viewModel: CampSpotterViewModel
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route){

        }
        composable(route = AuthScreen.SignUp.route){

        }
    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
}