package me.rerere.polymartapp.ui.route

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import me.rerere.polymartapp.ui.component.BasicNavTopBar

@Composable
fun AboutPage(navController: NavController){
    Scaffold(
        topBar = {
            BasicNavTopBar(navController, "About")
        }
    ) {

    }
}