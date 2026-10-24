package com.gigshield.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.TextMuted

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    subtext: String? = null
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.8.sp),
                    color = TextMuted
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtext != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp),
                    color = TextMuted
                )
            }
        }
    }
}
