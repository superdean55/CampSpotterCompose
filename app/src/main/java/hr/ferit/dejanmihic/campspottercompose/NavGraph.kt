package hr.ferit.dejanmihic.campspottercompose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.screens.LoginScreen
import hr.ferit.dejanmihic.campspottercompose.ui.screens.RegisterScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModel: CampSpotterViewModel,
    startDestination: String
){
    NavHost(
        navController = navController,
        startDestination = startDestination
        ){
        composable(
            route = Screen.LoginScreen.route
        ){
            BeckgroundScreen {
                LoginScreen(navController, viewModel)
            }
        }
        composable(
            route = Screen.RegisterScreen.route
        ){
            BeckgroundScreen {
                RegisterScreen(navController, viewModel)
            }
        }
        composable(
            route = Screen.MainScreen.route
        ){
            BeckgroundScreen {
                MainScreen(navController, viewModel)
            }
        }
    }
}