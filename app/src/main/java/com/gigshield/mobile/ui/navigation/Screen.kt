package com.gigshield.mobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Login")
    object Register : Screen("register", "Register")

    // Bottom Navigation destinations
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Plans : Screen("plans", "Plans", Icons.Default.Shield)
    object Claims : Screen("claims", "Claims", Icons.Default.ReceiptLong)
    object Risk : Screen("risk", "Risk", Icons.Default.Thunderstorm)
    object Earnings : Screen("earnings", "Earnings", Icons.Default.TrendingUp)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)

    // Details
    object ClaimDetail : Screen("claim_detail/{claimId}", "Claim Details") {
        fun createRoute(claimId: String) = "claim_detail/$claimId"
    }

    companion object {
        val bottomNavItems = listOf(Home, Plans, Claims, Risk, Earnings, Profile)
    }
}
