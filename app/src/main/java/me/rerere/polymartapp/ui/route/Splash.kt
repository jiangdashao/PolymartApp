package me.rerere.polymartapp.ui.route

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import me.rerere.polymartapp.ui.theme.POLYMART_COLOR_DARKER
import me.rerere.polymartapp.ui.viewmodel.SplashViewModel

@Composable
fun SplashPage(navController: NavController, splashViewModel: SplashViewModel = hiltViewModel()) {
    // delay 2 seconds, then go to index route
    LaunchedEffect(Unit) {
        splashViewModel.checkCookie()
        delay(1500)
        navController.navigate("index"){
            popUpTo("splash"){
                inclusive = true
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.primarySurface), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 65.sp, color = POLYMART_COLOR_DARKER, fontWeight = FontWeight.Bold)){
                    append("P")
                }
                withStyle(style = SpanStyle(fontSize = 65.sp, color = Color.Black, fontWeight = FontWeight.Bold)){
                    append("olymart")
                }
            })

            Text(text = "Polymart.org", style = MaterialTheme.typography.h6, color = Color.White)
        }
    }
}