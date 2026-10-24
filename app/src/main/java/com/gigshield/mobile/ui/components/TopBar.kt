package com.gigshield.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    subtitle: String? = null,
    serverMode: String = "CLOUD",
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "GigShield",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                when (serverMode) {
                                    "EMULATOR" -> IndigoAccent.copy(alpha = 0.2f)
                                    "DEMO_OFFLINE" -> AmberWarning.copy(alpha = 0.2f)
                                    else -> EmeraldPrimary.copy(alpha = 0.2f)
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = serverMode,
                            color = when (serverMode) {
                                "EMULATOR" -> IndigoAccent
                                "DEMO_OFFLINE" -> AmberWarning
                                else -> EmeraldPrimary
                            },
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = actions
    )
}
