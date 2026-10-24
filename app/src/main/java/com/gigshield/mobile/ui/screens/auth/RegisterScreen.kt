package com.gigshield.mobile.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gigshield.mobile.core.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("Ravi Kumar") }
    var email by remember { mutableStateOf("ravi.rider@gigshield.ai") }
    var password by remember { mutableStateOf("password123") }
    var city by remember { mutableStateOf("Mumbai") }
    var platform by remember { mutableStateOf("Zomato") }
    var zone by remember { mutableStateOf("Flood Prone (Dharavi)") }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToHome()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Worker Registration", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Set Up Parametric Protection",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Our Neural Network will calculate your dynamic risk profile.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Operating City (e.g. Mumbai, Bangalore, Pune)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = platform,
                onValueChange = { platform = it },
                label = { Text("Gig Platform (Zomato, Swiggy, Uber, Zepto)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = zone,
                onValueChange = { zone = it },
                label = { Text("Threat Zone (Low Risk / General / Flood Prone)") },
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
                onClick = {
                    viewModel.signup(name, email, password, city, platform, zone)
                },
                enabled = !state.isLoading && name.isNotBlank() && email.isNotBlank(),
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
                        text = "Compute AI Risk & Register",
                        fontWeight = FontWeight.Bold,
                        color = DarkBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
