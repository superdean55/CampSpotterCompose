package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.annotation.DimenRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.dejanmihic.campspottercompose.R
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalMessageDataProvider
import hr.ferit.dejanmihic.campspottercompose.data.local.LocalUserDataProvider
import hr.ferit.dejanmihic.campspottercompose.model.Message
import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme

@Composable
fun MessageData(
    messageText: String,
    modifier: Modifier = Modifier
){
    Box(modifier = modifier){
        Text(
            text = messageText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
@Composable
fun AnotherUserMessageData(
    @DimenRes messageDataPaddingId : Int,
    @DimenRes leftPaddingId: Int,
    @DimenRes cornerRadiusId: Int,
    messageBackgroundColor: Color,
    messageText: String,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
    ){
        Spacer(modifier = Modifier.width(dimensionResource(leftPaddingId)))
        MessageData(
            messageText = messageText,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = dimensionResource(cornerRadiusId),
                        bottomStart = dimensionResource(cornerRadiusId),
                        bottomEnd = dimensionResource(cornerRadiusId)
                    )
                )
                .background(messageBackgroundColor)
                .padding(dimensionResource(messageDataPaddingId))
        )
    }
}
@Composable
fun AnotherUserMessageInformation(
    @DimenRes imageSizeId: Int,
    userImageUrl: String,
    username: String,
    postDate: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
    ) {
        UserImageItem(
            userImageUrl = userImageUrl,
            modifier = Modifier
                .size(dimensionResource(imageSizeId))
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        MessageUserInformation(
            username = username,
            postDate = postDate,
            textAlign = TextAlign.Start,
            modifier = modifier.weight(1f)
        )
    }
}
@Composable
fun AnotherUserMessageCard(
    messageDataWidth: Float,
    @DimenRes imageSizeId: Int,
    @DimenRes cornerRadiusId: Int,
    @DimenRes messageDataPaddingId: Int,
    messageBackgroundColor: Color,
    userImageUrl: String,
    username: String,
    postDate: String,
    messageText: String,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
    ){
        Column(
            modifier = Modifier.weight(messageDataWidth)
        ){
            AnotherUserMessageInformation(
                userImageUrl = userImageUrl,
                username = username,
                postDate = postDate,
                imageSizeId = imageSizeId,
                modifier = Modifier.fillMaxWidth()
            )
            AnotherUserMessageData(
                messageDataPaddingId = messageDataPaddingId,
                leftPaddingId = imageSizeId,
                cornerRadiusId = cornerRadiusId,
                messageBackgroundColor = messageBackgroundColor,
                messageText = messageText
            )
        }
        Spacer(modifier = Modifier.weight(1.0f - messageDataWidth))
    }
}
@Composable
fun MessageUserInformation(
    textAlign: TextAlign,
    username: String,
    postDate: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        Text(
            text = username,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            textAlign = textAlign,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = postDate,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            textAlign = textAlign,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CurrentUserMessageData(
    @DimenRes messageDataPaddingId : Int,
    @DimenRes rightPaddingId: Int,
    @DimenRes cornerRadiusId: Int,
    messageBackgroundColor: Color,
    messageText: String,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
    ){
        Row (
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.weight(1f)
        ){
            MessageData(
                messageText = messageText,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = dimensionResource(cornerRadiusId),
                            topEnd = 0.dp,
                            bottomStart = dimensionResource(cornerRadiusId),
                            bottomEnd = dimensionResource(cornerRadiusId)
                        )
                    )
                    .background(messageBackgroundColor)
                    .padding(dimensionResource(messageDataPaddingId))
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(rightPaddingId)))
    }
}
@Composable
fun CurrentUserMessageInformation(
    @DimenRes imageSizeId: Int,
    userImageUrl: String,
    username: String,
    postDate: String,
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = modifier
    ) {
        MessageUserInformation(
            username = username,
            postDate = postDate,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
        UserImageItem(
            userImageUrl = userImageUrl,
            modifier = Modifier
                .size(dimensionResource(imageSizeId))
                .clip(CircleShape)
        )
    }
}
@Composable
fun CurrentUserMessageCard(
    messageDataWidth: Float,
    @DimenRes imageSizeId: Int,
    @DimenRes cornerRadiusId: Int,
    @DimenRes messageDataPaddingId: Int,
    messageBackgroundColor: Color,
    userImageUrl: String,
    username: String,
    postDate: String,
    messageText: String,
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier
    ){
        Box(modifier = Modifier.weight(1.0f - messageDataWidth)){

        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(messageDataWidth)
        ){
            CurrentUserMessageInformation(
                userImageUrl = userImageUrl,
                username = username,
                postDate = postDate,
                imageSizeId = imageSizeId,
                modifier = Modifier.fillMaxWidth()
            )
            CurrentUserMessageData(
                messageDataPaddingId = messageDataPaddingId,
                rightPaddingId = imageSizeId,
                cornerRadiusId = cornerRadiusId,
                messageBackgroundColor = messageBackgroundColor,
                messageText = messageText,

            )
        }
    }
}

