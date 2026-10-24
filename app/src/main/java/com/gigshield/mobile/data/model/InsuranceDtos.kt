package com.gigshield.mobile.data.model

import com.google.gson.annotations.SerializedName

data class RiskResponse(
    @SerializedName("riskScore") val riskScore: Double,
    @SerializedName("premiumSuggestion") val premiumSuggestion: String,
    @SerializedName("basePremium") val basePremium: Int
)

data class SubscribeRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("plan") val plan: String
)

data class CancelRequest(
    @SerializedName("userId") val userId: String
)

data class InsuranceActionResponse(
    @SerializedName("message") val message: String,
    @SerializedName("user") val user: UserDto
)

data class FraudChecksDto(
    @SerializedName("gpsVerified") val gpsVerified: Boolean? = true,
    @SerializedName("radiusValid") val radiusValid: Boolean? = true,
    @SerializedName("workerActive") val workerActive: Boolean? = true,
    @SerializedName("telemetryValid") val telemetryValid: Boolean? = true,
    @SerializedName("duplicateFree") val duplicateFree: Boolean? = true,
    @SerializedName("weatherMatch") val weatherMatch: Boolean? = true,
    @SerializedName("anomalyFree") val anomalyFree: Boolean? = true,
    @SerializedName("gpsSpoofingFree") val gpsSpoofingFree: Boolean? = true,
    @SerializedName("historicalWeatherValid") val historicalWeatherValid: Boolean? = true
)

data class EventDetailsDto(
    @SerializedName("severity") val severity: String? = null,
    @SerializedName("deliveryDrop") val deliveryDrop: String? = null,
    @SerializedName("zeroTouch") val zeroTouch: Boolean? = false
)

data class ClaimDto(
    @SerializedName("_id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("triggerEvent") val triggerEvent: String,
    @SerializedName("city") val city: String,
    @SerializedName("expectedIncome") val expectedIncome: Int,
    @SerializedName("actualIncome") val actualIncome: Int,
    @SerializedName("loss") val loss: Int,
    @SerializedName("payoutAmount") val payoutAmount: Int,
    @SerializedName("status") val status: String, // "Approved", "Under Review", "Rejected"
    @SerializedName("fraudChecks") val fraudChecks: FraudChecksDto? = null,
    @SerializedName("eventDetails") val eventDetails: EventDetailsDto? = null,
    @SerializedName("rejectionReason") val rejectionReason: String? = "",
    @SerializedName("createdAt") val createdAt: String? = null
)

data class LiveWeatherPayload(
    @SerializedName("rain") val rain: Double = 0.0,
    @SerializedName("weather_code") val weatherCode: Int = 0
)

data class SimulateEventRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("triggerEvent") val triggerEvent: String,
    @SerializedName("city") val city: String,
    @SerializedName("expectedIncome") val expectedIncome: Int,
    @SerializedName("actualIncome") val actualIncome: Int,
    @SerializedName("liveGps") val liveGps: LocationDto?,
    @SerializedName("liveWeather") val liveWeather: LiveWeatherPayload?,
    @SerializedName("telemetryVariance") val telemetryVariance: Double,
    @SerializedName("isWorkerActive") val isWorkerActive: Boolean = true
)

data class SimulateEventResponse(
    @SerializedName("message") val message: String,
    @SerializedName("payoutAmount") val payoutAmount: Int? = 0,
    @SerializedName("coverageRemaining") val coverageRemaining: Int? = null,
    @SerializedName("claim") val claim: ClaimDto,
    @SerializedName("requiresReview") val requiresReview: Boolean? = false
)

data class DisruptionWebhookRequest(
    @SerializedName("city") val city: String,
    @SerializedName("zone") val zone: String? = null,
    @SerializedName("triggerEvent") val triggerEvent: String,
    @SerializedName("eventSeverity") val eventSeverity: String? = "Severe Disruption"
)

data class DisruptionStatsDto(
    @SerializedName("usersAffected") val usersAffected: Int,
    @SerializedName("claimsCreated") val claimsCreated: Int,
    @SerializedName("totalPayout") val totalPayout: Long
)

data class DisruptionWebhookResponse(
    @SerializedName("message") val message: String,
    @SerializedName("stats") val stats: DisruptionStatsDto
)
