package com.gigshield.mobile.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*

@Composable
fun RiskGaugeCard(
    riskScore: Double,
    zone: String,
    city: String
) {
    val animatedProgress by animateFloatAsState(
        targetValue = riskScore.toFloat().coerceIn(0f, 1f),
        label = "riskProgress"
    )

    val (riskLabel, riskColor) = when {
        riskScore >= 0.7 -> "HIGH RISK" to RoseError
        riskScore >= 0.4 -> "MODERATE" to AmberWarning
        else -> "LOW RISK" to EmeraldPrimary
    }

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
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "BRAIN.JS NEURAL RISK ENGINE",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.8.sp),
                        color = PurpleAccent
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$zone · $city",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(riskColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = riskLabel,
                        color = riskColor,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Risk Severity Score: ${(riskScore * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Dynamic Pricing Active",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = riskColor,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        }
    }
}
