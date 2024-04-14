package hr.ferit.dejanmihic.campspottercompose.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import java.time.format.DateTimeFormatter
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import hr.ferit.dejanmihic.campspottercompose.BuildConfig
import hr.ferit.dejanmihic.campspottercompose.data.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.ui.utils.DateType
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@Composable
fun DetailCampSpotCard(
    imageHeight: Dp = 100.dp,
    modifier: Modifier = Modifier
){
    var imageIsClicked by remember { mutableStateOf(false) }
    Card (
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        modifier = modifier
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
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
                    Image(
                        painter = painterResource(R.drawable.camp_spot_image_1),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                imageIsClicked = !imageIsClicked
                            }
                    )
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !imageIsClicked,
                        enter = fadeIn(initialAlpha = 0.3f)+ slideInHorizontally(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.padding_small))
                        ) {
                            UsernameAndImage(
                                imageId = R.drawable.person,
                                usernameId =  R.string.username_1,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                            )

                        }


                    }
                }

            }
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ){
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.camp_spot_title_1),
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                AnimatedVisibility(
                    visible = imageIsClicked,
                    enter = slideInHorizontally {
                        -it/3
                    },
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column {
                        UsernameAndImage(
                            imageId = R.drawable.person,
                            usernameId =  R.string.username_1,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                    }
                }
                Divider()
                TitleAndTwoDataInRow(
                    titleId = R.string.label_gps,
                    firstLabel = R.string.label_latitude,
                    firstData = "4.448948",
                    secondLabel = R.string.label_longitude,
                    secondData = "5.94949",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.padding_small))
                )
                Divider()
                TitleAndTwoDataInRow(
                    titleId = R.string.label_date_of_event,
                    firstLabel = R.string.label_from,
                    firstData = "12.01.2024.",
                    secondLabel = R.string.label_to,
                    secondData = "17.01.2024",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.padding_small))
                )
                Divider()
                HorizontalLabelTextInfo(
                    labelId = R.string.label_numb_of_people,
                    data = 12,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider()

                Row(
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)),
                    verticalAlignment = Alignment.Top,
                ){
                    Text(
                        text = stringResource(R.string.label_description),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
                    Text(
                        text = dataToString("Kampiranje za prvi maj, bit će ludo"),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3
                    )
                }
                Divider()
                HorizontalLabelTextInfo(
                    labelId = R.string.label_reviews,
                    data = "Bez komentara",
                    modifier = Modifier.fillMaxWidth()
                )
                Divider()
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Button(
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = "Edit"
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun UsernameAndImage(
    @DrawableRes imageId: Int,
    @StringRes usernameId: Int,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ){
        Text(
            modifier = Modifier.background(Color.LightGray),
            text = stringResource(usernameId),
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
        CampSpotImageItem(
            imageId = imageId,
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
        campSpot = uiState.campSpot,
        onStartDateSelected = { viewModel.updatePickedStartDate(it) },
        onEndDateSelected = { viewModel.updatePickedEndDate(it, context) },
        onTitleChanged = { viewModel.updateCampSpotTitle(it) },
        onDescriptionChanged = { viewModel.updateCampSpotDescription(it) },
        onNumberOfPeopleChanged = { viewModel.updateCampSpotNumberOfPeople(it, context) },
        onSaveSketchClicked = { viewModel.saveCampSpot(context)},
        onPublishClicked = {},
        viewModel = viewModel
    )
}
@Composable
fun CampSpotForm(
    campSpot: CampSpot,
    errorColor: Color = MaterialTheme.colorScheme.error,
    onStartDateSelected: (LocalDate) -> Unit,
    onEndDateSelected: (LocalDate) -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onNumberOfPeopleChanged: (String) -> Unit,
    onSaveSketchClicked: () -> Unit,
    onPublishClicked: () -> Unit,
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
            viewModel.updateUri(uri)
        }

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            cameraLauncher.launch(uri)
            viewModel.startLocationUpdates(context)
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
    Card (
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_radius)
        ),
        modifier = modifier
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Row(
                modifier = if(campSpot.campSpotFormErrors.isImageUriError)Modifier.fillMaxWidth().background(errorColor) else Modifier.fillMaxWidth()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
                    if (campSpot.imageUri.path?.isNotEmpty() == true) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Center),
                            painter = rememberImagePainter(campSpot.imageUri),
                            contentDescription = null
                        )
                    } else {
                        CampSpotImageItem(
                            imageId = R.drawable.touch_screen_image,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (permissions.all {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    it
                                ) == PackageManager.PERMISSION_GRANTED
                            }) {
                            viewModel.startLocationUpdates(context)
                        } else {
                            launcherMultiplePermissions.launch(permissions)
                        }
                    }
                ) {
                    Text(
                        text = "get location",
                        textAlign = TextAlign.Center
                    )
                }
                TitleAndTwoDataInRow(
                    isError = campSpot.campSpotFormErrors.isLocationDetailsError,
                    errorColor = errorColor,
                    titleId = R.string.label_gps,
                    firstLabel = R.string.label_latitude,
                    firstData = campSpot.locationDetails.latitude,
                    secondLabel = R.string.label_longitude,
                    secondData = campSpot.locationDetails.longitude,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.padding_small))
                )
                Divider()
                UserInputField(
                    text = campSpot.title,
                    labelId = R.string.label_title,
                    onValueChanged = onTitleChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isError = campSpot.campSpotFormErrors.isTitleError
                )
                UserInputField(
                    text = campSpot.description,
                    labelId = R.string.label_description,
                    onValueChanged = onDescriptionChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isError = campSpot.campSpotFormErrors.isDescriptionError
                )
                UserInputField(
                    text = campSpot.numberOfPeople,
                    labelId = R.string.label_numb_of_people,
                    onValueChanged = onNumberOfPeopleChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    isError = campSpot.campSpotFormErrors.isNumberOfPeopleError
                )
                ChooseDate(
                    dateDialogState = startDateDialogState,
                    labelId = R.string.label_event_starts,
                    buttonLabelId = R.string.label_pick_date,
                    date = localDateToString(campSpot.startEventDate),
                    onDateSelected = onStartDateSelected,
                    property = DateType.EVENT_DATE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small))
                )
                Divider()
                ChooseDate(
                    dateDialogState = endDateDialogState,
                    labelId = R.string.label_event_ends,
                    buttonLabelId = R.string.label_pick_date,
                    date = localDateToString(campSpot.endEventDate),
                    onDateSelected = onEndDateSelected,
                    property = DateType.EVENT_DATE,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small))
                )
                Divider()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small))
                ) {
                    CustomButton(
                        onButtonClick = onSaveSketchClicked,
                        textId = R.string.label_save_sketch
                    )
                    CustomButton(
                        onButtonClick = onPublishClicked,
                        textId = R.string.label_publish
                    )
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
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
    date : String,
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
        Text(
            text = stringResource(labelId),
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_small)))
        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = {
            dateDialogState.show()
        }) {
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
        modifier = modifier
    ) {
        Text(
            text = stringResource( textId ),
            style = MaterialTheme.typography.bodyMedium
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
                campSpot = LocalCampSpotDataProvider.DefaultCampSpot,
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