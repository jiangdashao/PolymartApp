package me.rerere.polymartapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.rerere.polymartapp.api.PolymartApi
import me.rerere.polymartapp.api.PolymartApiImpl
import me.rerere.polymartapp.util.CookieJarHelper
import me.rerere.polymartapp.util.UserAgentInterceptor
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
        .addInterceptor(UserAgentInterceptor("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36"))
        .build()

    @Singleton
    @Provides
    fun providePolymartApi(httpClient: OkHttpClient): PolymartApi = PolymartApiImpl(httpClient)
}