package com.myapplication.kasir_app.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout // Import icon logout
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DashboardTab(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : DashboardTab("home", "Dashboard", Icons.Outlined.Home)
    object Transaksi : DashboardTab("transaksi", "Transaksi", Icons.Outlined.Receipt)
    object Produk : DashboardTab("produk", "Produk", Icons.Outlined.Inventory2)
    object Kategori : DashboardTab("kategori", "Kategori", Icons.Outlined.Label)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf<DashboardTab>(DashboardTab.Home) }

    // State untuk mengontrol muncul/tidaknya dialog konfirmasi logout
    var showLogoutDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        DashboardTab.Home,
        DashboardTab.Transaksi,
        DashboardTab.Produk,
        DashboardTab.Kategori
    )

    // Logika Dialog Konfirmasi Logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Apakah Anda yakin ingin keluar dari aplikasi?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout() // Memanggil fungsi logout yang dikirim dari MainActivity/NavHost
                    }
                ) {
                    Text("Ya, Keluar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            DashboardTab.Home -> "Dashboard Cafe"
                            DashboardTab.Transaksi -> "Transaksi Kasir"
                            DashboardTab.Produk -> "Kelola Menu"
                            DashboardTab.Kategori -> "Kategori Menu"
                        }
                    )
                },
                // --- BAGIAN ACTIONS INI ADALAH PENAMBAHAN TOMBOL LOGOUT ---
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error // Memberikan warna merah
                        )
                    }
                }
                // --------------------------------------------------------
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                DashboardTab.Home -> HomePanel()
                DashboardTab.Transaksi -> TransaksiPanel()
                DashboardTab.Produk -> ProdukPanel()
                DashboardTab.Kategori -> KategoriPanel()
            }
        }
    }
}