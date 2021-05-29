package me.rerere.polymartapp.ui.route

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.vanpra.composematerialdialogs.*
import me.rerere.polymartapp.R
import me.rerere.polymartapp.ui.viewmodel.LoginViewModel

@Composable
fun LoginPage(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(text = "Login")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsWithImePadding(),
            contentAlignment = Alignment.Center
        ) {
            LoginForm(loginViewModel, navController)
        }
    }
}

@Composable
private fun LoginForm(loginViewModel: LoginViewModel, navController: NavController) {
    // Progress Dialog
    val dialog = remember {
        MaterialDialog()
    }
    dialog.build {
        iconTitle(text = "Trying to login", icon = {
            CircularProgressIndicator(modifier = Modifier.size(30.dp))
        })
        message("Please wait for a while...")
    }
    // Failed Dialog
    val failedDialog = remember {
        MaterialDialog()
    }
    failedDialog.build {
        title("Failed to login")
        message("Please check your account and password!")
        buttons {
            button("Ok") {
                failedDialog.hide()
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // LOGO
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = "LOGO"
        )

        // Space
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
        )

        // Title
        Text(text = "Login into Polymart", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        // Space
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
        )

        // Username
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = loginViewModel.username,
            onValueChange = {
                loginViewModel.username = it
            },
            label = {
                Text(text = "Username or Email")
            },
            maxLines = 1
        )

        // Password
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = loginViewModel.password,
            onValueChange = {
                loginViewModel.password = it
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(text = "Password")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            maxLines = 1
        )

        // Spacer
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        )

        // Login Button
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(horizontal = 16.dp), onClick = {
            // show progress dialog
            dialog.show()
            loginViewModel.login {
                // hide progress dialog
                dialog.hide()
                // handle result
                if (it) {
                    navController.popBackStack()
                } else {
                    failedDialog.show()
                }
            }
        }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Login, "login icon")
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Login",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Spacer
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        // Register / Forgot Password
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 4.dp), onClick = { /*TODO*/ }) {
                Text(text = "Get an account")
            }

            Button(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 4.dp), onClick = { /*TODO*/ }) {
                Text(text = "Forgot Password")
            }
        }

        // Spacer
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        )

        // Note
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = "Two-step verification login is not supported temporarily",
                textAlign = TextAlign.Center
            )
        }
    }
}