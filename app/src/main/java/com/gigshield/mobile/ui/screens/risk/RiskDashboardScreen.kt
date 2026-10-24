package com.gigshield.mobile.ui.screens.risk

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*
import com.gigshield.mobile.domain.model.DisruptionType
import com.gigshield.mobile.ui.components.RiskGaugeCard
import com.gigshield.mobile.ui.components.TopBar
import com.gigshield.mobile.ui.components.WeatherCard

@Composable
fun RiskDashboardScreen(
    viewModel: RiskViewModel
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user

    Scaffold(
        topBar = {
            TopBar(
                title = "Parametric Risk & Oracles",
                subtitle = "Real-time satellite & predictive telemetry",
                actions = {
                    IconButton(onClick = { viewModel.loadPredictiveForecast() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = EmeraldPrimary)
                    }
                }
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
            // Live threat card
            item {
                RiskGaugeCard(
                    riskScore = user?.riskScore ?: 0.65,
                    zone = user?.zone ?: "Flood Prone (Dharavi)",
                    city = user?.city ?: "Mumbai"
                )
            }

            item {
                WeatherCard(
                    city = user?.city ?: "Mumbai",
                    precipitationMm = 21.4,
                    temperatureC = 28,
                    weatherCondition = "Live Monsoon Satellite Radar"
                )
            }

            // Predictive 7-Day Forecast Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "7-DAY PREDICTIVE RISK FORECAST",
                                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                                color = PurpleAccent
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PurpleAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "BRAIN.JS ML",
                                    color = PurpleAccent,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Forecast Bars
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            state.forecast.forEach { dayForecast ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    val barHeight = (dayForecast.riskFactor * 70).coerceAtLeast(10.0).dp
                                    val barColor = if (dayForecast.riskFactor >= 0.7) RoseError
                                    else if (dayForecast.riskFactor >= 0.4) AmberWarning
                                    else EmeraldPrimary

                                    Box(
                                        modifier = Modifier
                                            .width(18.dp)
                                            .height(barHeight)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(barColor)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = dayForecast.day,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (dayForecast.alert != null) RoseError else TextMuted
                                        )
                                    )
                                }
                            }
                        }

                        // Anomaly note if Wednesday has alert
                        val anomaly = state.forecast.find { it.alert != null }
                        if (anomaly != null) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(RoseError.copy(alpha = 0.15f))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = RoseError,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Forecast Warning: ${anomaly.day} - ${anomaly.alert} (${anomaly.predictedClaims} predicted claims)",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp, color = RoseError, fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5 Supported Disruption Vectors
            item {
                Text(
                    text = "5 SUPPORTED DISRUPTION TRIGGERS",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                    color = TextMuted
                )
            }

            items(DisruptionType.values()) { type ->
                DisruptionVectorCard(type = type)
            }

            // 5th Oracle background trigger note
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, TealSecondary.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = TealSecondary.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Bolt, contentDescription = null, tint = CyanAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "5. Autonomous Zero-Touch Background Oracle",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Backend autoTriggerTask.js runs every 10 minutes checking Mumbai, Delhi, and Bangalore satellite precipitation. Zero worker actions required.",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DisruptionVectorCard(type: DisruptionType) {
    val (icon, color) = when (type) {
        DisruptionType.HEAVY_RAIN -> Icons.Default.Thunderstorm to IndigoAccent
        DisruptionType.HIGH_AQI -> Icons.Default.Air to AmberWarning
        DisruptionType.CURFEW -> Icons.Default.Gavel to RoseError
        DisruptionType.APP_CRASH -> Icons.Default.CloudOff to CyanAccent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = type.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${type.triggerSource} · Threshold: ${type.defaultThreshold}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = type.expectedImpact,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp, color = color)
                )
            }
        }
    }
}
