package com.example.rateyourgame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.rateyourgame.R
import com.example.rateyourgame.ViewModels.AuthViewModel
import com.example.rateyourgame.ViewModels.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController,  authViewModel: AuthViewModel, sharedViewModel: SharedViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusRequesterPassword = FocusRequester()
    var showError by remember { mutableStateOf(false) }

    val successMessage = sharedViewModel.successMessage.collectAsState().value

    DisposableEffect(username, password) {
        onDispose {
            sharedViewModel.setSuccessMessage("")
        }
    }

    if (successMessage?.isNotEmpty() == true) {
        AlertDialog(
            onDismissRequest = {
                sharedViewModel.setSuccessMessage("")
            },
            title = {
                Text(text = "Success")
            },
            text = {
                Text(text = successMessage)
            },
            confirmButton = {
                Button(
                    onClick = {
                        sharedViewModel.setSuccessMessage("")
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
    val shapeis = if (isSystemInDarkTheme()) painterResource(id = R.drawable.loginshapenight) else painterResource(
        id = R.drawable.loginshape)

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            contentAlignment = Alignment.TopCenter, modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = shapeis,
                contentDescription = "loginshape",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )

            Image(
                painterResource(id = R.drawable.rateyourgamelogo),
                contentDescription = "rateyourgameloginlogo",
                modifier = Modifier
                    .size(225.dp)
                    .padding(top = 50.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", fontSize = 30.sp, fontWeight = FontWeight.Bold,)
            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusRequesterPassword.requestFocus()
                    }
                ),
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .focusRequester(focusRequesterPassword),
                visualTransformation = PasswordVisualTransformation()
            )
            if (showError) {
                AlertDialog(
                    onDismissRequest = {
                        showError = false
                    },
                    title = {
                        Text(text = "Couldn't log in")
                    },
                    text = {
                        Text("Username or Password is wrong. Try again.")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showError = false
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

            Button(
                onClick = {
                    authViewModel.viewModelScope.launch {
                        val user = authViewModel.login(username, password)
                        if (user != null) {
                            authViewModel.setUser(user)

                            navController.navigate("game_list_screen")
                        } else {
                            showError = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            )

            {
                Spacer(modifier = Modifier.height(30.dp))
                Text("Login")
            }
            Text("Don't have an account?")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate("signup_screen")
                }
            ) {
                Text("Sign up")
            }
        }

    }
}
