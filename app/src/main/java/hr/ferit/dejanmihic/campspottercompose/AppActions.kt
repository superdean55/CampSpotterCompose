package hr.ferit.dejanmihic.campspottercompose

sealed class AppActions{
    object LogIn: AppActions()
    object CreateAccount: AppActions()
    object UserLogOut: AppActions()
    object NavigateToRegisterScreen: AppActions()
}
