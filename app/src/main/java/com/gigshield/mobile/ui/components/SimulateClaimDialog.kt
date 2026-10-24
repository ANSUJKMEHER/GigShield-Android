package com.gigshield.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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

@Composable
fun SimulateClaimDialog(
    cityName: String,
    onSimulate: (triggerEvent: String, expected: Int, actual: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedEvent by remember { mutableStateOf("Heavy Rain") }
    var expectedIncome by remember { mutableStateOf("800") }
    var actualIncome by remember { mutableStateOf("200") }

    val loss = (expectedIncome.toIntOrNull() ?: 0) - (actualIncome.toIntOrNull() ?: 0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "SIMULATE PARAMETRIC DISRUPTION",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                    color = EmeraldPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Disruption Oracle ($cityName)",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Choose a disruption trigger to test the 7-vector fraud engine and automatic UPI payout pipeline:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Event Selector Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EventSelectorChip(
                        label = "Heavy Rain",
                        icon = Icons.Default.Thunderstorm,
                        isSelected = selectedEvent == "Heavy Rain",
                        onClick = { selectedEvent = "Heavy Rain" },
                        modifier = Modifier.weight(1f)
                    )
                    EventSelectorChip(
                        label = "High AQI",
                        icon = Icons.Default.Air,
                        isSelected = selectedEvent == "High AQI",
                        onClick = { selectedEvent = "High AQI" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EventSelectorChip(
                        label = "Curfew",
                        icon = Icons.Default.Warning,
                        isSelected = selectedEvent == "Curfew",
                        onClick = { selectedEvent = "Curfew" },
                        modifier = Modifier.weight(1f)
                    )
                    EventSelectorChip(
                        label = "App Crash",
                        icon = Icons.Default.CloudOff,
                        isSelected = selectedEvent == "App Crash",
                        onClick = { selectedEvent = "App Crash" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Expected & Actual Income
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = expectedIncome,
                        onValueChange = { expectedIncome = it },
                        label = { Text("Expected (₹)", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = actualIncome,
                        onValueChange = { actualIncome = it },
                        label = { Text("Actual (₹)", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Algorithmic Loss Gap:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                            color = TextMuted
                        )
                        Text(
                            text = "₹${loss.coerceAtLeast(0)}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (loss >= 100) EmeraldPrimary else RoseError
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val exp = expectedIncome.toIntOrNull() ?: 800
                    val act = actualIncome.toIntOrNull() ?: 200
                    onSimulate(selectedEvent, exp, act)
                },
                enabled = loss >= 100,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Run Zero-Touch Adjudication", fontWeight = FontWeight.Bold, color = DarkBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
private fun EventSelectorChip(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    val bgColor = if (isSelected) EmeraldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) EmeraldPrimary else TextMuted,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else TextMuted
                )
            )
        }
    }
}
