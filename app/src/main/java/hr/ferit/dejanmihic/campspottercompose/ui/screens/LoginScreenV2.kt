package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.ferit.dejanmihic.campspottercompose.BackgroundScreen
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenV2(
    email: String,
    onEmailValueChanged: (String) -> Unit,
    password: String,
    onPasswordValueChanged: (String) -> Unit,
    onPasswordVisibilityChanged: () -> Unit,
    isPasswordVisible: Boolean,
    visibleTransformation: PasswordVisualTransformation,
    onConfirmClicked: () -> Unit,
    onNavigateClicked: () -> Unit,
    isLoading: Boolean,
    color: Color = Color.White,
    modifier: Modifier = Modifier
){
    Auth(
        messageTitleId = R.string.auth_log_in_message_title,
        confirmLabelId = R.string.auth_log_in_label_confirm,
        bottomMessageId = R.string.auth_log_in_bottom_message,
        navigateLabelId = R.string.auth_log_in_label_navigate,
        email = email,
        onEmailValueChanged = onEmailValueChanged,
        password = password,
        onPasswordValueChanged = onPasswordValueChanged,
        onPasswordVisibilityChanged = onPasswordVisibilityChanged,
        isPasswordVisible = isPasswordVisible,
        visibleTransformation = visibleTransformation,
        onConfirmClicked = onConfirmClicked,
        onNavigateClicked = onNavigateClicked,
        isLoading = isLoading,
        color = color,
        modifier = modifier
    )
}


@Preview
@Composable
fun LoginScreenV2Preview(){
    CampSpotterComposeTheme {
        Surface {
            BackgroundScreen {
                LoginScreenV2(
                    email = "",
                    onEmailValueChanged = {},
                    password = "",
                    onPasswordValueChanged = {},
                    onPasswordVisibilityChanged = {},
                    isPasswordVisible = false,
                    visibleTransformation = PasswordVisualTransformation(),
                    onConfirmClicked = {},
                    onNavigateClicked = {},
                    isLoading = false,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

        }
    }
}