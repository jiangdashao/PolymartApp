package me.rerere.polymartapp.ui.route

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun SearchPage(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                onSearch = {},
                onBack = {navController.popBackStack()}
            )
        }
    ) {

    }
}

@Composable
fun TopBar(navController: NavController, onSearch: (content: String) -> Unit, onBack: ()->Unit) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = onBack ) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        },
        title = {
            SearchBar(onSearch)
        }
    )
}

@Composable
fun SearchBar(onSearch: (content: String) -> Unit) {
    var content by remember {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    Box(modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colors.primaryVariant)
                .padding(horizontal = 15.dp, vertical = 1.dp)
        ) {
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = content,
                    onValueChange = {
                        content = it
                    },
                    singleLine = true,
                    textStyle = TextStyle.Default.copy(fontSize = 13.sp,color = Color.White),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                    })
                )
                if(content.isNotEmpty()){
                    IconButton(onClick = {
                        content = ""
                    }) {
                        Icon(Icons.Default.Clear, "Clear search content")
                    }
                }
            }
        }
    }
}