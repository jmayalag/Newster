package com.hodinkee.newsapi

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer

class PrintLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val body = request.body()
        val requestBody = body?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            buffer.readUtf8()
        } ?: "null"

        val requestLog =
            "${request.method()} -> ${request.url()}\nHeaders: ${request.headers()}\nBody: $requestBody"

        val response = chain.proceed(request)
        val bodyString = response.body()?.string()

        val responseLog =
            "${request.method()} <- (${response.code()}) ${request.url()}\nBody: ${bodyString})"

        println(requestLog)
        println()
        println(responseLog)

        return response.newBuilder()
            .body(ResponseBody.create(response.body()?.contentType(), bodyString ?: ""))
            .build()
    }

}

fun provideLoggingInterceptor() = PrintLoggingInterceptor()