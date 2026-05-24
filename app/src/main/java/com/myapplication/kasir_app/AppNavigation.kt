package com.myapplication.kasir_app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myapplication.kasir_app.ui.auth.LoginScreen
import com.myapplication.kasir_app.ui.dashboard.DashboardScreen
import com.myapplication.kasir_app.ui.owner.OwnerDashboardScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val userRole by authViewModel.userRole.collectAsState()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    // Cek role yang baru saja diambil dari Firestore
                    val currentRole = authViewModel.userRole.value
                    if (currentRole == "owner") {
                        navController.navigate("owner_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else if (currentRole == "kasir") {
                        navController.navigate("kasir_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("kasir_home") {
            DashboardScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("owner_home") {
            OwnerDashboardScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

    }
}
