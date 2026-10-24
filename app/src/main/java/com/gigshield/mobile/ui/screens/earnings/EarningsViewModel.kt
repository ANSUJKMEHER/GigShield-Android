package com.gigshield.mobile.ui.screens.earnings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigshield.mobile.data.repository.AuthRepository
import com.gigshield.mobile.data.repository.ClaimsRepository
import com.gigshield.mobile.domain.model.EarningsSummary
import com.gigshield.mobile.domain.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class EarningsUiState(
    val user: User? = null,
    val summary: EarningsSummary? = null,
    val isLoading: Boolean = false
)

class EarningsViewModel(
    private val authRepository: AuthRepository,
    private val claimsRepository: ClaimsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EarningsUiState())
    val uiState: StateFlow<EarningsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                authRepository.getCurrentUserFlow(),
                claimsRepository.getClaimsFlow()
            ) { user, claims ->
                val summary = EarningsSummary.calculate(
                    claims = claims,
                    userTotalPayouts = user?.totalPayouts ?: 0
                )
                EarningsUiState(user = user, summary = summary)
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
}
