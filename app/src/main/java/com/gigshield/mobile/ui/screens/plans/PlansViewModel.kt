package com.gigshield.mobile.ui.screens.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigshield.mobile.core.common.Resource
import com.gigshield.mobile.data.repository.AuthRepository
import com.gigshield.mobile.data.repository.InsuranceRepository
import com.gigshield.mobile.domain.model.InsurancePlan
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PlansUiState(
    val user: User? = null,
    val plans: List<InsurancePlan> = emptyList(),
    val dynamicBasePremium: Int = 10,
    val isLoading: Boolean = false,
    val actionMessage: String? = null,
    val errorMessage: String? = null
)

class PlansViewModel(
    private val insuranceRepository: InsuranceRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlansUiState())
    val uiState: StateFlow<PlansUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUserFlow().collect { user ->
                _uiState.update { it.copy(user = user) }
                if (user != null) {
                    insuranceRepository.fetchDynamicRisk(user.city, user.zone)
                }
            }
        }

        viewModelScope.launch {
            insuranceRepository.dynamicBasePremium.collect { baseRate ->
                _uiState.update {
                    it.copy(
                        dynamicBasePremium = baseRate,
                        plans = insuranceRepository.getPlans(baseRate)
                    )
                }
            }
        }
    }

    fun subscribe(planTier: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, actionMessage = null, errorMessage = null) }
            when (val res = insuranceRepository.subscribePlan(planTier)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = res.data,
                            actionMessage = "Subscribed successfully to $planTier Plan!"
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = res.message)
                    }
                }
                else -> Unit
            }
        }
    }

    fun cancelPolicy() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, actionMessage = null, errorMessage = null) }
            when (val res = insuranceRepository.cancelPlan()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = res.data,
                            actionMessage = "Policy cancelled successfully."
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = res.message)
                    }
                }
                else -> Unit
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(actionMessage = null, errorMessage = null) }
    }
}
