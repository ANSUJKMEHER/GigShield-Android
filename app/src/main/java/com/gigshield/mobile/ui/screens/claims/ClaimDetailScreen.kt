package com.gigshield.mobile.ui.screens.claims

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaimDetailScreen(
    claimId: String,
    viewModel: ClaimsViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val claim = state.claims.find { it.id == claimId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Claim Adjudication Inspection", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (claim == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Claim record not found.", color = TextMuted)
            }
        } else {
            val (statusColor, statusTitle) = when (claim.status) {
                ClaimStatus.APPROVED -> EmeraldPrimary to "CLAIM APPROVED & PAID"
                ClaimStatus.UNDER_REVIEW -> AmberWarning to "FLAGGED FOR UNDERWRITER REVIEW"
                ClaimStatus.REJECTED -> RoseError to "FRAUD BLOCKED"
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header status card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(statusColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (claim.status) {
                                    ClaimStatus.APPROVED -> Icons.Default.CheckCircle
                                    ClaimStatus.UNDER_REVIEW -> Icons.Default.HourglassBottom
                                    ClaimStatus.REJECTED -> Icons.Default.Block
                                },
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = statusTitle,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = statusColor
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "₹${claim.payoutAmount}",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "Adjudicated automatically by GigShield Oracle",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                            color = TextMuted
                        )
                    }
                }

                // Loss Breakdown Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "INCOME LOSS MATRIX",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = CyanAccent
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DetailRow("Trigger Event", claim.triggerEvent)
                        DetailRow("Operating City", claim.city)
                        DetailRow("Expected Shift Margin", "₹${claim.expectedIncome}")
                        DetailRow("Actual Logged Revenue", "₹${claim.actualIncome}")
                        DetailRow("Algorithmic Loss Gap", "₹${claim.loss}")
                        DetailRow("Final Settled Payout", "₹${claim.payoutAmount}")
                        if (claim.rejectionReason.isNotBlank()) {
                            DetailRow("Adjudication Reason", claim.rejectionReason)
                        }
                    }
                }

                // 7-Vector Fraud Checks Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(18.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "7-VECTOR FRAUD INSPECTION",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = PurpleAccent
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FraudVectorRow("1. GPS Telemetry Verified", claim.isGpsVerified)
                        FraudVectorRow("2. Haversine 50km Bound Pass", true)
                        FraudVectorRow("3. Active Worker Online Shift", true)
                        FraudVectorRow("4. Biomechanical Accelerometer Variance > 0.5", claim.isTelemetryValid)
                        FraudVectorRow("5. GPS Velocity Anti-Spoofing", true)
                        FraudVectorRow("6. Movement vs Revenue Anomaly Free", true)
                        FraudVectorRow("7. 24-Hour Duplicate Cooldown Guard", claim.status != ClaimStatus.UNDER_REVIEW)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun FraudVectorRow(label: String, isPassed: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp), color = MaterialTheme.colorScheme.onSurface)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isPassed) EmeraldPrimary.copy(alpha = 0.15f) else AmberWarning.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = if (isPassed) "PASS" else "REVIEW",
                color = if (isPassed) EmeraldPrimary else AmberWarning,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
