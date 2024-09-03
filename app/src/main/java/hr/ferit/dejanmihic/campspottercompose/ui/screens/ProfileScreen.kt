package hr.ferit.dejanmihic.campspottercompose.ui.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.model.UserFormErrors
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme
import hr.ferit.dejanmihic.campspottercompose.ui.utils.DateType
import hr.ferit.dejanmihic.campspottercompose.ui.utils.dataToString
import java.time.LocalDate
import hr.ferit.dejanmihic.campspottercompose.ui.utils.languages

@Composable
fun DetailProfileCard(
    user: User,
    onLogOutClicked: () -> Unit,
    onEditClicked: () -> Unit,
    isAdditionalOptionsVisible: Boolean,
    isDeleteDialogVisible: Boolean,
    isDeleteButtonEnabled: Boolean,
    onAdditionalOptionsClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onConfirmDeletionClicked: () -> Unit,
    onRejectDeletionClicked: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    selectedLanguage: String,
    modifier: Modifier = Modifier
){
    Log.d("DETAIL PROFILE CARD", "selected language = $selectedLanguage")
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = dimensionResource(R.dimen.padding_small))
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacer_small))
            ) {
                UserImageItem(
                    userImageUrl = user.imageUrl!!,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.detail_user_image_size))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
                )
            }
            TwoInformationComponentInRow(
                firstLabelId = R.string.label_username,
                firstText = user.username!!,
                secondLabelId = R.string.label_email,
                secondText = user.email!!,
                rowHeight = R.dimen.information_row_height,
                dividerHeight = R.dimen.information_divider_height,
                modifier = Modifier.fillMaxWidth()
            )
            TwoInformationComponentInRow(
                firstLabelId = R.string.label_firstname,
                firstText = user.firstName!!,
                secondLabelId = R.string.label_lastname,
                secondText = user.lastName!!,
                rowHeight = R.dimen.information_row_height,
                dividerHeight = R.dimen.information_divider_height,
                modifier = Modifier.fillMaxWidth()
            )
            TwoInformationComponentInRow(
                firstLabelId = R.string.label_date_of_birth,
                firstText = user.birthDate!!,
                secondLabelId = R.string.label_registration_date,
                secondText = user.creationDate!!,
                rowHeight = R.dimen.information_row_height,
                dividerHeight = R.dimen.information_divider_height,
                modifier = Modifier.fillMaxWidth()
            )
            UserAdditionalOptions(
                isAdditionalOptionsVisible = isAdditionalOptionsVisible,
                isDeleteDialogVisible = isDeleteDialogVisible,
                onAdditionalOptionsClicked = onAdditionalOptionsClicked,
                onDeleteAccountClicked = onDeleteAccountClicked,
                onConfirmDeletionClicked = onConfirmDeletionClicked,
                onRejectDeletionClicked = onRejectDeletionClicked,
                isDeleteButtonEnabled = isDeleteButtonEnabled,
                onLanguageSelected = onLanguageSelected,
                selectedLanguage = selectedLanguage,
                modifier = Modifier
                    .padding(
                        start = dimensionResource(R.dimen.padding_small),
                        top = dimensionResource(R.dimen.padding_small)
                    )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth()
            ) {
                CustomButton(
                    onButtonClick = onLogOutClicked,
                    textId = R.string.user_label_log_uot,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(1f))
                CustomButton(
                    onButtonClick = onEditClicked,
                    textId = R.string.user_label_edit,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun UserAdditionalOptions(
    isAdditionalOptionsVisible: Boolean,
    isDeleteDialogVisible: Boolean,
    isDeleteButtonEnabled: Boolean,
    onAdditionalOptionsClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onConfirmDeletionClicked: () -> Unit,
    onRejectDeletionClicked: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    selectedLanguage: String,
    modifier: Modifier = Modifier
){

    Column (
        modifier = modifier
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.padding_small))
                .clickable(onClick = onAdditionalOptionsClicked)
        ) {
            Text(
                text = stringResource(R.string.user_label_additional_options),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
            Box(modifier = Modifier
                .size(dimensionResource(R.dimen.user_additional_options_box_icon_size))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.inversePrimary)) {
                Icon(
                    imageVector =
                    if (isAdditionalOptionsVisible) {
                        Icons.Default.ExpandLess
                    }else{
                        Icons.Default.ExpandMore
                        },
                    contentDescription = null
                )
            }
        }
        if(isAdditionalOptionsVisible){
            if(!isDeleteButtonEnabled){
                Text(
                    text = stringResource(R.string.user_account_delete_message),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            CustomButton(
                onButtonClick = onDeleteAccountClicked,
                isEnabled = isDeleteButtonEnabled,
                textId = R.string.user_label_delete_account
            )
            if (isDeleteDialogVisible){
                DeleteAccountDialog(
                    onConfirm = onConfirmDeletionClicked,
                    onReject = onRejectDeletionClicked
                )
            }
            ChooseLanguage(selectedLanguage = selectedLanguage, onLanguageSelected = onLanguageSelected)
        }
    }
}
@Composable
fun ChooseLanguage(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
){

    var expanded by remember { mutableStateOf(false) }
    Log.d("CHOSE LANGUAGE", "selected language = $selectedLanguage")
    Column( modifier = modifier ){
        Text(
            text = stringResource(R.string.select_language),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp),

            ) {
            Text(
                text = selectedLanguage,
                modifier = Modifier.padding(8.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { (languageName, languageCode) ->
                DropdownMenuItem(
                    text = {
                        Text(text = languageName)
                    },
                    onClick = {
                        expanded = false
                        onLanguageSelected(languageCode)
                    })
            }
        }
    }

}
@Composable
fun DeleteAccountDialog(
    onConfirm: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.user_label_delete_account),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.user_delete_dialog_alert_message),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        onDismissRequest = onReject,
        confirmButton = {
            CustomButton(
                onButtonClick = onConfirm,
                textId = R.string.user_label_delete_dialog_confirm
            )
        },
        dismissButton = {
            CustomButton(
                onButtonClick = onReject,
                textId = R.string.user_label_delete_dialog_reject
            )
        },
        modifier = modifier
    )
}

