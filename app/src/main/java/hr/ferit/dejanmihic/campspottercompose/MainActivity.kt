package hr.ferit.dejanmihic.campspottercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.ferit.dejanmihic.campspottercompose.ui.screens.Background
import hr.ferit.dejanmihic.campspottercompose.ui.screens.CampSpotterApp
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            CampSpotterComposeTheme {
                CampSpotterApp()
            }
        }

    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CampSpotterComposeTheme {

    }
}