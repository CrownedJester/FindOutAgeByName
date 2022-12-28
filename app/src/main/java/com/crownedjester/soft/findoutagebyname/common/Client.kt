package com.crownedjester.soft.findoutagebyname.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object Client {

    fun createClient(context: Context): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient().newBuilder()
            .addInterceptor(createOfflineInterceptor(context))
            .addNetworkInterceptor(onlineInterceptor)
            .cache(getCache(context))
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val onlineInterceptor: Interceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(1, TimeUnit.DAYS)
            .build()
        response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .removeHeader("Pragma")
            .build()
    }

    private fun createOfflineInterceptor(context: Context): Interceptor = Interceptor { chain ->
        val request = chain
            .request()

        if (!isInternetAvailable(context)) {
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$MAX_STALE")
                .removeHeader("Pragma")
                .build()

        }

        chain.proceed(request)

    }

    private fun getCache(context: Context): Cache =
        Cache(context.cacheDir, CACHE_SIZE)

    @Suppress("DEPRECATION")
    private fun isInternetAvailable(context: Context): Boolean {

        var isConnected = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected


    }


}

private const val MAX_STALE = 60 * 60
private const val CACHE_SIZE = 1024L * 1024L
