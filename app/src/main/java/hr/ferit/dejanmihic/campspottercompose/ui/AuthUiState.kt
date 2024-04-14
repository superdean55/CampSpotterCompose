package hr.ferit.dejanmihic.campspottercompose.ui

import androidx.compose.ui.text.input.PasswordVisualTransformation

data class AuthUiState (
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoadingData: Boolean = false,
    val visualTransformation: PasswordVisualTransformation = PasswordVisualTransformation(),
)