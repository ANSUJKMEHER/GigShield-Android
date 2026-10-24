package com.gigshield.mobile.ui.screens.earnings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.gigshield.mobile.domain.model.DailyEarnings
import com.gigshield.mobile.ui.components.StatCard
import com.gigshield.mobile.ui.components.TopBar

@Composable
fun EarningsScreen(
    viewModel: EarningsViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user
    val summary = state.summary

    Scaffold(
        topBar = {
            TopBar(
                title = "Income Protection",
                subtitle = "Algorithmic Loss Gap & Payouts"
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp)
        ) {
            // Net Income Protected Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(22.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "TOTAL NET INCOME PROTECTED",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = CyanAccent
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "₹${summary?.netIncomeProtected ?: 0}",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = EmeraldPrimary
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(EmeraldPrimary.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "100% COVERED",
                                    color = EmeraldPrimary,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Visual progress bar
                        LinearProgressIndicator(
                            progress = { 1.0f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = EmeraldPrimary,
                            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    }
                }
            }

            // Stats Matrix
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Expected Shift",
                        value = "₹${summary?.weeklyExpected ?: 0}",
                        icon = Icons.Default.TrendingUp,
                        iconTint = IndigoAccent,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Actual Revenue",
                        value = "₹${summary?.weeklyActual ?: 0}",
                        icon = Icons.Default.AccountBalanceWallet,
                        iconTint = CyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Disruption Loss",
                        value = "₹${summary?.totalLoss ?: 0}",
                        icon = Icons.Default.TrendingDown,
                        iconTint = RoseError,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Recovered Payout",
                        value = "₹${summary?.totalRecovered ?: 0}",
                        icon = Icons.Default.CheckCircle,
                        iconTint = EmeraldPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Daily 7-day Breakdown
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "WEEKLY INCOME RECOVERY BREAKDOWN",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        summary?.dailyBreakdown?.forEach { daily ->
                            DailyRowItem(daily = daily)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyRowItem(daily: DailyEarnings) {
    val hasLoss = daily.expected > daily.actual

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = daily.day,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (hasLoss) "Disruption Detected" else "Standard Shift Completed",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 11.sp,
                    color = if (hasLoss) AmberWarning else TextMuted
                )
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${daily.actual + daily.lossRecovered}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (daily.lossRecovered > 0) {
                    Text(
                        text = "+₹${daily.lossRecovered} via UPI",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldPrimary
                        )
                    )
                }
            }
        }
    }
}
