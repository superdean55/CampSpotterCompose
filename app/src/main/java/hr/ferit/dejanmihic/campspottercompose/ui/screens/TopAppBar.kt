package hr.ferit.dejanmihic.campspottercompose.ui.screens

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme

@Composable
fun TopAppBar(
    canNavigateBack: Boolean,
    isUserImageHidden: Boolean,
    navigateBack: () -> Unit,
    onUserIconClicked: (User) -> Unit,
    @StringRes titleId: Int,
    user: User,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        dimensionResource(R.dimen.padding_small)
                    )
            ) {
                if (canNavigateBack) {
                    IconButton(
                        onClick = navigateBack,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, shape = CircleShape),
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.top_app_bar_navigation_back)
                        )
                    }
                }
                Text(
                    text = stringResource(titleId),
                    style = MaterialTheme.typography.titleLarge
                )
                if(isUserImageHidden) {
                    TopAppBarUserImage(
                        imageUrl = user.imageUrl,
                        onUserIconClicked = { onUserIconClicked(user) },
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)

                    )
                }
            }

    }
}
@Composable
fun TopAppBarUserImage(
    imageUrl: String?,
    onUserIconClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onUserIconClicked,
        modifier = modifier.padding(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (imageUrl == ""){
            Image(
                painter = painterResource(R.drawable.blank_profile_picture),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(R.drawable.blank_profile_picture),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview(){
    CampSpotterComposeTheme {
        Surface {
            TopAppBar(
                titleId = R.string.auth_title_app,
                user = LocalUserDataProvider.defaultUser,
                canNavigateBack = true,
                isUserImageHidden = false,
                navigateBack = {},
                onUserIconClicked = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.top_app_bar_height))
            )
        }
    }

}
