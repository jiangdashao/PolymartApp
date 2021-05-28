package me.rerere.polymartapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.rerere.polymartapp.api.PolymartApi
import me.rerere.polymartapp.api.PolymartApiImpl
import me.rerere.polymartapp.util.CookieJarHelper
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .cookieJar(CookieJarHelper())
        .connectTimeout(10, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun providePolymartApi(httpClient: OkHttpClient): PolymartApi = PolymartApiImpl(httpClient)
}