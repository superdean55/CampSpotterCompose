package hr.ferit.dejanmihic.campspottercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var navController: NavHostController
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        //FirebaseApp.initializeApp(this)
        setContent {
            CampSpotterComposeTheme {
                // A surface container using the 'background' color from the theme


                val viewModel : CampSpotterViewModel = viewModel()
                navController = rememberNavController()
                this.navController = navController
                var startDestination: String = if(auth.currentUser != null){
                    Screen.MainScreen.route
                }else{
                    Screen.LoginScreen.route
                }
                /*LaunchedEffect(Unit) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        // Korisnik je već prijavljen
                        viewModel.user.value = auth.currentUser?.email.toString()
                        navigateToMainScreen()
                    }else{

                    }
                }*/
                SetupNavGraph(navController = navController, viewModel = viewModel, startDestination )

            }
        }

    }

    private fun navigateToMainScreen() {
        navController.navigate(route = Screen.MainScreen.route)
    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CampSpotterComposeTheme {
        BeckgroundScreen({ LoginScreen(navController = rememberNavController(), viewModel = viewModel()) })
    }
}