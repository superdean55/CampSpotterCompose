package hr.ferit.dejanmihic.campspottercompose

sealed class Screen(val route: String){
    object LoginScreen: Screen(route = "loginScreen")
    object RegisterScreen: Screen(route = "RegisterScreen")
    object MainScreen: Screen(route = "MainScreen")
}
