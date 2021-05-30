package me.rerere.polymartapp.ui.route.index

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Money
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import me.rerere.polymartapp.R
import me.rerere.polymartapp.model.resource.Resource
import me.rerere.polymartapp.ui.viewmodel.IndexViewModel

@ExperimentalAnimationApi
@Composable
fun ResourceComp(indexViewModel: IndexViewModel, navController: NavController) {
    val state = indexViewModel.resourceListPager.collectAsLazyPagingItems()
    val refreshState = rememberSwipeRefreshState(isRefreshing = state.loadState.refresh == LoadState.Loading)
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AnimatedVisibility(visible = listState.firstVisibleItemIndex > 6, enter = fadeIn(animationSpec = tween(500))) {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }) {
                    Icon(Icons.Default.ArrowUpward, null)
                }
            }
        }
    ) {
        SwipeRefresh(state = refreshState, onRefresh = { state.refresh() }) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                items(state) {
                    ResourceCard(resource = it!!) {
                        navController.navigate("resource/${it.id}")
                    }
                }

                if (state.loadState.append == LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp), contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(Modifier.size(25.dp))
                                Text(text = "Loading")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResourceCard(resource: Resource, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 70.dp)
                    .background(if (isSystemInDarkTheme()) resource.themeColorDark else resource.themeColorLight),
                painter = rememberCoilPainter(resource.thumbnailURL),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Title
                Text(text = resource.title, style = MaterialTheme.typography.h6)

                // Subtitle
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = resource.subtitle)
                }

                // Spacer
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                // Info
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                        ) {
                            val painter = rememberCoilPainter(
                                request = "https://s3.amazonaws.com/polymart.user.profilepictures/large/${resource.ownerId}?k="
                            )
                            Image(
                                painter = painter,
                                contentDescription = null
                            )
                            when (painter.loadState) {
                                is ImageLoadState.Success -> {
                                }
                                else -> {
                                    Icon(painterResource(R.drawable.avatar), null)
                                }
                            }
                        }
                        // Name
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = resource.ownerName
                        )

                        Row(
                            modifier = Modifier
                                .height(20.dp)
                                .weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            // Downloads
                            Row(modifier = Modifier.padding(horizontal = 5.dp)) {
                                Icon(Icons.Default.Download, null)
                                Text(text = resource.downloads.toString())
                            }

                            // Price
                            Row(modifier = Modifier.padding(horizontal = 5.dp)) {
                                Icon(Icons.Default.Money, null)
                                Text(resource.priceString())
                            }
                        }
                    }
                }
            }
        }
    }
}