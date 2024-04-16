package hr.ferit.dejanmihic.campspottercompose.ui.graphs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.dejanmihic.campspottercompose.ui.screens.Background
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.ui.AuthViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.screens.HomeScreen
import hr.ferit.dejanmihic.campspottercompose.ui.screens.LoginScreenV2
import hr.ferit.dejanmihic.campspottercompose.ui.screens.RegisterScreenV2

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var starDestination = Graph.AUTHENTICATION
    if (FirebaseAuth.getInstance().currentUser != null){
        starDestination = Graph.HOME
    }
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = starDestination
    ) {
        navigation(
            route = Graph.AUTHENTICATION,
            startDestination = AuthScreen.Login.route
        ) {
            composable(route = AuthScreen.Login.route) {
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
                            viewModel.logIn(context, navController)
                        },
                        onNavigateClicked = {
                            viewModel.setUiStateToDefault()
                            navController.navigate(route = AuthScreen.SignUp.route)
                        },
                        isLoading = uiState.isLoadingData,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
            }

            composable(route = AuthScreen.SignUp.route) {
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
                            if (viewModel.createAccount(context)) {
                                navController.navigate(AuthScreen.Login.route)
                            }

                        },
                        onNavigateClicked = {
                            viewModel.setUiStateToDefault()
                            navController.popBackStack()
                        },
                        isLoading = uiState.isLoadingData,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
            }
        }
        composable(route = Graph.HOME){
            HomeScreen(
                onLogOutClicked = {
                    viewModel.logOut()
                    navController.popBackStack(route = Graph.HOME, inclusive = true)
                    navController.navigate(route = Graph.AUTHENTICATION)
                }
            )
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
    const val USER_DETAILS = "user_details_graph"
}
sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
}