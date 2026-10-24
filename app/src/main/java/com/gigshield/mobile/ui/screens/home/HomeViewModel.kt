package com.gigshield.mobile.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigshield.mobile.core.preferences.SessionManager
import com.gigshield.mobile.core.sensor.TelemetrySensorManager
import com.gigshield.mobile.core.sensor.TelemetryState
import com.gigshield.mobile.data.repository.AuthRepository
import com.gigshield.mobile.data.repository.ClaimsRepository
import com.gigshield.mobile.domain.model.Claim
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val user: User? = null,
    val claims: List<Claim> = emptyList(),
    val telemetryState: TelemetryState = TelemetryState(),
    val isLoading: Boolean = false,
    val serverMode: String = "CLOUD",
    val errorMessage: String? = null
)

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val claimsRepository: ClaimsRepository,
    private val sensorManager: TelemetrySensorManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(serverMode = sessionManager.getServerMode())
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // Collect real-time user flow
        viewModelScope.launch {
            authRepository.getCurrentUserFlow().collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }

        // Collect real-time claims flow
        viewModelScope.launch {
            claimsRepository.getClaimsFlow().collect { claims ->
                _uiState.update { it.copy(claims = claims) }
            }
        }

        // Collect hardware sensor variance
        viewModelScope.launch {
            sensorManager.telemetryState.collect { telemetry ->
                _uiState.update { it.copy(telemetryState = telemetry) }
            }
        }

        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, serverMode = sessionManager.getServerMode()) }
            authRepository.refreshUserData()
            claimsRepository.fetchClaims()
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
