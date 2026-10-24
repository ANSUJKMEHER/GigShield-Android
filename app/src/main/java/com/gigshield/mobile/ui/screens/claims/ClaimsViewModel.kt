package com.gigshield.mobile.ui.screens.claims

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigshield.mobile.core.common.Resource
import com.gigshield.mobile.core.sensor.TelemetrySensorManager
import com.gigshield.mobile.data.repository.AuthRepository
import com.gigshield.mobile.data.repository.ClaimsRepository
import com.gigshield.mobile.domain.model.Claim
import com.gigshield.mobile.domain.model.ClaimStatus
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ClaimsUiState(
    val user: User? = null,
    val claims: List<Claim> = emptyList(),
    val isSimulating: Boolean = false,
    val simulationStep: Int = 0, // 0 = idle, 1 = oracle fetch, 2 = 7-vector fraud check, 3 = finalized
    val lastSimulatedClaim: Claim? = null,
    val showRazorpayModal: Boolean = false,
    val errorMessage: String? = null
)

class ClaimsViewModel(
    private val claimsRepository: ClaimsRepository,
    private val authRepository: AuthRepository,
    private val sensorManager: TelemetrySensorManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClaimsUiState())
    val uiState: StateFlow<ClaimsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUserFlow().collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }

        viewModelScope.launch {
            claimsRepository.getClaimsFlow().collect { list ->
                _uiState.update { it.copy(claims = list) }
            }
        }

        fetchClaims()
    }

    fun fetchClaims() {
        viewModelScope.launch {
            claimsRepository.fetchClaims()
        }
    }

    fun simulateDisruption(triggerEvent: String, expected: Int, actual: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSimulating = true, simulationStep = 1, errorMessage = null) }
            delay(600) // Step 1: Open-Meteo & Oracle signal query
            _uiState.update { it.copy(simulationStep = 2) }
            delay(600) // Step 2: 7-Vector Fraud & Biomechanical Telemetry Check

            val city = _uiState.value.user?.city ?: "Mumbai"
            val variance = sensorManager.telemetryState.value.variance

            val result = claimsRepository.simulateDisruption(
                triggerEvent = triggerEvent,
                city = city,
                expectedIncome = expected,
                actualIncome = actual,
                lat = 19.0760,
                lon = 72.8777,
                telemetryVariance = variance,
                isWorkerActive = true
            )

            when (result) {
                is Resource.Success -> {
                    val claim = result.data
                    authRepository.refreshUserData()
                    _uiState.update {
                        it.copy(
                            isSimulating = false,
                            simulationStep = 3,
                            lastSimulatedClaim = claim,
                            showRazorpayModal = claim.status == ClaimStatus.APPROVED
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isSimulating = false,
                            simulationStep = 0,
                            errorMessage = result.message
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun dismissRazorpay() {
        _uiState.update { it.copy(showRazorpayModal = false) }
    }
}
