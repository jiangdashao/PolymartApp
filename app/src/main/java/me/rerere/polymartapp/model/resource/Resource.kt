package me.rerere.polymartapp.model.resource

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class Resource(
    val id: Int,
    val title: String,
    val subtitle: String,

    val ownerId: Int,
    val ownerName: String,

    val thumbnailURL: String,
    val themeColorLight: Color,
    val themeColorDark: Color,

    val price: String,
    val currency: String,

    val version: String,
    val supportServerVersion: String,
    val supportServerSoftware: String?,
    val downloads: Int,

    val canDownload: Boolean
) {
    @Composable
    fun getThemeColorAuto() = if(isSystemInDarkTheme()) themeColorDark else themeColorLight

    fun priceString() = if (price.toDouble() > 0) {
        "$price $currency"
    } else {
        "Free"
    }
}
