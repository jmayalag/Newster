package com.hodinkee.hodinnews.di

import android.content.Context
import com.hodinkee.newsapi.NewsService
import com.hodinkee.newsapi.provideMoshiConverter
import com.hodinkee.newsapi.provideOkHttpClientBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpCache(@ApplicationContext applicationContext: Context): Cache {
        val cacheSize = 10L * 1024L * 1024L
        return Cache(applicationContext.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor?
    ): OkHttpClient {
        return provideOkHttpClientBuilder()
            .cache(cache)
            .apply { if (loggingInterceptor != null) addInterceptor(loggingInterceptor) }
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): MoshiConverterFactory {
        return provideMoshiConverter()
    }

    @Provides
    fun provideRetrofitBuilder(moshiConverter: MoshiConverterFactory, okHttpClient: OkHttpClient) =
        com.hodinkee.newsapi.provideRetrofitBuilder(moshiConverter, okHttpClient)

    @Provides
    fun provideRetrofit(builder: Retrofit.Builder) = builder.build()

    @Provides
    @Singleton
    fun provideNewsService(retrofit: Retrofit): NewsService = retrofit.create()
}