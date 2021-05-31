package me.rerere.polymartapp.ui.route.index

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.*
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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.launch
import me.rerere.polymartapp.R
import me.rerere.polymartapp.model.resource.Resource
import me.rerere.polymartapp.model.resource.ResourceSort
import me.rerere.polymartapp.ui.viewmodel.IndexViewModel

@ExperimentalAnimationApi
@Composable
fun ResourceComp(indexViewModel: IndexViewModel, navController: NavController, state: LazyPagingItems<Resource>) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = state.loadState.refresh == LoadState.Loading)
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val sortDialog = remember {
        MaterialDialog()
    }
    val currentSort by indexViewModel.resourceSort.observeAsState(ResourceSort.UPDATED)
    sortDialog.build {
        title("Choose a sort type")
        Column {
            ResourceSort.values().forEach {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        indexViewModel.setResourceSort(it)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    RadioButton(selected = it == currentSort, onClick = {
                        indexViewModel.setResourceSort(it)
                    })
                    Text(text = it.name)
                }
            }
        }
        buttons {
            button("Done") {
                sortDialog.hide()
                state.refresh()
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AnimatedVisibility(
                visible = listState.firstVisibleItemIndex > 1,
                enter = fadeIn(animationSpec = tween(500))
            ) {
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
        if (state.loadState.refresh is LoadState.Error) {
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable { state.refresh() }, contentAlignment = Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Failed to load resource list")
                    Text(text = "Click to try again")
                }
            }
        } else {
            SwipeRefresh(state = refreshState, onRefresh = { state.refresh() }) {
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    // Sort
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sort by: ")
                            Box(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        color = MaterialTheme.colors.onSurface,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .clickable {
                                        sortDialog.show()
                                    }
                                    .padding(4.dp), contentAlignment = Alignment.Center
                            ) {
                                Text(currentSort.value)
                            }
                        }
                    }
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