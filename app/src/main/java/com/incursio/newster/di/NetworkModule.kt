package com.incursio.newster.di

import android.content.Context
import coil.ImageLoader
import coil.util.CoilUtils
import com.incursio.newster.BuildConfig
import com.incursio.newsapi.NewsService
import com.incursio.newsapi.provideMoshiConverter
import com.incursio.newsapi.provideOkHttpClientBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpCache(@ApplicationContext applicationContext: Context): Cache {
        return CoilUtils.createDefaultCache(applicationContext)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
    ): OkHttpClient {
        val builder = provideOkHttpClientBuilder()
            .cache(cache)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply { level = BASIC })
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): MoshiConverterFactory {
        return provideMoshiConverter()
    }

    @Provides
    fun provideRetrofitBuilder(
        moshiConverter: MoshiConverterFactory,
        okHttpClient: OkHttpClient
    ) =
        com.incursio.newsapi.provideRetrofitBuilder(moshiConverter, okHttpClient)

    @Provides
    fun provideRetrofit(builder: Retrofit.Builder): Retrofit = builder.build()

    @Provides
    @Singleton
    fun provideNewsService(retrofit: Retrofit): NewsService = retrofit.create()

    @Provides
    @Singleton
    fun provideImageLoader(
        okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): ImageLoader {
        return ImageLoader.Builder(context).okHttpClient(okHttpClient).build()
    }
}