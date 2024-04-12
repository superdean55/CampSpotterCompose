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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hr.ferit.dejanmihic.campspottercompose.ui.CampSpotterViewModel
import hr.ferit.dejanmihic.campspottercompose.ui.theme.DarkBlue
import hr.ferit.dejanmihic.campspottercompose.ui.theme.MediumBlue

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: CampSpotterViewModel
){
    //val viewModel = viewModel<AppViewModel>()
    viewModel.context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxSize()
    ){
        Column() {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
                contentAlignment = Alignment.Center
            ){
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(size = 15.dp))

                ) {
                    Text(
                        modifier = Modifier
                            .background(MediumBlue)
                            .padding(all = 15.dp)
                            .background(MediumBlue),
                        text = "Camp Spotter",
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic,
                        color = DarkBlue
                    )
                }
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
                contentAlignment = Alignment.Center
            ){
                /*var email by remember {
                    mutableStateOf("")
                }
                var password by remember {
                    mutableStateOf("")
                }*/
                val keyboardController = LocalSoftwareKeyboardController.current
                var passwordVisibility by remember {
                    mutableStateOf(false)
                }
                var visible: VisualTransformation by remember {
                    mutableStateOf(PasswordVisualTransformation())
                }

                val icon = if(passwordVisibility){
                    painterResource(id = R.drawable.baseline_visibility_24)


                }else{
                    painterResource(id = R.drawable.baseline_visibility_off_24)

                }
                //val firebaseAuth = FirebaseAuth.getInstance()
                //val context = LocalContext.current
                Column(){
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        Text(
                            modifier = Modifier,
                            text = "Create account",
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.email.value,
                        onValueChange = {
                            viewModel.email.value = it
                        },
                        label = {
                            Text(text = "Email", color = Color.White)
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Email, contentDescription = "Email icon", tint = Color.White)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.password.value,
                        onValueChange = {
                            viewModel.password.value = it
                        },
                        label = {
                            Text(text = "Password", color = Color.White)
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Lock, contentDescription = "Email icon", tint = Color.White)
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility = !passwordVisibility
                                if(passwordVisibility){
                                    visible = VisualTransformation.None
                                }else{
                                    visible = PasswordVisualTransformation()
                                }
                            }) {
                                Icon(painter = icon, contentDescription = "Visibility icon")
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            focusedLabelColor = Color.White,
                            cursorColor = Color.White
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        visualTransformation = visible
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(size = 10.dp))
                        .background(DarkBlue),
                        contentAlignment = Alignment.Center

                    ){
                        Text(
                            modifier = Modifier
                                .padding(all = 10.dp)
                                .clickable {
                                    viewModel.onAction(AppActions.CreateAccount)
                                },
                            text = "Continue",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column() {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "you have an account?",
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate(route = Screen.LoginScreen.route)
                                    },
                                text = "Log In",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen(){
    BeckgroundScreen({ RegisterScreen(navController = rememberNavController(), viewModel = CampSpotterViewModel()) })
}