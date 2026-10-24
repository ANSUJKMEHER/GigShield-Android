package com.gigshield.mobile.ui.screens.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
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
import com.gigshield.mobile.domain.model.InsurancePlan
import com.gigshield.mobile.ui.components.TopBar

@Composable
fun PlansScreen(
    viewModel: PlansViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user
    val activePlan = user?.activePlan ?: "None"

    var showCancelDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Parametric Micro-Policies",
                subtitle = "Weekly dynamically priced plans"
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
            // Notice banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, IndigoAccent.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = IndigoAccent.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Premiums are dynamically adjusted via the Brain.js Neural Network based on your threat zone and live weather telemetry.",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            if (state.actionMessage != null) {
                item {
                    Text(
                        text = state.actionMessage ?: "",
                        color = EmeraldPrimary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Plan Cards
            items(state.plans) { plan ->
                PlanCard(
                    plan = plan,
                    isCurrentPlan = plan.tier.equals(activePlan, ignoreCase = true),
                    isLoading = state.isLoading,
                    onSubscribe = { viewModel.subscribe(plan.tier) },
                    onCancel = { showCancelDialog = true }
                )
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancel Policy Protection?") },
            text = {
                Text("Your income will no longer be protected during severe weather or curfew events.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelPolicy()
                        showCancelDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Cancel Policy", color = TextWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Keep Active")
                }
            }
        )
    }
}

@Composable
fun PlanCard(
    plan: InsurancePlan,
    isCurrentPlan: Boolean,
    isLoading: Boolean,
    onSubscribe: () -> Unit,
    onCancel: () -> Unit
) {
    val borderColor = if (isCurrentPlan) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(if (isCurrentPlan) 2.dp else 1.dp, borderColor, RoundedCornerShape(22.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentPlan) EmeraldDark.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${plan.tier} Plan",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (plan.badge != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PurpleAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = plan.badge,
                                    color = PurpleAccent,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                    Text(
                        text = plan.targetWorker,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = TextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${plan.basePremium}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = EmeraldPrimary
                        )
                    )
                    Text(
                        text = "per week",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = plan.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Features List
            plan.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isCurrentPlan) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RoseError)
                    ) {
                        Text("Cancel Coverage")
                    }

                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = EmeraldPrimary.copy(alpha = 0.2f),
                            disabledContentColor = EmeraldPrimary
                        )
                    ) {
                        Text("Active Tier", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Button(
                    onClick = onSubscribe,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text(
                        text = "Subscribe to ${plan.tier}",
                        fontWeight = FontWeight.Bold,
                        color = DarkBackground
                    )
                }
            }
        }
    }
}
