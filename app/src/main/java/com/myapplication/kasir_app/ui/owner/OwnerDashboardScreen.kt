package com.myapplication.kasir_app.ui.owner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class OwnerTab(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : OwnerTab("home", "Dashboard Owner", Icons.Outlined.Home)
    object Laporan : OwnerTab("laporan", "Laporan", Icons.Outlined.BarChart)
    object Kasir : OwnerTab("kasir", "Kelola Kasir", Icons.Outlined.PeopleAlt)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerDashboardScreen(onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf<OwnerTab>(OwnerTab.Home) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        OwnerTab.Home, 
        OwnerTab.Laporan, 
        OwnerTab.Kasir
    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Yakin ingin keluar?") },
            confirmButton = {
                TextButton(onClick = { onLogout() }) { Text("Ya", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(selectedTab.label) },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, null) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedTab) {
                OwnerTab.Home -> OwnerHomePanel()
                OwnerTab.Laporan -> LaporanPanel()
                OwnerTab.Kasir -> KelolakasirPanel()
            }
        }
    }
}
