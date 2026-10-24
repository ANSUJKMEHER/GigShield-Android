package com.gigshield.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gigshield.mobile.core.theme.GigShieldTheme
import com.gigshield.mobile.ui.navigation.BottomNavBar
import com.gigshield.mobile.ui.navigation.NavGraph
import com.gigshield.mobile.ui.navigation.Screen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as GigShieldApp
        val isUserLoggedIn = !app.sessionManager.getToken().isNullOrBlank() ||
                app.sessionManager.getUserId() != null

        val startDest = if (isUserLoggedIn) Screen.Home.route else Screen.Login.route

        setContent {
            val isDark = app.sessionManager.isDarkMode()
            GigShieldTheme(darkTheme = isDark) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in Screen.bottomNavItems.map { it.route }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        paddingValues = innerPadding,
                        startDestination = startDest,
                        app = app
                    )
                }
            }
        }
    }
}
