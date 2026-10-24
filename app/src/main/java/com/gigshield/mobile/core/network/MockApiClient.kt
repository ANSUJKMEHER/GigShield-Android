package com.gigshield.mobile.core.network

import com.gigshield.mobile.data.model.*
import java.util.UUID

class MockApiClient {

    private var currentUser = UserDto(
        id = "demo-worker-001",
        name = "Ravi Kumar",
        email = "ravi.kumar@gigshield.ai",
        city = "Mumbai",
        zone = "Flood Prone (Dharavi)",
        platform = "Zomato",
        riskScore = 0.65,
        activePlan = "Pro",
        coverage = 800,
        coverageRemaining = 550,
        premium = 24,
        maxClaimsPerWeek = 2,
        claimsThisWeek = 1,
        totalPayouts = 250,
        location = LocationDto(19.0760, 72.8777)
    )

    private val claimsList = mutableListOf(
        ClaimDto(
            id = "claim-demo-101",
            userId = "demo-worker-001",
            triggerEvent = "Heavy Rain",
            city = "Mumbai",
            expectedIncome = 700,
            actualIncome = 450,
            loss = 250,
            payoutAmount = 250,
            status = "Approved",
            rejectionReason = "",
            createdAt = "2026-04-18T10:15:00.000Z",
            eventDetails = EventDetailsDto(
                severity = "Live Rain: 24mm",
                deliveryDrop = "48%",
                zeroTouch = false
            ),
            fraudChecks = FraudChecksDto(
                gpsVerified = true,
                radiusValid = true,
                workerActive = true,
                telemetryValid = true,
                duplicateFree = true,
                weatherMatch = true,
                anomalyFree = true
            )
        )
    )

    fun login(request: LoginRequest): AuthResponse {
        return AuthResponse(
            message = "Login successful (Demo Mode)",
            user = currentUser,
            token = "demo_jwt_token_sample"
        )
    }

    fun signup(request: SignupRequest): AuthResponse {
        currentUser = currentUser.copy(
            id = "user-" + UUID.randomUUID().toString().take(8),
            name = request.name,
            email = request.email,
            city = request.city,
            platform = request.platform,
            zone = request.zone,
            location = LocationDto(request.lat, request.lon)
        )
        return AuthResponse(
            message = "User created successfully (Demo Mode)",
            user = currentUser,
            token = "demo_jwt_token_sample",
            premiumSuggestion = "Medium (₹24/week)"
        )
    }

    fun getUserProfile(userId: String): UserProfileResponse {
        return UserProfileResponse(
            user = currentUser,
            claims = claimsList
        )
    }

    fun updateProfile(userId: String, request: UpdateProfileRequest): InsuranceActionResponse {
        currentUser = currentUser.copy(
            name = request.name,
            city = request.city,
            platform = request.platform,
            zone = request.zone
        )
        return InsuranceActionResponse(
            message = "Profile updated successfully",
            user = currentUser
        )
    }

    fun getRisk(city: String, zone: String?): RiskResponse {
        val base = if (zone?.contains("flood", ignoreCase = true) == true) 33 else 12
        return RiskResponse(
            riskScore = if (base > 20) 0.65 else 0.25,
            premiumSuggestion = if (base > 20) "High (₹$base/week)" else "Low (₹$base/week)",
            basePremium = base
        )
    }

    fun subscribe(plan: String): InsuranceActionResponse {
        val coverage = when (plan) {
            "Elite" -> 1500
            "Pro" -> 800
            else -> 300
        }
        val maxClaims = when (plan) {
            "Elite" -> 3
            "Pro" -> 2
            else -> 1
        }
        val premium = when (plan) {
            "Elite" -> 36
            "Pro" -> 24
            else -> 12
        }

        currentUser = currentUser.copy(
            activePlan = plan,
            coverage = coverage,
            coverageRemaining = coverage,
            maxClaimsPerWeek = maxClaims,
            claimsThisWeek = 0,
            premium = premium
        )
        return InsuranceActionResponse(
            message = "Subscribed successfully to $plan Plan",
            user = currentUser
        )
    }

    fun cancel(): InsuranceActionResponse {
        currentUser = currentUser.copy(
            activePlan = "None",
            coverage = 0,
            coverageRemaining = 0,
            maxClaimsPerWeek = 0,
            claimsThisWeek = 0,
            premium = 0
        )
        return InsuranceActionResponse(
            message = "Policy cancelled successfully",
            user = currentUser
        )
    }

    fun simulateEvent(request: SimulateEventRequest): SimulateEventResponse {
        val loss = request.expectedIncome - request.actualIncome
        val payout = loss.coerceAtMost(currentUser.coverageRemaining)

        val isSoftReview = request.triggerEvent == "App Crash" ||
                request.telemetryVariance < 0.2

        val claim = ClaimDto(
            id = "claim-" + UUID.randomUUID().toString().take(8),
            userId = currentUser.id,
            triggerEvent = request.triggerEvent,
            city = request.city,
            expectedIncome = request.expectedIncome,
            actualIncome = request.actualIncome,
            loss = loss,
            payoutAmount = if (isSoftReview) 0 else payout,
            status = if (isSoftReview) "Under Review" else "Approved",
            rejectionReason = if (isSoftReview) "Flagged for manual duplicate review (Soft Queue)" else "",
            createdAt = "Just now",
            eventDetails = EventDetailsDto(
                severity = "${request.triggerEvent} Threshold Breached",
                deliveryDrop = "58%",
                zeroTouch = false
            ),
            fraudChecks = FraudChecksDto(
                gpsVerified = true,
                radiusValid = true,
                workerActive = request.isWorkerActive,
                telemetryValid = request.telemetryVariance >= 0.5,
                duplicateFree = !isSoftReview,
                weatherMatch = true,
                anomalyFree = true
            )
        )

        claimsList.add(0, claim)

        if (!isSoftReview) {
            currentUser = currentUser.copy(
                coverageRemaining = (currentUser.coverageRemaining - payout).coerceAtLeast(0),
                totalPayouts = currentUser.totalPayouts + payout,
                claimsThisWeek = currentUser.claimsThisWeek + 1
            )
        }

        return SimulateEventResponse(
            message = if (isSoftReview) "Claim soft-flagged for Admin Review" else "₹$payout credited instantly via Razorpay UPI!",
            payoutAmount = if (isSoftReview) 0 else payout,
            coverageRemaining = currentUser.coverageRemaining,
            claim = claim,
            requiresReview = isSoftReview
        )
    }

    fun getAdminStats(): AdminStatsResponse {
        return AdminStatsResponse(
            totalUsers = 4321,
            claimsCount = 1892,
            fraudAttempts = 42,
            totalPayout = 2937600L,
            totalPremiums = 4210000L,
            lossRatio = "69.77",
            activePolicies = 3980
        )
    }

    fun getPredictiveAnalytics(): PredictiveAnalyticsResponse {
        return PredictiveAnalyticsResponse(
            forecast = listOf(
                DayForecastDto("Mon", 0.2, 12),
                DayForecastDto("Tue", 0.3, 18),
                DayForecastDto("Wed", 0.8, 85, "High Monsoon Rain Forecast"),
                DayForecastDto("Thu", 0.6, 45),
                DayForecastDto("Fri", 0.2, 15),
                DayForecastDto("Sat", 0.1, 5),
                DayForecastDto("Sun", 0.1, 3)
            )
        )
    }
}
