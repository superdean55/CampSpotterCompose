package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
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
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalCampSpotDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.network.SingleUserRepository
import hr.ferit.dejanmihic.campspottercompose.model.CampSpot
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.CampSpotDetailScreen
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.Graph
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.HomeScreen
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.UserDetailsScreen
import hr.ferit.dejanmihic.campspottercompose.ui.theme.LightBlue
import hr.ferit.dejanmihic.campspottercompose.ui.utils.localDateToString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogOutClicked: () -> Unit,
    onConfirmUserDeletion: () -> Unit,
    isDeleteButtonEnabled: Boolean,
    campSpotViewModel: CampSpotterViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
){
    val uiState by campSpotViewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val campSpotRepositoryState by SingleUserRepository.repositoryState.collectAsState()
    val canNavigateBack = if(backStackEntry != null) backStackEntry!!.arguments != null else false
    val context = LocalContext.current

    val selectedLanguageCode = campSpotViewModel.loadLanguagePreference(context)
    campSpotViewModel.setLocale(context, selectedLanguageCode)

    val navigationItemContentList = listOf(
        NavigationItemContent(
            campSpotType = CampSpotNavigationType.ALL_CAMP_SPOTS,
            icon = Icons.Default.Group,
            text = stringResource(id = R.string.nav_all_camp_spots)
        ),
        NavigationItemContent(
            campSpotType = CampSpotNavigationType.MY_CAMP_SPOTS,
            icon = Icons.Default.Person,
            text = stringResource(id = R.string.nav_my_camp_spots)
        ),
        NavigationItemContent(
            campSpotType = CampSpotNavigationType.SKETCHES,
            icon = Icons.Default.EditNote,
            text = stringResource(id = R.string.nav_my_sketches)
        ),
    )
    println("CURRENT BACK STACK")
    println(backStackEntry?.destination?.route)
    println(backStackEntry?.arguments)
    when(backStackEntry?.destination?.route){
        HomeScreen.BottomNavigation.route -> {
            campSpotViewModel.updateBottomNavigationVisibility(true)
            campSpotViewModel.updateTopAppBarUserImageVisibility(true)
        }
        HomeScreen.AddCampSpot.route ->{
            campSpotViewModel.updateBottomNavigationVisibility(false)
            campSpotViewModel.updateTopAppBarUserImageVisibility(false)
        }
        UserDetailsScreen.UserDetails.route ->{
            campSpotViewModel.updateBottomNavigationVisibility(false)
            campSpotViewModel.updateTopAppBarUserImageVisibility(false)
        }
        CampSpotDetailScreen.CampSpotDetails.route ->{
            campSpotViewModel.updateTopAppBarUserImageVisibility(true)
        }
    }
    if (campSpotRepositoryState.isUserDataRetrieved) {
        Scaffold(
            topBar = {
                TopAppBar(
                    canNavigateBack = canNavigateBack,
                    isUserImageHidden = uiState.isTopAppBarUserImageVisible,
                    navigateBack = { navController.popBackStack() },
                    onUserIconClicked = {
                        println("TOP_APP_BAR")
                        println(backStackEntry?.destination?.route)
                        navController.navigate(route = Graph.USER_DETAILS)
                    },
                    titleId = R.string.auth_title_app,
                    user = campSpotRepositoryState.user!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(MaterialTheme.colorScheme.inversePrimary)
                )
            },

            floatingActionButton = {
                if (uiState.isBottomNavigationVisible) {
                    AddCampSpot(onClick = {
                        campSpotViewModel.updateBottomNavigationVisibility(false)
                        campSpotViewModel.resetCampSpotValues()
                        navController.navigate(route = HomeScreen.AddCampSpot.route)
                    }
                    )
                }
            },
            bottomBar = {
                if (uiState.isBottomNavigationVisible) {
                    CampSpotterBottomNavigationBar(
                        currentTab = uiState.currentlySelectedNavType,
                        onTabPressed = {
                            campSpotViewModel.updateCurrentCampSpotType(it)
                        },
                        navigationItemContentList = navigationItemContentList,
                        modifier = Modifier.height(60.dp)
                    )
                }
            },
            modifier = modifier
        ) {
            println("PADDING VALUES\ntop: ${it.calculateTopPadding()}\nbottom: ${it.calculateBottomPadding()}")
            HomeNavGraph(
                onLogOutClicked = onLogOutClicked,
                navController = navController,
                campSpotterViewModel = campSpotViewModel,
                onConfirmUserDeletion = onConfirmUserDeletion,
                isDeleteButtonEnabled = isDeleteButtonEnabled,
                modifier = Modifier
                    .padding(it)
            )
        }
    }else{
        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator(
                modifier = modifier.align(Alignment.Center)
            )
        }

    }
}
@Composable
fun AddCampSpot(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SmallFloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.inversePrimary,
        contentColor = MaterialTheme.colorScheme.scrim,
        modifier = modifier
    ) {
        Icon(Icons.Filled.Add, "Small floating action button.")
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
    Row(
        modifier = modifier.clickable(onClick = onCardClick)
    ) {
        CampSpotImageItem(
            imageUrl = campSpot.imageUrl!!,
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
                text = campSpot.title!!,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            InformationComponent(
                labelId = R.string.label_participant,
                text = campSpot.numberOfPeople!!,
                modifier = Modifier.fillMaxWidth()
            )
            InformationComponent(
                labelId = R.string.label_publication_date,
                text = campSpot.publishDate!!,
                modifier = Modifier.fillMaxWidth()
            )
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
                userImageUrl = user.imageUrl!!,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.card_account_image_height))
                    .clip(CircleShape)
            )
            Text(
                text = user.username!!,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
@Composable
fun CampSpotImageItem(
    imageUrl: String,
    modifier: Modifier = Modifier
){
    ImageItem(
        placeholderImageId = R.drawable.no_image_available,
        imageUrl = imageUrl,
        modifier = modifier
    )
}
@Composable
fun PickImageItem(
    imageUrl: String,
    modifier: Modifier = Modifier
){
    ImageItem(
        placeholderImageId = R.drawable.touch_screen_image,
        imageUrl = imageUrl,
        modifier = modifier
    )
}
@Composable
fun ImageItem(
    @DrawableRes placeholderImageId: Int,
    imageUrl: String,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier){
        if (imageUrl != ""){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(placeholderImageId),
                alignment = Alignment.Center,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }else {
            Image(
                painter = painterResource(placeholderImageId),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
@Composable
fun UserImageItem(
    userImageUrl: String,
    modifier: Modifier = Modifier
){
    ImageItem(
        placeholderImageId = R.drawable.blank_profile_picture,
        imageUrl = userImageUrl,
        modifier = modifier
    )
}



fun dateToString(date: Date, format: String = "dd.MM.yyyy."): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(date)
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
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier,
    ){
        items(campSpots.reversed(), key = { campSpot -> campSpot.id!! }) { campSpot ->
            CampSpotItem(
                campSpot = campSpot,
                user = getUserById(users, campSpot.userId!!) ?: LocalUserDataProvider.getUsersData()[0],
                onCardClick = { onCampSpotClick(campSpot) },
                modifier = Modifier
                    .fillMaxWidth()
                    .size(dimensionResource(R.dimen.card_image_height))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }

}
fun getUserById(users: List<User>, userId: String): User? {
    return users.find { it.uid == userId }
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
                    .size(dimensionResource(R.dimen.card_image_height))
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