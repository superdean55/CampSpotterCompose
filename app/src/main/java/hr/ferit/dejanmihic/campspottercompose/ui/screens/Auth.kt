package hr.ferit.dejanmihic.campspottercompose.ui.screens


import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.ferit.dejanmihic.campspottercompose.BackgroundScreen
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme
import hr.ferit.dejanmihic.campspottercompose.ui.theme.DarkBlue
import hr.ferit.dejanmihic.campspottercompose.ui.theme.MediumBlue


@ExperimentalMaterial3Api
@Composable
fun Auth(
    @StringRes messageTitleId: Int,
    @StringRes confirmLabelId: Int,
    @StringRes bottomMessageId: Int,
    @StringRes navigateLabelId: Int,
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
    color: Color,
    modifier: Modifier = Modifier
){

    val context = LocalContext.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            CampSpotterTitle(
                titleId = R.string.auth_title_app,
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                AuthMessageTitle(
                    messageTitleId = messageTitleId,
                    color = color
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_extra_large)))
                AuthEmailAndPasswordInput(
                    email = email,
                    onEmailValueChanged = onEmailValueChanged,
                    password = password,
                    onPasswordValueChanged = onPasswordValueChanged,
                    onPasswordVisibilityChanged = onPasswordVisibilityChanged,
                    isPasswordVisible = isPasswordVisible,
                    visibleTransformation = visibleTransformation,
                    color = color
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
                AuthConfirmButton(
                    confirmLabelId = confirmLabelId,
                    onConfirmClicked = onConfirmClicked,
                    color = color
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_extra_large)))
                AuthBottomMessage(
                    bottomMessageId = bottomMessageId
                )
                AuthNavigateButton(
                    navigateLabelId = navigateLabelId,
                    onNavigateClicked = onNavigateClicked,
                    color = color
                )
            }
        }
        if (isLoading){
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CampSpotterTitle(
    @StringRes titleId: Int,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_medium)))

    ) {
        Text(
            modifier = Modifier
                .background(MediumBlue)
                .padding(dimensionResource(R.dimen.padding_medium)),
            text = stringResource(titleId),
            style = MaterialTheme.typography.titleLarge,
            color = DarkBlue
        )
    }
}

@Composable
fun AuthMessageTitle(
    @StringRes messageTitleId: Int,
    color: Color,
    modifier: Modifier = Modifier
){
    Text(
        text = stringResource(messageTitleId),
        style = MaterialTheme.typography.titleLarge,
        color = color,
        modifier = modifier
    )
}

@Composable
fun AuthBottomMessage(
    @StringRes bottomMessageId: Int,
    modifier: Modifier = Modifier
){
    Text(
        text = stringResource(bottomMessageId),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

@Composable
fun AuthNavigateButton(
    @StringRes navigateLabelId: Int,
    onNavigateClicked: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier
){
    TextButton(
        onClick = onNavigateClicked,
        modifier = modifier
    ) {
        Text(
            text = stringResource(navigateLabelId),
            color = color,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun AuthConfirmButton(
    @StringRes confirmLabelId: Int,
    onConfirmClicked: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onConfirmClicked,
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(confirmLabelId),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthEmailAndPasswordInput(
    email: String,
    onEmailValueChanged: (String) -> Unit,
    password: String,
    onPasswordValueChanged: (String) -> Unit,
    onPasswordVisibilityChanged: () -> Unit,
    isPasswordVisible: Boolean,
    visibleTransformation: PasswordVisualTransformation,
    color: Color = Color.White,
    modifier: Modifier = Modifier
){
    val keyboardController = LocalSoftwareKeyboardController.current

    val icon = if(isPasswordVisible){
        painterResource(id = R.drawable.baseline_visibility_24)
    }else{
        painterResource(id = R.drawable.baseline_visibility_off_24)
    }
    Column(
        modifier = modifier
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailValueChanged,
            label = {
                Text(
                    text = stringResource(R.string.auth_label_email),
                    color = color
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Email icon",
                    tint = color
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = color,
                unfocusedBorderColor = color,
                focusedLabelColor = color,
                cursorColor = color
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = onPasswordValueChanged,
            label = {
                Text(
                    text = stringResource(R.string.auth_label_password),
                    color = color
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Email icon",
                    tint = color
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onPasswordVisibilityChanged
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = "Visibility icon",
                        tint = color
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = color,
                unfocusedBorderColor = color,
                focusedLabelColor = color,
                cursorColor = color
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            visualTransformation = visibleTransformation
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AuthPreview(){
    CampSpotterComposeTheme {
        Surface {
            BackgroundScreen {
                Auth(
                    messageTitleId = R.string.auth_log_in_message_title,
                    confirmLabelId = R.string.auth_log_in_label_confirm,
                    bottomMessageId = R.string.auth_log_in_bottom_message,
                    navigateLabelId = R.string.auth_log_in_label_navigate,
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