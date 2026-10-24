package com.gigshield.mobile.data.repository

import com.gigshield.mobile.core.common.Resource
import com.gigshield.mobile.core.database.ClaimDao
import com.gigshield.mobile.core.database.ClaimEntity
import com.gigshield.mobile.core.database.UserDao
import com.gigshield.mobile.core.network.NetworkModule
import com.gigshield.mobile.core.preferences.SessionManager
import com.gigshield.mobile.data.model.*
import com.gigshield.mobile.domain.model.Claim
import com.gigshield.mobile.domain.model.ClaimStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClaimsRepository(
    private val networkModule: NetworkModule,
    private val sessionManager: SessionManager,
    private val claimDao: ClaimDao,
    private val userDao: UserDao
) {

    fun getClaimsFlow(): Flow<List<Claim>> {
        val currentUserId = sessionManager.getUserId() ?: ""
        return claimDao.getClaimsFlow(currentUserId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun fetchClaims(): Resource<List<Claim>> {
        val userId = sessionManager.getUserId() ?: return Resource.Error("Not logged in")
        return try {
            val claimDtos: List<ClaimDto> = if (networkModule.isDemoMode()) {
                networkModule.mockClient.getUserProfile(userId).claims
            } else {
                val response = networkModule.getApiService().getUserProfile(userId)
                if (!response.isSuccessful || response.body() == null) {
                    val local = claimDao.getClaims(userId).map { it.toDomain() }
                    return Resource.Success(local)
                }
                response.body()!!.claims
            }

            val domainClaims = claimDtos.map { mapClaimDtoToDomain(it) }
            claimDao.insertAll(domainClaims.map { ClaimEntity.fromDomain(it) })
            Resource.Success(domainClaims)
        } catch (e: Exception) {
            val local = claimDao.getClaims(userId).map { it.toDomain() }
            if (local.isNotEmpty()) Resource.Success(local)
            else Resource.Error("Failed to fetch claims: ${e.localizedMessage}")
        }
    }

    suspend fun simulateDisruption(
        triggerEvent: String,
        city: String,
        expectedIncome: Int,
        actualIncome: Int,
        lat: Double?,
        lon: Double?,
        telemetryVariance: Double,
        isWorkerActive: Boolean = true
    ): Resource<Claim> {
        val userId = sessionManager.getUserId() ?: return Resource.Error("User not logged in")

        val loss = expectedIncome - actualIncome
        if (loss < 100) {
            return Resource.Error("Loss of ₹$loss is below the minimum ₹100 threshold.")
        }

        val request = SimulateEventRequest(
            userId = userId,
            triggerEvent = triggerEvent,
            city = city,
            expectedIncome = expectedIncome,
            actualIncome = actualIncome,
            liveGps = LocationDto(lat, lon),
            liveWeather = LiveWeatherPayload(rain = if (triggerEvent == "Heavy Rain") 22.0 else 0.0),
            telemetryVariance = telemetryVariance,
            isWorkerActive = isWorkerActive
        )

        return try {
            val claimDto: ClaimDto
            if (networkModule.isDemoMode()) {
                val simRes = networkModule.mockClient.simulateEvent(request)
                claimDto = simRes.claim
            } else {
                val response = networkModule.getApiService().simulateEvent(request)
                if (!response.isSuccessful || response.body() == null) {
                    val err = response.errorBody()?.string() ?: "Simulation failed"
                    return Resource.Error(err)
                }
                claimDto = response.body()!!.claim
            }

            val domainClaim = mapClaimDtoToDomain(claimDto)
            claimDao.insertClaim(ClaimEntity.fromDomain(domainClaim))
            Resource.Success(domainClaim)
        } catch (e: Exception) {
            Resource.Error("Network error during disruption simulation: ${e.localizedMessage}", e)
        }
    }

    private fun mapClaimDtoToDomain(dto: ClaimDto): Claim {
        val status = when (dto.status) {
            "Approved" -> ClaimStatus.APPROVED
            "Under Review" -> ClaimStatus.UNDER_REVIEW
            else -> ClaimStatus.REJECTED
        }
        return Claim(
            id = dto.id,
            userId = dto.userId,
            triggerEvent = dto.triggerEvent,
            city = dto.city,
            expectedIncome = dto.expectedIncome,
            actualIncome = dto.actualIncome,
            loss = dto.loss,
            payoutAmount = dto.payoutAmount,
            status = status,
            severity = dto.eventDetails?.severity ?: "Severity Breached",
            deliveryDrop = dto.eventDetails?.deliveryDrop ?: "45%",
            rejectionReason = dto.rejectionReason ?: "",
            createdAt = dto.createdAt ?: "Recently",
            isGpsVerified = dto.fraudChecks?.gpsVerified ?: true,
            isTelemetryValid = dto.fraudChecks?.telemetryValid ?: true,
            isWeatherMatch = dto.fraudChecks?.weatherMatch ?: true
        )
    }
}
