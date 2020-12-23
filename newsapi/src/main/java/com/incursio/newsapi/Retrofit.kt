package com.incursio.newsapi

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

fun provideMoshiConverter(): MoshiConverterFactory = MoshiConverterFactory.create(
    Moshi.Builder()
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .build()
)

fun provideOkHttpClientBuilder() = OkHttpClient.Builder()

fun provideRetrofitBuilder(moshiConverter: MoshiConverterFactory, okHttpClient: OkHttpClient): Retrofit.Builder =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(moshiConverter)
        .client(okHttpClient)