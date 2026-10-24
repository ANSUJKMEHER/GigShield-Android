package com.gigshield.mobile.ui.screens.claims

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*
import com.gigshield.mobile.domain.model.Claim
import com.gigshield.mobile.domain.model.ClaimStatus
import com.gigshield.mobile.ui.components.*
import com.gigshield.mobile.ui.screens.home.ClaimListItem

@Composable
fun ClaimsScreen(
    viewModel: ClaimsViewModel,
    onNavigateToClaimDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user

    var showSimulateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Claims & Adjudications",
                subtitle = "Zero-Touch Parametric Settlements",
                actions = {
                    IconButton(onClick = { viewModel.fetchClaims() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = EmeraldPrimary)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showSimulateDialog = true },
                containerColor = EmeraldPrimary,
                contentColor = DarkBackground,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Simulate Disruption", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp)
        ) {
            // Simulation in progress indicator
            if (state.isSimulating) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, IndigoAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = IndigoAccent.copy(alpha = 0.15f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = CyanAccent,
                                    strokeWidth = 3.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = when (state.simulationStep) {
                                        1 -> "Fetching Open-Meteo Satellite Signals..."
                                        2 -> "Executing 7-Vector Fraud Engine..."
                                        else -> "Finalizing Parametric Payout..."
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CyanAccent
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = {
                                    when (state.simulationStep) {
                                        1 -> 0.35f
                                        2 -> 0.70f
                                        else -> 1.0f
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = CyanAccent
                            )
                        }
                    }
                }
            }

            // Summary Metric Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LIFETIME RECOVERED PAYOUTS",
                                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "₹${user?.totalPayouts ?: 0}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldPrimary
                                )
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${state.claims.size} Total Claims",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Zero Paperwork",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                                color = TealSecondary
                            )
                        }
                    }
                }
            }

            if (state.errorMessage != null) {
                item {
                    Text(
                        text = state.errorMessage ?: "",
                        color = RoseError,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Header for list
            item {
                Text(
                    text = "ADJUDICATION LOG",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                    color = TextMuted
                )
            }

            if (state.claims.isEmpty() && !state.isSimulating) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No claims recorded yet.\nTap 'Simulate Disruption' below to trigger an algorithmic payout.",
                            color = TextMuted,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else {
                items(state.claims) { claim ->
                    ClaimListItem(claim = claim, onClick = { onNavigateToClaimDetail(claim.id) })
                }
            }
        }
    }

    // Disruption Simulator Modal
    if (showSimulateDialog) {
        SimulateClaimDialog(
            cityName = user?.city ?: "Mumbai",
            onSimulate = { trigger, exp, act ->
                showSimulateDialog = false
                viewModel.simulateDisruption(trigger, exp, act)
            },
            onDismiss = { showSimulateDialog = false }
        )
    }

    // Razorpay Instant UPI Bottom Sheet
    if (state.showRazorpayModal && state.lastSimulatedClaim != null) {
        RazorpayBottomSheet(
            payoutAmount = state.lastSimulatedClaim!!.payoutAmount,
            triggerEvent = state.lastSimulatedClaim!!.triggerEvent,
            onDismiss = { viewModel.dismissRazorpay() }
        )
    }
}
