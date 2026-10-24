package com.gigshield.mobile.domain.model

enum class ClaimStatus {
    APPROVED,
    UNDER_REVIEW,
    REJECTED
}

data class Claim(
    val id: String,
    val userId: String,
    val triggerEvent: String,
    val city: String,
    val expectedIncome: Int,
    val actualIncome: Int,
    val loss: Int,
    val payoutAmount: Int,
    val status: ClaimStatus,
    val severity: String,
    val deliveryDrop: String,
    val rejectionReason: String,
    val createdAt: String,
    val isGpsVerified: Boolean,
    val isTelemetryValid: Boolean,
    val isWeatherMatch: Boolean
)
