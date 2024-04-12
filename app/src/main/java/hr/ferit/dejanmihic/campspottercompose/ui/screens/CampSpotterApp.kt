package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.RootNavigationGraph

@Composable
fun CampSpotterApp(
    viewModel: CampSpotterViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){

    RootNavigationGraph(
        navController = navController,
        viewModel = viewModel
    )
}