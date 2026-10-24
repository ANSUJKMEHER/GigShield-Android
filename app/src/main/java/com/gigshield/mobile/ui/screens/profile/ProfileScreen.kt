package com.gigshield.mobile.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.gigshield.mobile.core.common.Constants
import com.gigshield.mobile.core.theme.*
import com.gigshield.mobile.ui.components.TopBar

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user

    var name by remember(user?.name) { mutableStateOf(user?.name ?: "Ravi Kumar") }
    var city by remember(user?.city) { mutableStateOf(user?.city ?: "Mumbai") }
    var platform by remember(user?.platform) { mutableStateOf(user?.platform ?: "Zomato") }
    var zone by remember(user?.zone) { mutableStateOf(user?.zone ?: "Flood Prone (Dharavi)") }

    var customUrl by remember(state.customUrl) { mutableStateOf(state.customUrl) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Worker Profile & Config",
                subtitle = "Threat vectors & backend connection"
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(22.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(EmeraldPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = user?.name ?: "Ravi Kumar",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = user?.email ?: "ravi@gigshield.ai",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Platform: ${user?.platform ?: "Zomato"} · ${user?.city ?: "Mumbai"}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.sp, color = CyanAccent)
                        )
                    }
                }
            }

            if (state.updateMessage != null) {
                Text(
                    text = state.updateMessage ?: "",
                    color = EmeraldPrimary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Risk Profile Editor
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "RISK PROFILE & OPERATING ZONE",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                        color = CyanAccent
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Worker Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Operating City") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = platform,
                        onValueChange = { platform = it },
                        label = { Text("Gig Platform") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = zone,
                        onValueChange = { zone = it },
                        label = { Text("Threat Zone (Low Risk / General / Flood Prone)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.updateProfile(name, city, platform, zone) },
                        enabled = !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text("Recalculate AI Risk & Save", fontWeight = FontWeight.Bold, color = DarkBackground)
                    }
                }
            }

            // Server Connection Switcher
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "BACKEND CONNECTION TARGET",
                        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
                        color = IndigoAccent
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = state.serverMode == Constants.MODE_CLOUD,
                            onClick = { viewModel.setServerMode(Constants.MODE_CLOUD) },
                            label = { Text("Render Cloud", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = state.serverMode == Constants.MODE_EMULATOR,
                            onClick = { viewModel.setServerMode(Constants.MODE_EMULATOR) },
                            label = { Text("10.0.2.2 Host", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = state.serverMode == Constants.MODE_DEMO_OFFLINE,
                            onClick = { viewModel.setServerMode(Constants.MODE_DEMO_OFFLINE) },
                            label = { Text("Offline Demo", fontSize = 11.sp) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = customUrl,
                        onValueChange = {
                            customUrl = it
                            viewModel.setCustomUrl(it)
                        },
                        label = { Text("Custom API URL", fontSize = 11.sp) },
                        placeholder = { Text("http://192.168.1.100:5000/") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Sign Out Button
            Button(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RoseError)
            ) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = null, tint = TextWhite)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out", fontWeight = FontWeight.Bold, color = TextWhite)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
