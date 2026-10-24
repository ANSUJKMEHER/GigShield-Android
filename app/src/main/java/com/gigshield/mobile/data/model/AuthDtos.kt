package com.gigshield.mobile.data.model

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lon") val lon: Double? = null
)

data class WorkingHoursDto(
    @SerializedName("start") val start: String? = "09:00",
    @SerializedName("end") val end: String? = "21:00"
)

data class UserDto(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("city") val city: String,
    @SerializedName("zone") val zone: String = "General",
    @SerializedName("platform") val platform: String,
    @SerializedName("riskScore") val riskScore: Double = 0.0,
    @SerializedName("activePlan") val activePlan: String = "None",
    @SerializedName("location") val location: LocationDto? = null,
    @SerializedName("workingHours") val workingHours: WorkingHoursDto? = null,
    @SerializedName("policyActiveAt") val policyActiveAt: String? = null,
    @SerializedName("pastClaimsCount") val pastClaimsCount: Int = 0,
    @SerializedName("coverage") val coverage: Int = 0,
    @SerializedName("coverageRemaining") val coverageRemaining: Int = 0,
    @SerializedName("premium") val premium: Int = 0,
    @SerializedName("maxClaimsPerWeek") val maxClaimsPerWeek: Int = 0,
    @SerializedName("claimsThisWeek") val claimsThisWeek: Int = 0,
    @SerializedName("totalPayouts") val totalPayouts: Int = 0,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class SignupRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("city") val city: String,
    @SerializedName("platform") val platform: String,
    @SerializedName("zone") val zone: String = "General",
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lon") val lon: Double? = null,
    @SerializedName("workingHoursStart") val workingHoursStart: String? = "09:00",
    @SerializedName("workingHoursEnd") val workingHoursEnd: String? = "21:00"
)

data class AuthResponse(
    @SerializedName("message") val message: String,
    @SerializedName("user") val user: UserDto,
    @SerializedName("token") val token: String? = null,
    @SerializedName("premiumSuggestion") val premiumSuggestion: String? = null
)

data class UserProfileResponse(
    @SerializedName("user") val user: UserDto,
    @SerializedName("claims") val claims: List<ClaimDto> = emptyList()
)

data class UpdateProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String,
    @SerializedName("platform") val platform: String,
    @SerializedName("zone") val zone: String,
    @SerializedName("workingHoursStart") val workingHoursStart: String? = null,
    @SerializedName("workingHoursEnd") val workingHoursEnd: String? = null
)
