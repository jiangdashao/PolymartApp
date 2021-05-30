package me.rerere.polymartapp.ui.route.index

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Money
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import me.rerere.polymartapp.R
import me.rerere.polymartapp.model.resource.Resource
import me.rerere.polymartapp.ui.viewmodel.IndexViewModel

@Composable
fun ResourceComp(indexViewModel: IndexViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(indexViewModel.resourceList) {
            ResourceCard(resource = it)
        }
    }
}

@Composable
private fun ResourceCard(resource: Resource) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp))

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
                                is ImageLoadState.Success -> {}
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