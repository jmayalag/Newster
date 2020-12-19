package com.hodinkee.hodinnews

import android.app.Application
import android.content.Context
import com.hodinkee.newsapi.NewsService
import com.hodinkee.newsapi.provideMoshiConverter
import com.hodinkee.newsapi.provideOkHttpClientBuilder
import com.hodinkee.newsapi.provideRetrofitBuilder
import dagger.hilt.android.HiltAndroidApp
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.create


fun Context.app() = this.applicationContext as NewsApp
fun Application.news() = this as NewsApp

@HiltAndroidApp
class NewsApp : Application() {
    private val retrofit by lazy {
        val moshi = provideMoshiConverter()

        val okHttpClient = provideOkHttpClientBuilder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor())
                }
            }
            .build()


        val retrofit = provideRetrofitBuilder(moshi, okHttpClient).build()
        retrofit
    }

    val newsService by lazy { retrofit.create<NewsService>() }

    override fun onCreate() {
        super.onCreate()
    }
}