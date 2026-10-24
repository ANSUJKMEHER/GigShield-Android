package com.gigshield.mobile.data.repository

import com.gigshield.mobile.core.common.Resource
import com.gigshield.mobile.core.database.UserDao
import com.gigshield.mobile.core.database.UserEntity
import com.gigshield.mobile.core.network.NetworkModule
import com.gigshield.mobile.core.preferences.SessionManager
import com.gigshield.mobile.data.model.LoginRequest
import com.gigshield.mobile.data.model.SignupRequest
import com.gigshield.mobile.data.model.UpdateProfileRequest
import com.gigshield.mobile.data.model.UserDto
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val networkModule: NetworkModule,
    private val sessionManager: SessionManager,
    private val userDao: UserDao
) {
    fun getCurrentUserFlow(): Flow<User?> {
        val currentUserId = sessionManager.getUserId() ?: ""
        return userDao.getUserFlow(currentUserId).map { it?.toDomain() }
    }

    suspend fun login(email: String, pass: String): Resource<User> {
        return try {
            val userDto: UserDto
            if (networkModule.isDemoMode()) {
                val res = networkModule.mockClient.login(LoginRequest(email, pass))
                sessionManager.saveToken(res.token)
                userDto = res.user
            } else {
                val response = networkModule.getApiService().login(LoginRequest(email, pass))
                if (!response.isSuccessful || response.body() == null) {
                    val errMsg = response.errorBody()?.string() ?: "Login failed (${response.code()})"
                    return Resource.Error(errMsg)
                }
                val body = response.body()!!
                sessionManager.saveToken(body.token)
                userDto = body.user
            }

            sessionManager.saveUser(userDto)
            val domainUser = mapDtoToDomain(userDto)
            userDao.insertUser(UserEntity.fromDomain(domainUser))
            Resource.Success(domainUser)
        } catch (e: Exception) {
            // Check if we have cached user or fallback to demo
            val cached = sessionManager.getCachedUser()
            if (cached != null) {
                Resource.Success(mapDtoToDomain(cached))
            } else {
                Resource.Error("Network error: ${e.localizedMessage ?: "Failed to connect"}", e)
            }
        }
    }

    suspend fun signup(
        name: String,
        email: String,
        pass: String,
        city: String,
        platform: String,
        zone: String,
        lat: Double?,
        lon: Double?
    ): Resource<User> {
        return try {
            val request = SignupRequest(
                name = name,
                email = email,
                password = pass,
                city = city,
                platform = platform,
                zone = zone,
                lat = lat,
                lon = lon
            )

            val userDto: UserDto
            if (networkModule.isDemoMode()) {
                val res = networkModule.mockClient.signup(request)
                sessionManager.saveToken(res.token)
                userDto = res.user
            } else {
                val response = networkModule.getApiService().signup(request)
                if (!response.isSuccessful || response.body() == null) {
                    val errMsg = response.errorBody()?.string() ?: "Registration failed (${response.code()})"
                    return Resource.Error(errMsg)
                }
                val body = response.body()!!
                sessionManager.saveToken(body.token)
                userDto = body.user
            }

            sessionManager.saveUser(userDto)
            val domainUser = mapDtoToDomain(userDto)
            userDao.insertUser(UserEntity.fromDomain(domainUser))
            Resource.Success(domainUser)
        } catch (e: Exception) {
            Resource.Error("Network error during signup: ${e.localizedMessage}", e)
        }
    }

    suspend fun updateProfile(
        name: String,
        city: String,
        platform: String,
        zone: String
    ): Resource<User> {
        val userId = sessionManager.getUserId() ?: return Resource.Error("Not logged in")
        return try {
            val req = UpdateProfileRequest(name, city, platform, zone)
            val userDto: UserDto = if (networkModule.isDemoMode()) {
                networkModule.mockClient.updateProfile(userId, req).user
            } else {
                val response = networkModule.getApiService().updateProfile(userId, req)
                if (!response.isSuccessful || response.body() == null) {
                    return Resource.Error("Update failed: ${response.code()}")
                }
                response.body()!!.user
            }

            sessionManager.saveUser(userDto)
            val domainUser = mapDtoToDomain(userDto)
            userDao.insertUser(UserEntity.fromDomain(domainUser))
            Resource.Success(domainUser)
        } catch (e: Exception) {
            Resource.Error("Error updating profile: ${e.localizedMessage}", e)
        }
    }

    suspend fun refreshUserData(): Resource<User> {
        val userId = sessionManager.getUserId() ?: return Resource.Error("No active session")
        return try {
            val userDto: UserDto = if (networkModule.isDemoMode()) {
                networkModule.mockClient.getUserProfile(userId).user
            } else {
                val response = networkModule.getApiService().getUserProfile(userId)
                if (!response.isSuccessful || response.body() == null) {
                    return Resource.Error("Failed to fetch profile (${response.code()})")
                }
                response.body()!!.user
            }

            sessionManager.saveUser(userDto)
            val domainUser = mapDtoToDomain(userDto)
            userDao.insertUser(UserEntity.fromDomain(domainUser))
            Resource.Success(domainUser)
        } catch (e: Exception) {
            val cached = sessionManager.getCachedUser()
            if (cached != null) Resource.Success(mapDtoToDomain(cached))
            else Resource.Error("Offline: ${e.localizedMessage}")
        }
    }

    fun logout() {
        sessionManager.clearSession()
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
