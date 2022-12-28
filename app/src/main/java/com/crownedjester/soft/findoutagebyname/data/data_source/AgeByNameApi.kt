package com.crownedjester.soft.findoutagebyname.data.data_source

import com.crownedjester.soft.findoutagebyname.data.model.PersonDataDto
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface AgeByNameApi {

    companion object {
        const val BASE_URL = "https://api.agify.io/"

        fun createClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BASIC

            val offlineCacheInterceptor = Interceptor { chain ->
                val builder = chain
                    .request()
                    .newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)

                chain.proceed(builder.build())
            }

            return OkHttpClient().newBuilder()
                .addInterceptor(offlineCacheInterceptor)
                .addNetworkInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        }

    }

    @GET("")
    suspend fun getAgeByName(@Query("name") name: String): PersonDataDto

}