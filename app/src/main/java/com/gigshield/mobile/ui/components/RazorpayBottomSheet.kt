package com.gigshield.mobile.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RazorpayBottomSheet(
    payoutAmount: Int,
    triggerEvent: String,
    onDismiss: () -> Unit
) {
    var isProcessing by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(1600) // Realistic UPI handshake simulation
        isProcessing = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp)
        ) {
            // Razorpay Dark Brand Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(RazorpayNavy)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(RazorpayBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "R",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Razorpay Instant Payout",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Body content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(
                    targetState = isProcessing,
                    label = "payoutTransition"
                ) { processing ->
                    if (processing) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 20.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(54.dp),
                                color = RazorpayBlue,
                                strokeWidth = 4.dp
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = "Processing UPI Transfer",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                            )
                            Text(
                                text = "Connecting to NPCI gateway...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF64748B)
                                )
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD1FAE5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = EmeraldDark,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "₹$payoutAmount",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                            )
                            Text(
                                text = "Instant UPI Payout Credited",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Transaction receipt card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    ReceiptRow(label = "Sender", value = "GigShield Parametric Trust")
                                    ReceiptRow(label = "Beneficiary", value = "Primary VPA (worker@okaxis)")
                                    ReceiptRow(label = "Trigger Source", value = triggerEvent)
                                    ReceiptRow(label = "Status", value = "SUCCESS (NPCI)")
                                    ReceiptRow(label = "Ref ID", value = "RZP" + System.currentTimeMillis().toString().takeLast(9))
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Done",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SECURED BY RAZORPAY UPI DIRECT",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF94A3B8),
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp, color = Color(0xFF64748B)))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A)))
    }
}
