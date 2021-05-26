package me.rerere.polymartapp.ui.route

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.rerere.polymartapp.R
import me.rerere.polymartapp.ui.component.Avatar
import me.rerere.polymartapp.ui.theme.POLYMART_COLOR_DARKER
import me.rerere.polymartapp.util.currentVisualPage
import me.rerere.polymartapp.util.unread

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun IndexPage(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = 3)

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
                },
                onClickMessage = {
                    navController.navigate("message")
                }
            )
        },
        drawerContent = {
            IndexDrawer(navController)
        },
        bottomBar = {
            BottomBar(pagerState)
        }
    ) {
        Content(pagerState)
    }
}

@ExperimentalPagerApi
@Composable
fun Content(pagerState: PagerState){
    HorizontalPager(modifier = Modifier.fillMaxWidth(), state = pagerState) {

    }
}

@Composable
fun TopBar(onClickNavigationIcon: () -> Unit, onClickSearch: () -> Unit, onClickMessage: ()->Unit) {
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
                    onClickMessage()
                }) {
                    Icon(
                        modifier = Modifier.unread(show),
                        imageVector = Icons.Default.Message,
                        contentDescription = "Messages"
                    )
                }
            }
        }
    )
}

@ExperimentalPagerApi
@Composable
fun BottomBar(pagerState: PagerState){
    val coroutineScope = rememberCoroutineScope()
    BottomNavigation(
        modifier = Modifier.navigationBarsPadding()
    ) {
        // Resource
        BottomNavigationItem(
            selected = pagerState.currentVisualPage == 0,
            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
            icon = {
                Icon(painterResource(R.drawable.resource), null)
            },
            label = {
                Text(text = "Resource")
            }
        )
        // Servers
        BottomNavigationItem(
            selected = pagerState.currentVisualPage == 1,
            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
            icon = {
                Icon(painterResource(R.drawable.servers), null)
            },
            label = {
                Text(text = "Server")
            }
        )
        // Servers
        BottomNavigationItem(
            selected = pagerState.currentVisualPage == 2,
            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(2) } },
            icon = {
                Icon(painterResource(R.drawable.forum), null)
            },
            label = {
                Text(text = "Forum")
            }
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun IndexDrawer(navController: NavController) {
    // Profile
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .statusBarsPadding()
            .background(MaterialTheme.colors.primarySurface),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
            navController.navigate("login")
        }) {
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

    // Navigation Items
    Column(Modifier.fillMaxSize()) {
        // My Resource
        ListItem(
            modifier = Modifier.clickable {
                navController.navigate("my_resource")
            },
            icon = {
                Icon(painterResource(R.drawable.my_resource), "My Resources")
            },
            text = {
                Text("My Resources")
            }
        )

        // WishList
        ListItem(
            modifier = Modifier.clickable {
                navController.navigate("wishlist")
            },
            icon = {
                Icon(painterResource(R.drawable.wishlist), "Wishlist")
            },
            text = {
                Text("Wishlist")
            }
        )

        // About
        ListItem(
            modifier = Modifier.clickable {
                navController.navigate("about")
            },
            icon = {
                Icon(Icons.Default.Info, "About")
            },
            text = {
                Text("About")
            }
        )
    }
}