package com.gigshield.mobile.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("demo@gigshield.ai") }
    var password by remember { mutableStateOf("password123") }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToHome()
            viewModel.resetState()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo / Branding
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(EmeraldPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "GigShield",
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "GigShield Mobile",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Income protection for every gig worker.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(36.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Worker Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = EmeraldPrimary)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = EmeraldPrimary)
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = state.errorMessage ?: "",
                    color = RoseError,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.login(email.trim(), password) },
                enabled = !state.isLoading && email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = DarkBackground,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Sign In & Protect Income",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = DarkBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = "New gig worker? Register Profile",
                    color = CyanAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
