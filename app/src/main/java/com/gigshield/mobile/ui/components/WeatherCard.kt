package com.gigshield.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*

@Composable
fun WeatherCard(
    city: String,
    precipitationMm: Double = 18.5,
    temperatureC: Int = 29,
    weatherCondition: String = "Heavy Monsoon Rain"
) {
    val isHeavyRain = precipitationMm > 10.0
    val gradientColors = if (isHeavyRain) {
        listOf(IndigoAccent.copy(alpha = 0.35f), TealSecondary.copy(alpha = 0.2f))
    } else {
        listOf(TealSecondary.copy(alpha = 0.25f), EmeraldPrimary.copy(alpha = 0.15f))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(gradientColors))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "OPEN-METEO SATELLITE TELEMETRY",
                            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                            color = CyanAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$city · $temperatureC°C",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$weatherCondition (${precipitationMm}mm)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isHeavyRain) IndigoAccent.copy(alpha = 0.3f)
                            else AmberWarning.copy(alpha = 0.3f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isHeavyRain) Icons.Default.Thunderstorm else Icons.Default.WbSunny,
                        contentDescription = "Weather",
                        tint = if (isHeavyRain) CyanAccent else AmberWarning,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}