@Composable
fun MessageCard(
    messageDataWidth: Float = 0.8f,
    @DimenRes imageSizeId: Int = R.dimen.message_user_image_size,
    @DimenRes cornerRadiusId: Int = R.dimen.message_corner_radius,
    @DimenRes messageDataPaddingId: Int = R.dimen.message_data_padding,
    user: User,
    message: Message,
    modifier: Modifier = Modifier
){
    if (FirebaseAuth.getInstance().currentUser?.uid == message.userId) {
        CurrentUserMessageCard(
            messageDataWidth = messageDataWidth,
            imageSizeId = imageSizeId,
            cornerRadiusId = cornerRadiusId,
            messageDataPaddingId = messageDataPaddingId,
            messageBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
            userImageUrl = user.imageUrl!!,
            username = user.username!!,
            postDate = message.postDate!!,
            messageText = message.message!!,
            modifier = modifier
        )
    }else{
        AnotherUserMessageCard(
            messageDataWidth = messageDataWidth,
            imageSizeId = imageSizeId,
            cornerRadiusId = cornerRadiusId,
            messageDataPaddingId = messageDataPaddingId,
            messageBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            userImageUrl = user.imageUrl!!,
            username = user.username!!,
            postDate = message.postDate!!,
            messageText = message.message!!,
            modifier = modifier
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesCard(
    messages: List<Message>,
    users: List<User>,
    sendMessageText: String,
    onSendMessageTextChanged: (String) -> Unit,
    onSendMessageClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.inversePrimary)
        ){
            Text(text = "Messages", style = MaterialTheme.typography.labelMedium)
        }
        Row(modifier = Modifier.weight(1f)) {
            MessagesList(
                messages = messages,
                users = users,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        ) {
            OutlinedTextField(
                value = sendMessageText,
                onValueChange = { onSendMessageTextChanged(it) },
                label = {
                    Text("enter message")
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
            OutlinedButton(
                onClick = onSendMessageClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.width(80.dp).height(55.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                )
            }
        }
    }

}
@Composable
fun MessagesList(
    messages: List<Message>,
    users: List<User>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
){
    LazyColumn(
        contentPadding= contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier
    ){
        items(messages, key = { message -> message.id!! }) { message ->
            users.find { it.uid == message.userId }?.let {
                MessageCard(
                    user = it,
                    message = message
                )
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun MessageDataPreview(){
    CampSpotterComposeTheme {
        Surface {
            MessageData(
                messageText = "Example text",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = dimensionResource(R.dimen.padding_medium),
                            bottomStart = dimensionResource(R.dimen.padding_medium),
                            bottomEnd = dimensionResource(R.dimen.padding_medium)
                        )
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnotherMessageUserInformationPreview(){
    CampSpotterComposeTheme {
        Surface {
            AnotherUserMessageInformation(
                userImageUrl = "",
                username = "dejan",
                postDate = "01.01.2024. 12:00",
                imageSizeId = R.dimen.message_user_image_size,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CurrentMessageUserInformationPreview(){
    CampSpotterComposeTheme {
        Surface {
            CurrentUserMessageInformation(
                userImageUrl = "",
                username = "dejan",
                postDate = "22.02.2022. 14:00",
                imageSizeId = R.dimen.message_user_image_size,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnotherUSerMessageCardPreview(){
    CampSpotterComposeTheme {
        Surface {
            AnotherUserMessageCard(
                messageDataWidth = 0.8f,
                imageSizeId = R.dimen.message_user_image_size,
                messageDataPaddingId = R.dimen.message_data_padding,
                cornerRadiusId = R.dimen.message_corner_radius,
                messageBackgroundColor = MaterialTheme.colorScheme.primaryContainer,
                userImageUrl = "",
                username = "dejan",
                postDate = "01.01.2024. 12:00",
                messageText = "Where are you?",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentUserMessageCardPreview(){
    CampSpotterComposeTheme {
        Surface {
            CurrentUserMessageCard(
                messageDataWidth = 0.8f,
                imageSizeId = R.dimen.message_user_image_size,
                messageDataPaddingId = R.dimen.message_data_padding,
                cornerRadiusId = R.dimen.message_corner_radius,
                messageBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                userImageUrl = "",
                username = "dejan",
                postDate = "22.02.2022. 14:00",
                messageText = "I am here...",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesListPreview(){
    CampSpotterComposeTheme {
        Surface {
            MessagesList(
                messages = LocalMessageDataProvider.getMessages(),
                users = LocalUserDataProvider.getUsersData(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                    .background(Color.Gray)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessagesCardPreview(){
    CampSpotterComposeTheme {
        Surface {
                MessagesCard(
                    messages = LocalMessageDataProvider.getMessages(),
                    users = LocalUserDataProvider.getUsersData(),
                    sendMessageText = "",
                    onSendMessageTextChanged = {},
                    onSendMessageClicked = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.inversePrimary)
                        .padding(dimensionResource(R.dimen.padding_small))
                )
        }
    }
}