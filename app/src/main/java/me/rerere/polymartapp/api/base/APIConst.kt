package me.rerere.polymartapp.api

import okhttp3.Request

// Base URL
const val POLYMART_BASEURL = "https://polymart.org/"

// Url Helper
fun Request.Builder.url(base: String, path: String): Request.Builder = this.url("$base$path")

// User Agent
const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36"