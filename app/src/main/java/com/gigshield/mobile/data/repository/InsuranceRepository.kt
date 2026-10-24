package com.gigshield.mobile.data.repository

import com.gigshield.mobile.core.common.Resource
import com.gigshield.mobile.core.database.UserDao
import com.gigshield.mobile.core.database.UserEntity
import com.gigshield.mobile.core.network.NetworkModule
import com.gigshield.mobile.core.preferences.SessionManager
import com.gigshield.mobile.data.model.CancelRequest
import com.gigshield.mobile.data.model.SubscribeRequest
import com.gigshield.mobile.data.model.UserDto
import com.gigshield.mobile.domain.model.InsurancePlan
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InsuranceRepository(
    private val networkModule: NetworkModule,
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) {
    private val _dynamicBasePremium = MutableStateFlow(10)
    val dynamicBasePremium: StateFlow<Int> = _dynamicBasePremium.asStateFlow()

    suspend fun fetchDynamicRisk(city: String, zone: String?): Resource<Int> {
        return try {
            val baseRate: Int = if (networkModule.isDemoMode()) {
                networkModule.mockClient.getRisk(city, zone).basePremium
            } else {
                val res = networkModule.getApiService().getRisk(city, zone)
                if (res.isSuccessful && res.body() != null) {
                    res.body()!!.basePremium
                } else {
                    12
                }
            }
            _dynamicBasePremium.value = baseRate
            Resource.Success(baseRate)
        } catch (e: Exception) {
            Resource.Success(_dynamicBasePremium.value)
        }
    }

    suspend fun subscribePlan(planTier: String): Resource<User> {
        val userId = sessionManager.getUserId() ?: return Resource.Error("User not logged in")
        return try {
            val updatedUserDto: UserDto = if (networkModule.isDemoMode()) {
                networkModule.mockClient.subscribe(planTier).user
            } else {
                val response = networkModule.getApiService().subscribePolicy(SubscribeRequest(userId, planTier))
                if (!response.isSuccessful || response.body() == null) {
                    return Resource.Error("Failed to subscribe to $planTier (${response.code()})")
                }
                response.body()!!.user
            }

            sessionManager.saveUser(updatedUserDto)
            val domainUser = mapDtoToDomain(updatedUserDto)
            userDao.insertUser(UserEntity.fromDomain(domainUser))
            Resource.Success(domainUser)
        } catch (e: Exception) {
            Resource.Error("Network error subscribing: ${e.localizedMessage}", e)
        }
    }

    suspend fun cancelPlan(): Resource<User> {
        val userId = sessionManager.getUserId() ?: return Resource.Error("User not logged in")
        return try {
            val updatedUserDto: UserDto = if (networkModule.isDemoMode()) {
                networkModule.mockClient.cancel().user
            } else {
                val response = networkModule.getApiService().cancelPolicy(CancelRequest(userId))
                if (!response.isSuccessful || response.body() == null) {
                    return Resource.Error("Failed to cancel policy")
                }
                response.body()!!.user
            }

            sessionManager.saveUser(updatedUserDto)
            val domainUser = mapDtoToDomain(updatedUserDto)
            userDao.insertUser(UserEntity.fromDomain(domainUser))
            Resource.Success(domainUser)
        } catch (e: Exception) {
            Resource.Error("Error cancelling policy: ${e.localizedMessage}", e)
        }
    }

    fun getPlans(baseRate: Int = _dynamicBasePremium.value): List<InsurancePlan> {
        return InsurancePlan.getAvailablePlans(baseRate)
    }

    private fun mapDtoToDomain(dto: UserDto): User {
        return User(
            id = dto.id,
            name = dto.name,
            email = dto.email,
            city = dto.city,
            zone = dto.zone,
            platform = dto.platform,
            riskScore = dto.riskScore,
            activePlan = dto.activePlan,
            latitude = dto.location?.lat,
            longitude = dto.location?.lon,
            coverage = dto.coverage,
            coverageRemaining = dto.coverageRemaining,
            premium = dto.premium,
            maxClaimsPerWeek = dto.maxClaimsPerWeek,
            claimsThisWeek = dto.claimsThisWeek,
            totalPayouts = dto.totalPayouts
        )
    }
}
