package com.gigshield.mobile.core.network

import com.gigshield.mobile.core.preferences.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        val token = sessionManager.getToken()
        if (!token.isNullOrBlank()) {
            builder.addHeader("Authorization", "Bearer $token")
        }

        builder.addHeader("Accept", "application/json")
        builder.addHeader("Content-Type", "application/json")

        return chain.proceed(builder.build())
    }
}
