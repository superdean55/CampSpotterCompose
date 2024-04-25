package hr.ferit.dejanmihic.campspottercompose.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme
import java.time.LocalDate
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.dejanmihic.campspottercompose.BuildConfig
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.CampSpotFormErrors
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.utils.CampSpotFormMode
import hr.ferit.dejanmihic.campspottercompose.ui.utils.DateType
import hr.ferit.dejanmihic.campspottercompose.ui.utils.mapToObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun DetailCampSpotCard(
    campSpot: CampSpot,
    user: User,
    users: List<User>,
    sendMessageText: String,
    onSendMessageTextChanged: (String) -> Unit,
    onSendMessageClicked: () -> Unit,
    onEditClicked: (CampSpot) -> Unit,
    onDeleteClicked: (CampSpot) -> Unit,
    imageHeight: Dp = 100.dp,
    dividersColor: Color = MaterialTheme.colorScheme.scrim,
    modifier: Modifier = Modifier
){
    var imageIsClicked by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier =
            if (!imageIsClicked) Modifier
                .fillMaxWidth()
                .height(imageHeight)
            else Modifier
                .fillMaxWidth(),
            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (campSpot.imageUrl != "") {
                    AsyncImage(
                        model = campSpot.imageUrl,
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                imageIsClicked = !imageIsClicked
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.no_image_available),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                imageIsClicked = !imageIsClicked
                            }
                    )
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = !imageIsClicked,
                    enter = fadeIn(initialAlpha = 0.3f) + slideInHorizontally(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_small))
                    ) {
                        UsernameAndImage(
                            user = user,
                            usernameBackgroundColor = Color.LightGray,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = campSpot.title!!,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            AnimatedVisibility(
                visible = imageIsClicked,
                enter = slideInHorizontally {
                    -it / 3
                },
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    UsernameAndImage(
                        user = user,
                        usernameBackgroundColor = Color.Transparent,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                }
            }
            TitleAndTwoInformationComponent(
                titleLabelId = R.string.label_gps,
                firstLabelId = R.string.label_latitude,
                firstText = campSpot.locationDetails["latitude"]!!,
                secondLabelId = R.string.label_longitude,
                secondText = campSpot.locationDetails["longitude"]!!,
                modifier = Modifier.fillMaxWidth()
            )
            TitleAndTwoInformationComponent(
                titleLabelId = R.string.label_date_of_event,
                firstLabelId = R.string.label_from,
                firstText = campSpot.startEventDate!!,
                secondLabelId = R.string.label_to,
                secondText = campSpot.endEventDate!!,
                modifier = Modifier.fillMaxWidth()
            )
            Divider(color = dividersColor)
            HorizontalLabelTextInfo(
                labelId = R.string.label_numb_of_people,
                data = campSpot.numberOfPeople,
                modifier = Modifier.fillMaxWidth()
            )
            Divider(color = dividersColor)

            Row(
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = stringResource(R.string.label_description),
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
                Text(
                    text = campSpot.description!!,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3
                )
            }
            Divider(color = dividersColor)
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MessagesCard(
                messages = mapToObject(campSpot.messages),
                users = users,
                sendMessageText = sendMessageText,
                onSendMessageTextChanged = onSendMessageTextChanged,
                onSendMessageClicked = onSendMessageClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(dimensionResource(R.dimen.padding_small))
            )
            if (user.uid == FirebaseAuth.getInstance().currentUser?.uid) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small))
                ) {
                    CustomButton(
                        onButtonClick = { onDeleteClicked(campSpot) },
                        textId = R.string.camp_spot_label_delete
                    )
                    CustomButton(
                        onButtonClick = { onEditClicked(campSpot) },
                        textId = R.string.camp_spot_label_edit
                    )
                }
            }
        }
    }
}
@Composable
fun TitleAndTwoInformationComponent(
    @StringRes titleLabelId: Int,
    @StringRes firstLabelId: Int,
    firstText: String,
    @StringRes secondLabelId: Int,
    secondText: String,
    modifier: Modifier = Modifier
){
    Column (
        modifier = modifier
    ){
        Text(text = stringResource(titleLabelId), style = MaterialTheme.typography.labelMedium)
        TwoInformationComponentInRow(
            firstLabelId = firstLabelId,
            firstText = firstText,
            secondLabelId = secondLabelId,
            secondText = secondText,
            rowHeight = R.dimen.information_row_height,
            dividerHeight = R.dimen.information_divider_height,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
fun UsernameAndImage(
    user: User,
    usernameBackgroundColor: Color,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Text(
            modifier = Modifier.background(usernameBackgroundColor),
            text = user.username!!,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
        UserImageItem(
            userImageUrl = user.imageUrl!!,
            modifier = Modifier
                .size(dimensionResource(R.dimen.card_account_image_height))
                .clip(CircleShape)
        )
    }
}
@Composable
fun TestAppComponent(
    viewModel: CampSpotterViewModel = viewModel(),
    modifier : Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    CampSpotForm(
        campSpot = uiState.campSpotForm,
        campSpotImageUri = uiState.campSpotImageUri,
        campSpotFormErrors = uiState.campSpotFormErrors,
        campSpotFormMode = CampSpotFormMode.Add,
        onStartDateSelected = { viewModel.updatePickedStartDate(it) },
        onEndDateSelected = { viewModel.updatePickedEndDate(it, context) },
        onTitleChanged = { viewModel.updateCampSpotTitle(it) },
        onDescriptionChanged = { viewModel.updateCampSpotDescription(it) },
        onNumberOfPeopleChanged = { viewModel.updateCampSpotNumberOfPeople(it, context) },
        onSaveSketchClicked = { viewModel.saveCampSpot(context)},
        onPublishClicked = {},
        viewModel = viewModel,
        modifier = modifier
    )
}
@Composable
fun CampSpotForm(
    campSpot: CampSpot,
    campSpotImageUri: Uri,
    campSpotFormErrors: CampSpotFormErrors,
    errorColor: Color = MaterialTheme.colorScheme.error,
    campSpotFormMode: CampSpotFormMode,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate) -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onNumberOfPeopleChanged: (String) -> Unit,
    onSaveSketchClicked: (String) -> Unit,
    onPublishClicked: (String) -> Unit,
    dividersColor: Color = MaterialTheme.colorScheme.scrim,
    modifier: Modifier = Modifier,
    viewModel: CampSpotterViewModel
){

    val startDateDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()

    val context = LocalContext.current

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if(it) { viewModel.updateUri(uri, context) }
        }

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            cameraLauncher.launch(uri)
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = if (campSpotFormErrors.isImageUriError) Modifier
                    .fillMaxWidth()
                    .background(errorColor) else Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
                        .clickable {
                            if (permissions.all {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        it
                                    ) == PackageManager.PERMISSION_GRANTED
                                }) {
                                cameraLauncher.launch(uri)
                            } else {
                                launcherMultiplePermissions.launch(permissions)
                            }
                        }
                ) {
                    if (campSpotImageUri != Uri.EMPTY) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(campSpotImageUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (campSpot.imageUrl != "") {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(campSpot.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.no_image_available),
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.no_image_available),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                TitleAndTwoDataInRow(
                    isError = campSpotFormErrors.isLocationDetailsError,
                    errorColor = errorColor,
                    titleId = R.string.label_gps,
                    firstLabel = R.string.label_latitude,
                    firstData = campSpot.locationDetails["latitude"],
                    secondLabel = R.string.label_longitude,
                    secondData = campSpot.locationDetails["longitude"],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.padding_small))
                )
                Divider(color = dividersColor)
                UserInputField(
                    text = campSpot.title!!,
                    labelId = R.string.label_title,
                    onValueChanged = onTitleChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isError = campSpotFormErrors.isTitleError
                )
                UserInputField(
                    text = campSpot.description!!,
                    labelId = R.string.label_description,
                    onValueChanged = onDescriptionChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isError = campSpotFormErrors.isDescriptionError
                )
                UserInputField(
                    text = campSpot.numberOfPeople!!,
                    labelId = R.string.label_numb_of_people,
                    onValueChanged = onNumberOfPeopleChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    isError = campSpotFormErrors.isNumberOfPeopleError
                )
                ChooseDate(
                    dateDialogState = startDateDialogState,
                    labelId = R.string.label_event_starts,
                    buttonLabelId = R.string.label_pick_date,
                    date = campSpot.startEventDate!!,
                    onDateSelected = onStartDateSelected,
                    property = DateType.EVENT_DATE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                )
                Divider(color = dividersColor)
                ChooseDate(
                    dateDialogState = endDateDialogState,
                    labelId = R.string.label_event_ends,
                    buttonLabelId = R.string.label_pick_date,
                    date = campSpot.endEventDate!!,
                    onDateSelected = onEndDateSelected,
                    property = DateType.EVENT_DATE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_small))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small))
                ) {
                    if(campSpot.campSpotType == CampSpotType.Sketch.text) {
                        CustomButton(
                            onButtonClick = { onSaveSketchClicked(campSpot.campSpotType!!) },
                            textId = campSpotFormMode.leftButtonLabelId
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    CustomButton(
                        onButtonClick = { onPublishClicked(campSpot.campSpotType!!) },
                        textId = campSpotFormMode.rightButtonLabelId
                    )
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}
@Composable
fun <T>TitleAndTwoDataInRow(
    isError: Boolean = false,
    errorColor: Color = MaterialTheme.colorScheme.error,
    @StringRes titleId: Int,
    @StringRes firstLabel: Int,
    firstData: T,
    @StringRes secondLabel: Int,
    secondData: T,
    modifier: Modifier = Modifier
){

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(titleId),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        AdditionalItemData(
            isError = isError,
            errorColor = errorColor,
            firstLabel = firstLabel,
            firstData = firstData,
            secondLabel = secondLabel,
            secondData = secondData,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
@Composable
fun ChooseDate(
    dateDialogState: MaterialDialogState,
    date: String,
    onDateSelected: (LocalDate) -> Unit,
    @StringRes labelId: Int,
    @StringRes buttonLabelId: Int,
    property: DateType,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier

    ){
        InformationComponent(labelId = labelId, text = date)
        Spacer(modifier = Modifier.weight(1f))
        TextButton(
            onClick = {
                dateDialogState.show()
            }
        ) {
            Text(
                text = stringResource(buttonLabelId)
            )
        }
        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "Ok") {

                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
                allowedDateValidator = {
                    if(property == DateType.BIRTH_DATE){
                        it.isBefore(LocalDate.now())
                    }else {
                        it.isAfter(LocalDate.now())
                    }
                }
            ) {
                onDateSelected(it)
            }
        }
    }
}
@Composable
fun CustomButton(
    onButtonClick: () -> Unit,
    @StringRes textId: Int,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onButtonClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
            contentColor = MaterialTheme.colorScheme.scrim
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.padding_small)),
        elevation = ButtonDefaults.buttonElevation(),
        modifier = modifier
    ) {
        Text(
            text = stringResource( textId ),
            style = MaterialTheme.typography.titleSmall
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputField(
    text: String,
    labelId: Int,
    isError: Boolean,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier.fillMaxWidth()
){
    TextField(
        modifier = modifier,
        value = text,
        isError = isError,
        onValueChange = onValueChanged,
        label = {
            Text(
                text = stringResource(labelId),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        keyboardOptions = keyboardOptions,
        maxLines = 2,
        textStyle = MaterialTheme.typography.bodyMedium
    )
}
@Composable
fun <T>AdditionalItemData(
    isError: Boolean = false,
    errorColor: Color = MaterialTheme.colorScheme.error,
    @StringRes firstLabel: Int,
    firstData: T,
    @StringRes secondLabel: Int,
    secondData: T,
    modifier: Modifier = Modifier
){
    Row(
        modifier = if(isError)modifier.background(errorColor) else modifier
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = stringResource(firstLabel),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = dataToString(firstData),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(
                text = stringResource(secondLabel),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = dataToString(secondData),
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}
@Preview(showBackground = true)
@Composable
fun DetailCampSpotCardPreview(){
    CampSpotterComposeTheme {
        Surface {
            DetailCampSpotCard(
                campSpot = LocalCampSpotDataProvider.getCampSpots()[0],
                user = LocalUserDataProvider.getUsersData()[0],
                users = LocalUserDataProvider.getUsersData(),
                sendMessageText = "",
                onSendMessageTextChanged = {},
                onSendMessageClicked = {},
                onEditClicked = {},
                onDeleteClicked = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview(){
    CampSpotterComposeTheme {
        Surface {
            TestAppComponent()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampSpotFormPreview(){
    CampSpotterComposeTheme {
        Surface {
            CampSpotForm(
                campSpot = LocalCampSpotDataProvider.defaultCampSpot,
                campSpotImageUri = Uri.EMPTY,
                campSpotFormErrors = CampSpotFormErrors(),
                campSpotFormMode = CampSpotFormMode.Add,
                onStartDateSelected = {},
                onEndDateSelected = {},
                onTitleChanged = {},
                onDescriptionChanged = {},
                onNumberOfPeopleChanged = {},
                onSaveSketchClicked = {},
                onPublishClicked = {},
                viewModel = CampSpotterViewModel()
            )
        }
    }
}