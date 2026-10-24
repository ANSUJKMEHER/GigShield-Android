package com.gigshield.mobile.data.model

import com.google.gson.annotations.SerializedName

data class AdminStatsResponse(
    @SerializedName("totalUsers") val totalUsers: Int,
    @SerializedName("claimsCount") val claimsCount: Int,
    @SerializedName("fraudAttempts") val fraudAttempts: Int,
    @SerializedName("totalPayout") val totalPayout: Long,
    @SerializedName("totalPremiums") val totalPremiums: Long,
    @SerializedName("lossRatio") val lossRatio: String,
    @SerializedName("activePolicies") val activePolicies: Int
)

data class DayForecastDto(
    @SerializedName("day") val day: String,
    @SerializedName("riskFactor") val riskFactor: Double,
    @SerializedName("predictedClaims") val predictedClaims: Int,
    @SerializedName("alert") val alert: String? = null
)

data class PredictiveAnalyticsResponse(
    @SerializedName("forecast") val forecast: List<DayForecastDto>
)

data class ResolveClaimRequest(
    @SerializedName("status") val status: String,
    @SerializedName("reason") val reason: String? = null
)
