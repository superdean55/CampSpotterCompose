package hr.ferit.dejanmihic.campspottercompose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hr.ferit.dejanmihic.campspottercompose.ui.theme.LightBlue

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: AppViewModel
){
    viewModel.navController = navController as NavHostController
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier
                .clip(RoundedCornerShape(size = 10.dp))
                .background(LightBlue)
            ){
                Text(text = "Log Out",
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            viewModel.onAction(AppActions.UserLogOut)
                        }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Main Screen",
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = viewModel.user.value)
        }

    }
}
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen(){
    MainScreen(navController = rememberNavController(), viewModel = AppViewModel())
}