package com.gigshield.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*

@Composable
fun LiveSensorCard(
    variance: Double,
    isGpsValid: Boolean,
    city: String
) {
    val isTelemetryValid = variance >= 0.5

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "7-VECTOR FRAUD SENSORS",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                    color = CyanAccent
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(EmeraldPrimary.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "STREAMING",
                        color = EmeraldPrimary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Biomechanical Telemetry
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sensors,
                                contentDescription = null,
                                tint = if (isTelemetryValid) EmeraldPrimary else AmberWarning,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Biometrics",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Var: ${"%.2f".format(variance)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isTelemetryValid) "Human Motion" else "Static Device",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp),
                            color = if (isTelemetryValid) EmeraldPrimary else RoseError
                        )
                    }
                }

                // GPS 50km Bound
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = CyanAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Radius Lock",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = city,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "< 50km Bound Pass",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 10.sp),
                            color = EmeraldPrimary
                        )
                    }
                }
            }
        }
    }
}
