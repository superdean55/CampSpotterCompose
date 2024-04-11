package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme

@Composable
fun DetailProfileCard(
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
                CampSpotImageItem(
                    imageId = R.drawable.person,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.card_image_height))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
                )
                VerticalLabelTextInfo(
                    labelId = R.string.label_username,
                    data = "deos",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_firstname,
                data = "Dejan",
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_lastname,
                data = "Mihić",
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_date_of_birth,
                data = "26.01.1990",
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
            HorizontalLabelTextInfo(
                labelId = R.string.label_registration_date,
                data = "26.01.2022",
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
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Log out")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ }
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
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}