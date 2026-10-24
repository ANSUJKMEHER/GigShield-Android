package com.gigshield.mobile.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val city: String,
    val zone: String,
    val platform: String,
    val riskScore: Double,
    val activePlan: String,
    val latitude: Double?,
    val longitude: Double?,
    val coverage: Int,
    val coverageRemaining: Int,
    val premium: Int,
    val maxClaimsPerWeek: Int,
    val claimsThisWeek: Int,
    val totalPayouts: Int
) {
    val isPolicyActive: Boolean
        get() = activePlan != "None" && activePlan.isNotBlank()

    val coveragePercentage: Float
        get() = if (coverage > 0) coverageRemaining.toFloat() / coverage.toFloat() else 0f
}
