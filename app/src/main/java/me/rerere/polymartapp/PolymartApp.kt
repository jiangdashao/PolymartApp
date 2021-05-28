package me.rerere.polymartapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PolymartApp : Application(){
    companion object {
        lateinit var instance: PolymartApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}