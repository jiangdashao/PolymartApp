package me.rerere.polymartapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.accompanist.coil.LocalImageLoader
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import me.rerere.polymartapp.ui.route.*
import me.rerere.polymartapp.ui.theme.PolymartAppTheme
import okhttp3.OkHttpClient
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var httpClient: OkHttpClient

    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PolymartAppTheme {
                val systemUiController = rememberSystemUiController()
                val primaryColor = MaterialTheme.colors.primarySurface
                val imageLoader = remember {
                    ImageLoader.Builder(this)
                        .okHttpClient(httpClient)
                        .crossfade(true)
                        .build()
                }

                // set ui color
                SideEffect {
                    systemUiController.setNavigationBarColor(primaryColor)
                    systemUiController.setStatusBarColor(primaryColor, false)
                }

                ProvideWindowInsets {
                    val navController = rememberNavController()

                    // navigation
                    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                        NavHost(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = "splash"
                        ) {
                            composable("splash") {
                                SplashPage(navController)
                            }

                            composable("index") {
                                IndexPage(navController)
                            }

                            composable("search") {
                                SearchPage(navController)
                            }

                            composable("login") {
                                LoginPage(navController)
                            }

                            composable("message") {
                                MessagePage(navController)
                            }

                            composable("about") {
                                AboutPage(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}