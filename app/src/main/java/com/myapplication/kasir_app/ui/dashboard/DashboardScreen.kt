package com.myapplication.kasir_app.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DashboardTab(
    val route: String,
    val label: String,
    val icon: ImageVector
) {

    object Home : DashboardTab(
        "home",
        "Dashboard",
        Icons.Outlined.Home
    )

    object Transaksi : DashboardTab(
        "transaksi",
        "Transaksi",
        Icons.Outlined.Receipt
    )

    object Produk : DashboardTab(
        "produk",
        "Produk",
        Icons.Outlined.Inventory2
    )

    object Kategori : DashboardTab(
        "kategori",
        "Kategori",
        Icons.Outlined.Label
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onLogout: () -> Unit
) {

    var selectedTab by remember {
        mutableStateOf<DashboardTab>(
            DashboardTab.Home
        )
    }

    val tabs = listOf(

        DashboardTab.Home,
        DashboardTab.Transaksi,
        DashboardTab.Produk,
        DashboardTab.Kategori

    )

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(

                        text = when (selectedTab) {

                            DashboardTab.Home ->
                                "Dashboard Cafe"

                            DashboardTab.Transaksi ->
                                "Transaksi Kasir"

                            DashboardTab.Produk ->
                                "Kelola Menu"

                            DashboardTab.Kategori ->
                                "Kategori Menu"

                        }

                    )

                }

            )

        },

        bottomBar = {

            NavigationBar {

                tabs.forEach { tab ->

                    NavigationBarItem(

                        selected =
                            selectedTab == tab,

                        onClick = {
                            selectedTab = tab
                        },

                        icon = {

                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )

                        },

                        label = {

                            Text(tab.label)

                        }

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

                DashboardTab.Home ->
                    HomePanel()

                DashboardTab.Transaksi ->
                    TransaksiPanel()

                DashboardTab.Produk ->
                    ProdukPanel()

                DashboardTab.Kategori ->
                    KategoriPanel()

            }

        }

    }

}