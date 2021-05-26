package me.rerere.polymartapp.ui.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun BasicNavTopBar(navController: NavController, title: String){
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Rounded.ArrowBack, "Back")
            }
        }
    )
}