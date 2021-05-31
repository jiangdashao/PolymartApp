package me.rerere.polymartapp.ui.route

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.rerere.polymartapp.R
import me.rerere.polymartapp.model.resource.Resource
import me.rerere.polymartapp.model.user.NOT_LOGIN
import me.rerere.polymartapp.model.user.UserInfo
import me.rerere.polymartapp.ui.route.index.ForumComp
import me.rerere.polymartapp.ui.route.index.ResourceComp
import me.rerere.polymartapp.ui.route.index.ServerListComp
import me.rerere.polymartapp.ui.theme.POLYMART_COLOR_DARKER
import me.rerere.polymartapp.ui.viewmodel.IndexViewModel
import me.rerere.polymartapp.util.currentVisualPage
import me.rerere.polymartapp.util.unread

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun IndexPage(navController: NavController, indexViewModel: IndexViewModel = hiltViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = 3)
    val state = indexViewModel.resourceListPager.collectAsLazyPagingItems()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                profilePic = indexViewModel.userInfo.profilePic,
                indexViewModel = indexViewModel,
                onClickNavigationIcon = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onClickSearch = {
                    indexViewModel.isSearching = !indexViewModel.isSearching
                    indexViewModel.search("")
                    state.refresh()
                },
                onClickMessage = {
                    navController.navigate("message")
                }
            )
        },
        drawerContent = {
            IndexDrawer(navController, indexViewModel.userInfo)
        },
        bottomBar = {
            BottomBar(pagerState)
        }
    ) {
        Content(it, pagerState, indexViewModel, navController, state)
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
private fun Content(
    paddingValues: PaddingValues,
    pagerState: PagerState,
    indexViewModel: IndexViewModel,
    navController: NavController,
    state: LazyPagingItems<Resource>
) {
    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        state = pagerState
    ) { page ->
        when (page) {
            0 -> {
                Column(Modifier.fillMaxSize()) {
                    SearchBar(indexViewModel){
                        state.refresh()
                    }
                    ResourceComp(indexViewModel, navController, state)
                }
            }
            1 -> {
                ServerListComp(indexViewModel)
            }
            2 -> {
                ForumComp(indexViewModel)
            }
        }
    }
}

@Composable
private fun SearchBar(indexViewModel: IndexViewModel, onSearch: ()->Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .height(if (indexViewModel.isSearching) 60.dp else 0.dp),
        elevation = 0.dp,
        color = MaterialTheme.colors.primarySurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // text field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.5.dp))
                    .height(40.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    value = indexViewModel.query,
                    textStyle = TextStyle.Default.copy(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    singleLine = true,
                    onValueChange = {
                        indexViewModel.search(it)
                    })
            }

            // Search
            IconButton(onClick = {
                onSearch()
            }) {
                Icon(Icons.Default.Search, null)
            }
        }
    }
}

@Composable
private fun TopBar(
    profilePic: String,
    indexViewModel: IndexViewModel,
    onClickNavigationIcon: () -> Unit,
    onClickSearch: () -> Unit,
    onClickMessage: () -> Unit
) {
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
                    avatarUrl = profilePic
                )
            }
        },
        actions = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    onClickSearch()
                }) {
                    Icon(
                        if (indexViewModel.isSearching) Icons.Default.Close else Icons.Default.Search,
                        "Search"
                    )
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
        },
        elevation = if (indexViewModel.isSearching) 0.dp else 4.dp
    )
}

@Composable
private fun Avatar(avatarUrl: String) {
    val painter = rememberCoilPainter(
        request = avatarUrl,
        fadeIn = true,
        previewPlaceholder = R.drawable.logo
    )
    Box(
        modifier = Modifier
            .size(35.dp)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painter, contentDescription = "avatar")
        when (painter.loadState) {
            is ImageLoadState.Success -> {
            }
            is ImageLoadState.Loading -> {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }
            else -> {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(R.drawable.avatar),
                    contentDescription = null
                )
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun BottomBar(pagerState: PagerState) {
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
private fun IndexDrawer(navController: NavController, userInfo: UserInfo) {
    // Profile
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .statusBarsPadding()
            .background(MaterialTheme.colors.primaryVariant),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
            if (userInfo == NOT_LOGIN) {
                navController.navigate("login")
            } else {
                navController.navigate("user")
            }
        }) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberCoilPainter(request = userInfo.profilePic)
                Image(painter = painter, contentDescription = null)
                when (painter.loadState) {
                    is ImageLoadState.Loading -> {
                        CircularProgressIndicator(
                            Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                    is ImageLoadState.Error -> {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),
                            imageVector = Icons.Default.NoAccounts,
                            contentDescription = null
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .widthIn(max = 150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Nickname
                Text(
                    text = userInfo.nickname,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Biography
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    Text(text = userInfo.biography)
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