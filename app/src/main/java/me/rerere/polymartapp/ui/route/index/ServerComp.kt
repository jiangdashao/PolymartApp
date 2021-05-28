package me.rerere.polymartapp.ui.route.index

import android.content.ClipData
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.listItemsSingleChoice
import com.vanpra.composematerialdialogs.title
import me.rerere.polymartapp.PolymartApp
import me.rerere.polymartapp.R
import me.rerere.polymartapp.model.server.Server
import me.rerere.polymartapp.model.server.ServerSort
import me.rerere.polymartapp.ui.viewmodel.IndexViewModel

@Composable
fun ServerListComp(indexViewModel: IndexViewModel) {
    val refreshState = rememberSwipeRefreshState(indexViewModel.serverListIsLoading)
    val sortDialog = remember {
        MaterialDialog()
    }
    sortDialog.build {
        title("Sort by")
        Column {
            ServerSort.values().forEach {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        indexViewModel.sortType = it
                        indexViewModel.sortType = it
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                ){
                    RadioButton(selected = it == indexViewModel.sortType, onClick = {
                        indexViewModel.sortType = it
                        indexViewModel.sortType = it
                    })
                    Text(text = it.name)
                }
            }
        }
        buttons {
            button("Done"){
                indexViewModel.refreshServerList()
                sortDialog.hide()
            }
        }
    }
    
    if(indexViewModel.serverListError){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(text = "Error when loading server list")
        }
    }else {
        SwipeRefresh(modifier = Modifier.fillMaxSize(), state = refreshState, onRefresh = {
            indexViewModel.refreshServerList()
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                // Sort Type
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
                            Text(indexViewModel.sortType.name)
                        }
                    }
                }

                //Server List
                items(indexViewModel.serverList) {
                    ServerCard(it)
                }
            }
        }
    }
}

@Composable
fun ServerCard(server: Server) {
    val clipboardManager =
        PolymartApp.instance.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = 4.dp
    ) {
        Column {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 60.dp),
                painter = rememberCoilPainter(server.logo),
                contentDescription = "server logo",
                contentScale = ContentScale.FillWidth
            )
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = server.name,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.online),
                            contentDescription = null
                        )
                        Text(text = "${server.online}/${server.maxOnline}")
                    }
                }
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = server.description)
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 8.dp)
                        .background(Color.LightGray)
                        .alpha(0.5f)
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(modifier = Modifier.weight(1f), text = "IP: ${server.ip}")
                    }
                    IconButton(onClick = {
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, server.ip))
                    }, Modifier.size(15.dp)) {
                        Icon(Icons.Default.ContentCopy, "Copy Ip")
                    }
                }
            }
        }
    }
}