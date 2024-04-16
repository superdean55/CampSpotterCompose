package hr.ferit.dejanmihic.campspottercompose.ui.screens

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.HomeNavGraph
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import hr.ferit.dejanmihic.campspottercompose.data.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.Graph
import hr.ferit.dejanmihic.campspottercompose.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogOutClicked: () -> Unit,
    campSpotViewModel: CampSpotterViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val canNavigateBack = if(backStackEntry != null) backStackEntry!!.arguments != null else false

    Scaffold(
        topBar = {
                 TopAppBar(
                     canNavigateBack = canNavigateBack,
                     navigateBack = { navController.popBackStack() },
                     onUserIconClicked = {
                            navController.navigate(route = Graph.USER_DETAILS)
                     },
                     titleId = R.string.auth_title_app,
                     userImageUri = Uri.EMPTY,
                     modifier = Modifier
                         .fillMaxWidth()
                         .height(70.dp)
                         .background(LightBlue)
                 )
        },
        modifier = modifier
    ) {
        Column {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            HomeNavGraph(
                onLogOutClicked = onLogOutClicked,
                navController = navController,
                campSpotterViewModel = campSpotViewModel,
                modifier = Modifier.padding(it)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampSpotItem(
    campSpot: CampSpot,
    user: User,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card (
        elevation = CardDefaults.cardElevation(),
        shape = RoundedCornerShape(
            topStart = dimensionResource(R.dimen.card_corner_radius),
            bottomEnd = dimensionResource(R.dimen.card_corner_radius)
        ),
        onClick = onCardClick,
        modifier = modifier
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(dimensionResource(R.dimen.card_image_height))
        ) {
            CampSpotImageItem(
                imageId = campSpot.imageId,
                modifier = Modifier.size(dimensionResource(R.dimen.card_image_height))
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(dimensionResource(R.dimen.padding_small))
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = campSpot.title,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Column(){
                    Text(
                        text = stringResource(R.string.label_location),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = dataToString("Borovik"),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                Column(){
                    Text(
                        text = stringResource(R.string.label_date),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = dataToString(campSpot.publishDate),
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }


            }

            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(dimensionResource(R.dimen.card_account_image_container_width))
                    .padding(dimensionResource(R.dimen.padding_small))
                    .fillMaxHeight()
            ) {
                UserImageItem(
                    userImageUri = user.image,
                    modifier = Modifier

                        .size(dimensionResource(R.dimen.card_account_image_height))
                        .clip(CircleShape)
                )
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}
@Composable
fun CampSpotImageItem(
    @DrawableRes imageId: Int,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier){
        Image(
            painter = painterResource(imageId),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@Composable
fun UserImageItem(
    userImageUri: Uri,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier){
        if(userImageUri != Uri.EMPTY) {
            AsyncImage(model = userImageUri, contentDescription = null)
        }else{
            Image(
                painter = painterResource(R.drawable.blank_profile_picture),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

fun <T>dataToString(data: T) :String{
    val text = when(data){
        is LocalDate -> localDateToString(data)
        is Date -> dateToString(data)
        is Int -> data.toString()
        is Float -> data.toString()
        is Double -> data.toString()
        is String -> data
        else -> "unknown"
    }
    return text
}

fun dateToString(date: Date, format: String = "dd.MM.yyyy."): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(date)
}
fun localDateToString(localDate: LocalDate, format: String = "dd.MM.yyyy."): String{
    return DateTimeFormatter.ofPattern("dd.MM.yyyy.").format(localDate)
}
@Composable
fun CampSpotsList(
    campSpots: List<CampSpot>,
    users: List<User>,
    onCampSpotClick: (CampSpot) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
){
    LazyColumn(
        contentPadding= contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier,
    ){
        items(campSpots, key = { campSpot -> campSpot.id }) { campSpot ->
            CampSpotItem(
                campSpot = campSpot,
                user = getUserById(users = users, campSpot.userId) ?: LocalUserDataProvider.getUsersData()[0],
                onCardClick = { onCampSpotClick(campSpot) }
            )
        }
    }
}
fun getUserById(users: List<User>, userId: Long): User? {
    return users.find { it.id == userId }
}
@Preview(showBackground = true)
@Composable
fun CampSpotItemPreview(){
    CampSpotterComposeTheme {
        Surface {
            CampSpotItem(
                campSpot = LocalCampSpotDataProvider.getCampSpots()[0],
                user = LocalUserDataProvider.getUsersData()[0],
                onCardClick = {  },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampSpotListPreview(){
    CampSpotterComposeTheme {
        Surface {
            CampSpotsList(
                campSpots = LocalCampSpotDataProvider.getCampSpots(),
                users = LocalUserDataProvider.getUsersData(),
                onCampSpotClick = {  },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}