@Composable
fun EditProfileCard(
    user: User,
    userImageUri: Uri,
    userFormErrors: UserFormErrors,
    onResultUpdateImage: (Uri?) -> Unit,
    onBirthDateSelected: (LocalDate) -> Unit,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onSaveClicked: (User) -> Unit,
    modifier: Modifier = Modifier
){
    val birthDateDialogState = rememberMaterialDialogState()
    val singlePhoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia() ,
        onResult = {
            onResultUpdateImage(it)
        }
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(dimensionResource(R.dimen.padding_small))
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.edit_user_image_size))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.edit_user_image_corner_radius)))
                        .clickable {
                            singlePhoto.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                ){
                    if(userImageUri != Uri.EMPTY){
                        AsyncImage(
                            model = userImageUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }else if(user.imageUrl != ""){
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.blank_profile_picture),
                            modifier = Modifier.fillMaxSize()
                        )
                    }else {
                        Image(
                            painter = painterResource(R.drawable.blank_profile_picture),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                
            }

            UserInputField(
                text = user.firstName!!,
                labelId = R.string.label_firstname,
                isError = userFormErrors.isFirstNameError,
                onValueChanged = onFirstNameChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )
            UserInputField(
                text = user.lastName!!,
                labelId = R.string.label_lastname,
                isError = userFormErrors.isLastNameError,
                onValueChanged = onLastNameChanged,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.None
                ),
            )
            ChooseDate(
                dateDialogState = birthDateDialogState,
                date = dataToString(user.birthDate),
                onDateSelected = onBirthDateSelected,
                labelId = R.string.label_date_of_birth,
                buttonLabelId = R.string.label_pick_date,
                property = DateType.BIRTH_DATE,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
            )
            Row (
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium))
            ){
                CustomButton(
                    onButtonClick = { onSaveClicked(user) },
                    textId = R.string.label_save
                )
            }

        }
    }
}

@Composable
fun <T>VerticalLabelTextInfo(
    @StringRes labelId: Int,
    data: T,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ){
        Text(
            text = stringResource(labelId),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = dataToString(data),
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun InformationComponent(
    @StringRes labelId: Int,
    maxLines: Int = 1,
    text: String,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        Text(
            text = stringResource(labelId),
            style = MaterialTheme.typography.bodySmall,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@Composable
fun TwoInformationComponentInRow(
    @StringRes firstLabelId: Int,
    firstText: String,
    @StringRes secondLabelId: Int,
    secondText: String,
    @DimenRes rowHeight: Int,
    @DimenRes dividerHeight: Int,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier.height(dimensionResource(rowHeight))
    ){
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.information_left_padding))
                .weight(1f)
                .fillMaxHeight()
        ) {
            InformationComponent(labelId = firstLabelId, text = firstText)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                modifier = Modifier
                    .height(dimensionResource(dividerHeight))
                    .width(1.dp)
            )
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(start = dimensionResource(R.dimen.information_left_padding))
                .weight(1f)
                .fillMaxHeight()
        ) {
            InformationComponent(labelId = secondLabelId, text = secondText)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TwoInformationComponentInRowPreview(){
    CampSpotterComposeTheme {
        Surface {
            TwoInformationComponentInRow(
                firstLabelId = R.string.label_firstname,
                firstText = "Dejan",
                secondLabelId = R.string.label_lastname,
                secondText = "Mihić",
                rowHeight = R.dimen.information_row_height,
                dividerHeight = R.dimen.information_divider_height,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun InformationComponentPreview(){
    CampSpotterComposeTheme {
        Surface {
            InformationComponent(
                labelId = R.string.label_firstname,
                text = "Dejan"
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DetailProfileCardPreview(){
    CampSpotterComposeTheme {
        Surface {
            DetailProfileCard(
                user = LocalUserDataProvider.getUsersData()[0],
                onLogOutClicked = {},
                onEditClicked = {},
                isAdditionalOptionsVisible = true,
                isDeleteDialogVisible = false,
                isDeleteButtonEnabled = true,
                onAdditionalOptionsClicked = {},
                onDeleteAccountClicked = {},
                onConfirmDeletionClicked = {},
                onRejectDeletionClicked = {},
                onLanguageSelected = {},
                selectedLanguage = "English",
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileCardPreview(){
    CampSpotterComposeTheme {
        Surface {
            EditProfileCard(
                user = LocalUserDataProvider.getUsersData()[0],
                userImageUri = Uri.EMPTY,
                userFormErrors = UserFormErrors(),
                onBirthDateSelected = {},
                onFirstNameChanged = {},
                onLastNameChanged = {},
                onSaveClicked = {},
                onResultUpdateImage = {},
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestEditProfileCard(){
    CampSpotterComposeTheme {
        Surface {
            val viewModel: CampSpotterViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            EditProfileCard(
                user = uiState.user,
                userImageUri = Uri.EMPTY,
                userFormErrors = uiState.userFormErrors,
                onBirthDateSelected = { viewModel.updateBirthDate(it) },
                onFirstNameChanged = { viewModel.updateFirstName(it) },
                onLastNameChanged = { viewModel.updateLastName(it) },
                onSaveClicked = { viewModel.saveEditedUser(it, context) },
                onResultUpdateImage = { viewModel.updateUserImageUri(it) }
            )
        }
    }
}