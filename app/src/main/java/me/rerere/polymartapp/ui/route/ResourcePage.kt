package me.rerere.polymartapp.ui.route

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import me.rerere.polymartapp.ui.component.BasicNavTopBar
import me.rerere.polymartapp.ui.viewmodel.ResourceViewModel

@Composable
fun ResourcePage(navController: NavController, resourceId: Int, resourceViewModel: ResourceViewModel = hiltViewModel()){
    LaunchedEffect(Unit){
        println("Loading $resourceId")
    }

    Scaffold(
        topBar = {
            ResourceTopBar(navController, resourceId)
        }
    ) {

    }
}

@Composable
private fun ResourceTopBar(navController: NavController, resourceId: Int){
    BasicNavTopBar(navController, "Resource $resourceId")
}