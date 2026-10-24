package com.gigshield.mobile.core.network

import com.gigshield.mobile.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth endpoints
    @POST("api/auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/auth/user/{id}")
    suspend fun getUserProfile(@Path("id") userId: String): Response<UserProfileResponse>

    @PUT("api/auth/profile/{id}")
    suspend fun updateProfile(
        @Path("id") userId: String,
        @Body request: UpdateProfileRequest
    ): Response<InsuranceActionResponse>

    // Insurance endpoints
    @GET("api/insurance/risk/{city}")
    suspend fun getRisk(
        @Path("city") city: String,
        @Query("zone") zone: String? = null,
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("pastClaimsCount") pastClaimsCount: Int = 0
    ): Response<RiskResponse>

    @POST("api/insurance/subscribe")
    suspend fun subscribePolicy(@Body request: SubscribeRequest): Response<InsuranceActionResponse>

    @POST("api/insurance/cancel")
    suspend fun cancelPolicy(@Body request: CancelRequest): Response<InsuranceActionResponse>

    @POST("api/insurance/simulate-event")
    suspend fun simulateEvent(@Body request: SimulateEventRequest): Response<SimulateEventResponse>

    @POST("api/insurance/webhook/trigger-disruption")
    suspend fun triggerDisruptionWebhook(@Body request: DisruptionWebhookRequest): Response<DisruptionWebhookResponse>

    // Admin & Predictive Analytics
    @GET("api/admin/stats")
    suspend fun getAdminStats(): Response<AdminStatsResponse>

    @GET("api/admin/predictive-analytics")
    suspend fun getPredictiveAnalytics(): Response<PredictiveAnalyticsResponse>
}
