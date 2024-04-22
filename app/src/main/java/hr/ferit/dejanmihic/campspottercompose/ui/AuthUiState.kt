package hr.ferit.dejanmihic.campspottercompose.ui

import androidx.annotation.DrawableRes
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import hr.ferit.dejanmihic.campspottercompose.R

data class AuthUiState (
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoadingData: Boolean = false,
    val visualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val isUserLoggedOutDuringRuntime :Boolean = false
)