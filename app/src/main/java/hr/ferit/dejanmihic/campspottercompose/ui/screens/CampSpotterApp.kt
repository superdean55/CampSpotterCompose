package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hr.ferit.dejanmihic.campspottercompose.ui.AuthViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.RootNavigationGraph

@Composable
fun CampSpotterApp(
    viewModel: AuthViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){

    RootNavigationGraph(
        navController = navController,
        viewModel = viewModel
    )
}