package hr.ferit.dejanmihic.campspottercompose.ui.graphs

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.ferit.dejanmihic.campspottercompose.ui.screens.Background
import hr.ferit.dejanmihic.campspottercompose.MainScreenV2
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.ui.AuthUiState
import hr.ferit.dejanmihic.campspottercompose.ui.AuthViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.screens.LoginScreenV2
import hr.ferit.dejanmihic.campspottercompose.ui.screens.RegisterScreenV2


fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    viewModel: AuthViewModel,
    uiState: AuthUiState,
    context: Context
) {

    if (Firebase.auth.currentUser != null){
        navController.navigate(Graph.HOME)
    }
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route){
            Background {
                LoginScreenV2(
                    email = uiState.email,
                    onEmailValueChanged = { viewModel.updateEmail(it) },
                    password = uiState.password,
                    onPasswordValueChanged = { viewModel.updatePassword(it) },
                    onPasswordVisibilityChanged = { viewModel.changePasswordVisibility() },
                    isPasswordVisible = uiState.isPasswordVisible,
                    visibleTransformation = uiState.visualTransformation,
                    onConfirmClicked = {

                    },
                    onNavigateClicked = { navController.navigate(route= AuthScreen.SignUp.route) },
                    isLoading = uiState.isLoadingData,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
        composable(route = AuthScreen.SignUp.route){
            Background {
                RegisterScreenV2(
                    email = uiState.email,
                    onEmailValueChanged = { viewModel.updateEmail(it) },
                    password = uiState.password,
                    onPasswordValueChanged = { viewModel.updatePassword(it) },
                    onPasswordVisibilityChanged = { viewModel.changePasswordVisibility() },
                    isPasswordVisible = uiState.isPasswordVisible,
                    visibleTransformation = uiState.visualTransformation,
                    onConfirmClicked = {
                        if (viewModel.createAccount(context)){
                            navController.navigate(AuthScreen.Login.route)
                        }
                    },
                    onNavigateClicked = { navController.navigate(AuthScreen.Login.route) },
                    isLoading = uiState.isLoadingData,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
        composable(route = Graph.HOME){
            MainScreenV2(
                onLogOutClicked = {

                                  },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

