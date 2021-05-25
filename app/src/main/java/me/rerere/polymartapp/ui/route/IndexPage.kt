package me.rerere.polymartapp.ui.route

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import me.rerere.polymartapp.ui.component.Avatar
import me.rerere.polymartapp.ui.theme.POLYMART_COLOR_DARKER
import me.rerere.polymartapp.util.unread

@Composable
fun IndexPage(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                onClickNavigationIcon = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onClickSearch = {
                    navController.navigate("search")
                }
            )
        },
        drawerContent = {
            IndexDrawer()
        }
    ) {

    }
}

@Composable
fun TopBar(onClickNavigationIcon: () -> Unit, onClickSearch: () -> Unit) {
    TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        title = {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = POLYMART_COLOR_DARKER,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("P")
                }
                append("olymart")
                withStyle(style = SpanStyle(fontSize = 15.sp)) {
                    append(".org")
                }
            })
        },
        navigationIcon = {
            IconButton(onClick = {
                onClickNavigationIcon()
            }) {
                Avatar(
                    modifier = Modifier.size(30.dp),
                    avatarUrl = "https://s3.amazonaws.com/polymart.user.profilepictures/small/275"
                )
            }
        },
        actions = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    onClickSearch()
                }) {
                    Icon(Icons.Default.Search, "Search")
                }

                var show by remember {
                    mutableStateOf(true)
                }
                IconButton(onClick = {
                    show = !show
                }) {
                    Icon(
                        modifier = Modifier.unread(show),
                        imageVector = Icons.Default.Message,
                        contentDescription = "Notifications"
                    )
                }
            }
        }
    )
}

@Composable
fun IndexDrawer() {
    // Profile
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .statusBarsPadding()
            .background(MaterialTheme.colors.primarySurface),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            Avatar(
                modifier = Modifier.size(100.dp),
                avatarUrl = "https://s3.amazonaws.com/polymart.user.profilepictures/small/275"
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .widthIn(max = 150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Nickname
                Text(
                    text = "RE_OVO",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Biography
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = "Biography Biography Biography Biography", color = Color.White)
                }
            }
        }
    }
}