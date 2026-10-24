package com.gigshield.mobile.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gigshield.mobile.domain.model.Claim
import com.gigshield.mobile.domain.model.ClaimStatus
import com.gigshield.mobile.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
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
    fun toDomain(): User = User(
        id = id,
        name = name,
        email = email,
        city = city,
        zone = zone,
        platform = platform,
        riskScore = riskScore,
        activePlan = activePlan,
        latitude = latitude,
        longitude = longitude,
        coverage = coverage,
        coverageRemaining = coverageRemaining,
        premium = premium,
        maxClaimsPerWeek = maxClaimsPerWeek,
        claimsThisWeek = claimsThisWeek,
        totalPayouts = totalPayouts
    )

    companion object {
        fun fromDomain(user: User): UserEntity = UserEntity(
            id = user.id,
            name = user.name,
            email = user.email,
            city = user.city,
            zone = user.zone,
            platform = user.platform,
            riskScore = user.riskScore,
            activePlan = user.activePlan,
            latitude = user.latitude,
            longitude = user.longitude,
            coverage = user.coverage,
            coverageRemaining = user.coverageRemaining,
            premium = user.premium,
            maxClaimsPerWeek = user.maxClaimsPerWeek,
            claimsThisWeek = user.claimsThisWeek,
            totalPayouts = user.totalPayouts
        )
    }
}

@Entity(tableName = "claims")
data class ClaimEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val triggerEvent: String,
    val city: String,
    val expectedIncome: Int,
    val actualIncome: Int,
    val loss: Int,
    val payoutAmount: Int,
    val status: String,
    val severity: String,
    val deliveryDrop: String,
    val rejectionReason: String,
    val createdAt: String,
    val isGpsVerified: Boolean,
    val isTelemetryValid: Boolean,
    val isWeatherMatch: Boolean
) {
    fun toDomain(): Claim = Claim(
        id = id,
        userId = userId,
        triggerEvent = triggerEvent,
        city = city,
        expectedIncome = expectedIncome,
        actualIncome = actualIncome,
        loss = loss,
        payoutAmount = payoutAmount,
        status = when (status) {
            "Approved" -> ClaimStatus.APPROVED
            "Under Review" -> ClaimStatus.UNDER_REVIEW
            else -> ClaimStatus.REJECTED
        },
        severity = severity,
        deliveryDrop = deliveryDrop,
        rejectionReason = rejectionReason,
        createdAt = createdAt,
        isGpsVerified = isGpsVerified,
        isTelemetryValid = isTelemetryValid,
        isWeatherMatch = isWeatherMatch
    )

    companion object {
        fun fromDomain(claim: Claim): ClaimEntity = ClaimEntity(
            id = claim.id,
            userId = claim.userId,
            triggerEvent = claim.triggerEvent,
            city = claim.city,
            expectedIncome = claim.expectedIncome,
            actualIncome = claim.actualIncome,
            loss = claim.loss,
            payoutAmount = claim.payoutAmount,
            status = when (claim.status) {
                ClaimStatus.APPROVED -> "Approved"
                ClaimStatus.UNDER_REVIEW -> "Under Review"
                ClaimStatus.REJECTED -> "Rejected"
            },
            severity = claim.severity,
            deliveryDrop = claim.deliveryDrop,
            rejectionReason = claim.rejectionReason,
            createdAt = claim.createdAt,
            isGpsVerified = claim.isGpsVerified,
            isTelemetryValid = claim.isTelemetryValid,
            isWeatherMatch = claim.isWeatherMatch
        )
    }
}
