package com.gigshield.mobile.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*
import com.gigshield.mobile.domain.model.Claim
import com.gigshield.mobile.domain.model.ClaimStatus
import com.gigshield.mobile.ui.components.*
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToPlans: () -> Unit,
    onNavigateToClaims: () -> Unit,
    onNavigateToClaimDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user

    val greeting = remember {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> "Good Morning"
            hour < 17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "GigShield",
                subtitle = user?.platform ?: "Gig Partner",
                serverMode = state.serverMode,
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = EmeraldPrimary
                        )
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
            // Worker Greeting Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        IndigoAccent.copy(alpha = 0.2f),
                                        EmeraldPrimary.copy(alpha = 0.15f)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "$greeting,",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "${user?.name ?: "Worker"} 👋",
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Black
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(EmeraldPrimary.copy(alpha = 0.2f))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = user?.city ?: "India",
                                        color = EmeraldPrimary,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "PLATFORM: ",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = user?.platform ?: "Zomato",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "PROTECTED EARNINGS: ",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "₹${user?.totalPayouts ?: 0}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = EmeraldPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Active Policy Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = if (user?.isPolicyActive == true)
                            EmeraldDark.copy(alpha = 0.15f)
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (user?.isPolicyActive == true) EmeraldPrimary.copy(alpha = 0.2f)
                                        else RoseError.copy(alpha = 0.2f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (user?.isPolicyActive == true) Icons.Default.Shield else Icons.Default.ShieldMoon,
                                    contentDescription = null,
                                    tint = if (user?.isPolicyActive == true) EmeraldPrimary else RoseError,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (user?.isPolicyActive == true) "${user.activePlan} Plan Active" else "No Active Policy",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (user?.isPolicyActive == true)
                                        "Remaining: ₹${user.coverageRemaining} / ₹${user.coverage}"
                                    else
                                        "Subscribe to protect income during disruptions",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted
                                )
                            }
                        }

                        Button(
                            onClick = onNavigateToPlans,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (user?.isPolicyActive == true) TealSecondary else EmeraldPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (user?.isPolicyActive == true) "Manage" else "Get Covered",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = DarkBackground
                            )
                        }
                    }
                }
            }

            // Live Weather & Risk Gauge
            item {
                WeatherCard(
                    city = user?.city ?: "Mumbai",
                    precipitationMm = 21.4,
                    temperatureC = 28,
                    weatherCondition = "Monsoon Precipitation"
                )
            }

            item {
                RiskGaugeCard(
                    riskScore = user?.riskScore ?: 0.65,
                    zone = user?.zone ?: "Flood Prone (Dharavi)",
                    city = user?.city ?: "Mumbai"
                )
            }

            // Stats Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Coverage Left",
                        value = "₹${user?.coverageRemaining ?: 0}",
                        icon = Icons.Default.AccountBalanceWallet,
                        iconTint = CyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Weekly Claims",
                        value = "${user?.claimsThisWeek ?: 0} / ${user?.maxClaimsPerWeek ?: 2}",
                        icon = Icons.Default.Receipt,
                        iconTint = IndigoAccent,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Hardware Telemetry & Fraud Sensors
            item {
                LiveSensorCard(
                    variance = state.telemetryState.variance,
                    isGpsValid = true,
                    city = user?.city ?: "Mumbai"
                )
            }

            // Quick Actions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onNavigateToClaims,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = DarkBackground)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simulate Claim", fontWeight = FontWeight.Bold, color = DarkBackground)
                    }

                    OutlinedButton(
                        onClick = onNavigateToPlans,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Compare Plans", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Recent Claims Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT CLAIM ADJUDICATIONS",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                        color = TextMuted
                    )
                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.bodyMedium.copy(color = CyanAccent, fontWeight = FontWeight.Bold),
                        modifier = Modifier.clickable { onNavigateToClaims() }
                    )
                }
            }

            if (state.claims.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No claims adjudicated yet. Simulate a disruption to test.", color = TextMuted, fontSize = 13.sp)
                    }
                }
            } else {
                items(state.claims.take(3)) { claim ->
                    ClaimListItem(claim = claim, onClick = { onNavigateToClaimDetail(claim.id) })
                }
            }
        }
    }
}

@Composable
fun ClaimListItem(claim: Claim, onClick: () -> Unit) {
    val (statusColor, statusText) = when (claim.status) {
        ClaimStatus.APPROVED -> EmeraldPrimary to "PAID (UPI)"
        ClaimStatus.UNDER_REVIEW -> AmberWarning to "PENDING REVIEW"
        ClaimStatus.REJECTED -> RoseError to "BLOCKED"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (claim.status) {
                            ClaimStatus.APPROVED -> Icons.Default.CheckCircle
                            ClaimStatus.UNDER_REVIEW -> Icons.Default.HourglassBottom
                            ClaimStatus.REJECTED -> Icons.Default.Cancel
                        },
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = claim.triggerEvent,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${claim.city} · ${claim.createdAt.take(10)}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${claim.payoutAmount}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = if (claim.status == ClaimStatus.APPROVED) EmeraldPrimary else TextMuted
                    )
                )
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = statusColor
                )
            }
        }
    }
}
