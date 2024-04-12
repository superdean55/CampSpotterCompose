package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampSpotItem(
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
                imageId = R.drawable.camp_spot_image_1,
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
                    text = stringResource(R.string.camp_spot_title_1),
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
                        text = dataToString("26.01.2022"),
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
                CampSpotImageItem(
                    imageId = R.drawable.person,
                    modifier = Modifier

                        .size(dimensionResource(R.dimen.card_account_image_height))
                        .clip(CircleShape)
                )
                Text(
                    text = "deos",
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

@Preview(showBackground = true)
@Composable
fun CampSpotItemPreview(){
    CampSpotterComposeTheme {
        Surface {
            CampSpotItem(
                onCardClick = {  },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}