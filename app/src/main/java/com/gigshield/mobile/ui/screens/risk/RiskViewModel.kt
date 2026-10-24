package com.gigshield.mobile.ui.screens.risk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigshield.mobile.core.network.NetworkModule
import com.gigshield.mobile.data.model.DayForecastDto
import com.gigshield.mobile.data.repository.AuthRepository
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RiskUiState(
    val user: User? = null,
    val forecast: List<DayForecastDto> = emptyList(),
    val isLoading: Boolean = false
)

class RiskViewModel(
    private val authRepository: AuthRepository,
    private val networkModule: NetworkModule
) : ViewModel() {

    private val _uiState = MutableStateFlow(RiskUiState())
    val uiState: StateFlow<RiskUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUserFlow().collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
        loadPredictiveForecast()
    }

    fun loadPredictiveForecast() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val forecastList: List<DayForecastDto> = if (networkModule.isDemoMode()) {
                    networkModule.mockClient.getPredictiveAnalytics().forecast
                } else {
                    val response = networkModule.getApiService().getPredictiveAnalytics()
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!.forecast
                    } else {
                        networkModule.mockClient.getPredictiveAnalytics().forecast
                    }
                }
                _uiState.update { it.copy(forecast = forecastList, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        forecast = networkModule.mockClient.getPredictiveAnalytics().forecast,
                        isLoading = false
                    )
                }
            }
        }
    }
}
