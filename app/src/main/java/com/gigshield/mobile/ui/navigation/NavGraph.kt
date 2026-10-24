package com.gigshield.mobile.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gigshield.mobile.GigShieldApp
import com.gigshield.mobile.ui.screens.auth.AuthViewModel
import com.gigshield.mobile.ui.screens.auth.LoginScreen
import com.gigshield.mobile.ui.screens.auth.RegisterScreen
import com.gigshield.mobile.ui.screens.claims.ClaimDetailScreen
import com.gigshield.mobile.ui.screens.claims.ClaimsScreen
import com.gigshield.mobile.ui.screens.claims.ClaimsViewModel
import com.gigshield.mobile.ui.screens.earnings.EarningsScreen
import com.gigshield.mobile.ui.screens.earnings.EarningsViewModel
import com.gigshield.mobile.ui.screens.home.HomeScreen
import com.gigshield.mobile.ui.screens.home.HomeViewModel
import com.gigshield.mobile.ui.screens.plans.PlansScreen
import com.gigshield.mobile.ui.screens.plans.PlansViewModel
import com.gigshield.mobile.ui.screens.profile.ProfileScreen
import com.gigshield.mobile.ui.screens.profile.ProfileViewModel
import com.gigshield.mobile.ui.screens.risk.RiskDashboardScreen
import com.gigshield.mobile.ui.screens.risk.RiskViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    startDestination: String,
    app: GigShieldApp
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(Screen.Login.route) {
            val authViewModel = AuthViewModel(app.authRepository)
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            val authViewModel = AuthViewModel(app.authRepository)
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            val homeViewModel = HomeViewModel(
                authRepository = app.authRepository,
                claimsRepository = app.claimsRepository,
                sensorManager = app.sensorManager,
                sessionManager = app.sessionManager
            )
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToPlans = { navController.navigate(Screen.Plans.route) },
                onNavigateToClaims = { navController.navigate(Screen.Claims.route) },
                onNavigateToClaimDetail = { claimId ->
                    navController.navigate(Screen.ClaimDetail.createRoute(claimId))
                }
            )
        }

        composable(Screen.Plans.route) {
            val plansViewModel = PlansViewModel(
                insuranceRepository = app.insuranceRepository,
                authRepository = app.authRepository
            )
            PlansScreen(
                viewModel = plansViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Claims.route) {
            val claimsViewModel = ClaimsViewModel(
                claimsRepository = app.claimsRepository,
                authRepository = app.authRepository,
                sensorManager = app.sensorManager
            )
            ClaimsScreen(
                viewModel = claimsViewModel,
                onNavigateToClaimDetail = { claimId ->
                    navController.navigate(Screen.ClaimDetail.createRoute(claimId))
                }
            )
        }

        composable(
            route = Screen.ClaimDetail.route,
            arguments = listOf(navArgument("claimId") { type = NavType.StringType })
        ) { backStackEntry ->
            val claimId = backStackEntry.arguments?.getString("claimId") ?: ""
            val claimsViewModel = ClaimsViewModel(
                claimsRepository = app.claimsRepository,
                authRepository = app.authRepository,
                sensorManager = app.sensorManager
            )
            ClaimDetailScreen(
                claimId = claimId,
                viewModel = claimsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Risk.route) {
            val riskViewModel = RiskViewModel(
                authRepository = app.authRepository,
                networkModule = app.networkModule
            )
            RiskDashboardScreen(viewModel = riskViewModel)
        }

        composable(Screen.Earnings.route) {
            val earningsViewModel = EarningsViewModel(
                authRepository = app.authRepository,
                claimsRepository = app.claimsRepository
            )
            EarningsScreen(viewModel = earningsViewModel)
        }

        composable(Screen.Profile.route) {
            val profileViewModel = ProfileViewModel(
                authRepository = app.authRepository,
                sessionManager = app.sessionManager
            )
            ProfileScreen(
                viewModel = profileViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
