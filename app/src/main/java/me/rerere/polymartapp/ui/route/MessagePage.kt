package me.rerere.polymartapp.ui.route

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun MessagePage(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                title = {
                    Text("Messages")
                }
            )
        }
    ) {

    }
}