package hr.ferit.dejanmihic.campspottercompose.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import java.time.LocalDate

@Composable
fun DetailProfileCard(
    user: User,
    onLogOutClicked: () -> Unit,
    onEditClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Card (
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_radius)
        ),
        modifier = modifier
    ){
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.spacer_small))
            ) {
                UserImageItem(
                    userImageUrl = user.imageUrl,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.card_image_height))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
                )
                VerticalLabelTextInfo(
                    labelId = R.string.label_username,
                    data = user.username,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_email,
                data = user.email,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_firstname,
                data = user.firstName,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_lastname,
                data = user.lastName,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_date_of_birth,
                data = user.birthDate,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_registration_date,
                data = user.creationDate,
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth()
            ){
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onLogOutClicked
                ) {
                    Text(text = "Log out")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onEditClicked
                ) {
                    Text(text = "Edit")
                }
            }
        }

    }
}

@Composable
fun <T>HorizontalLabelTextInfo(
    lineHeight: Dp = 30.dp,
    @StringRes labelId: Int,
    data: T,
    modifier: Modifier = Modifier
){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(lineHeight)
    ){
        Text(
            text = stringResource(labelId),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
        Text(
            text = dataToString(data),
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
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

    Card(
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_radius)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                        .size(200.dp)
                        .clip(RoundedCornerShape(20.dp))
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
                property = DateType.BIRTH_DATE
            )
            Divider()
            Row (
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
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

@Preview(showBackground = true)
@Composable
fun DetailProfileCardPreview(){
    CampSpotterComposeTheme {
        Surface {
            DetailProfileCard(
                user = LocalUserDataProvider.getUsersData()[0],
                onLogOutClicked = {},
                onEditClicked = {},
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