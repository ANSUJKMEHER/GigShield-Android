package com.gigshield.mobile.core.network

import com.gigshield.mobile.core.preferences.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkModule(private val sessionManager: SessionManager) {

    private var currentBaseUrl = sessionManager.getEffectiveBaseUrl()
    private var retrofitInstance: Retrofit? = null
    private var apiServiceInstance: ApiService? = null

    val mockClient = MockApiClient()

    private fun buildOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Synchronized
    fun getApiService(): ApiService {
        val effectiveUrl = sessionManager.getEffectiveBaseUrl()
        if (apiServiceInstance == null || currentBaseUrl != effectiveUrl) {
            currentBaseUrl = effectiveUrl
            val safeUrl = if (currentBaseUrl.endsWith("/")) currentBaseUrl else "$currentBaseUrl/"

            retrofitInstance = Retrofit.Builder()
                .baseUrl(safeUrl)
                .client(buildOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiServiceInstance = retrofitInstance!!.create(ApiService::class.java)
        }
        return apiServiceInstance!!
    }

    fun isDemoMode(): Boolean {
        return sessionManager.getServerMode() == com.gigshield.mobile.core.common.Constants.MODE_DEMO_OFFLINE
    }
}
