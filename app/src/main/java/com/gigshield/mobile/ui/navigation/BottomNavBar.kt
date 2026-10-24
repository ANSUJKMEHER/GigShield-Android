package com.gigshield.mobile.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gigshield.mobile.core.theme.EmeraldPrimary
import com.gigshield.mobile.core.theme.TextMuted

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Screen.bottomNavItems.forEach { screen ->
            val isSelected = currentDestination?.route == screen.route
            NavigationBarItem(
                icon = {
                    screen.icon?.let { icon ->
                        Icon(
                            imageVector = icon,
                            contentDescription = screen.title,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EmeraldPrimary,
                    selectedTextColor = EmeraldPrimary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = EmeraldPrimary.copy(alpha = 0.15f)
                )
            )
        }
    }
}
