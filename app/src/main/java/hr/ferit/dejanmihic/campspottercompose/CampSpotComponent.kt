package hr.ferit.dejanmihic.campspottercompose


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.dejanmihic.campspottercompose.ui.theme.DarkBlue
import hr.ferit.dejanmihic.campspottercompose.ui.theme.LightBlue

@Composable
fun CampSpotComponent(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(size = 10.dp))
        .background(LightBlue)
        .border(BorderStroke(3.dp, DarkBlue))
        .padding(10.dp)

    ){
        Surface(modifier = Modifier
            .fillMaxWidth(),

        ) {
            Row() {
                Column(modifier = Modifier
                    .weight(2f)
                ) {
                    Text(text = "dejan", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        modifier = Modifier.height(60.dp),
                        painter = painterResource(id = R.drawable.slika),
                        contentDescription ="bla" )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(4f)){
                    Row() {
                        Text(text = "početak: ", fontSize = 15.sp)
                        Text(text = "5.10.2023", fontSize = 15.sp)
                    }
                    Row() {
                        Text(text = "kraj: ", fontSize = 15.sp)
                        Text(text = "10.10.2023", fontSize = 15.sp)
                    }
                }
            }

        }


    }

}

@Preview(showBackground = true)
@Composable
fun PreviewCampSpotComponent(){
    CampSpotComponent()
}