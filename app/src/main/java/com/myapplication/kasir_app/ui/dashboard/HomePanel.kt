package com.myapplication.kasir_app.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================
// DATA CLASS
// ==========================
data class StatItem(
    val label: String,
    val value: String,
    val sub: String,
    val icon: ImageVector,
    val isPositive: Boolean = true
)

// ==========================
// HOME PANEL
// ==========================
@Composable
fun HomePanel(

    totalTransaksi: Int,
    pendapatanHariIni: Int,
    menuTerlaris: String,
    jumlahMenuTerjual: Int,
    transaksiRealtime: List<String>

) {

    // ==========================
    // STATISTIK
    // ==========================
    val stats = listOf(

        StatItem(

            "Pendapatan Hari Ini",

            "Rp $pendapatanHariIni",

            "Realtime transaksi",

            Icons.Outlined.Payments

        ),

        StatItem(

            "Total Transaksi",

            "$totalTransaksi",

            "Realtime transaksi",

            Icons.Outlined.Receipt

        ),

        StatItem(

            "Menu Terlaris",

            menuTerlaris,

            "${jumlahMenuTerjual} terjual",

            Icons.Outlined.Coffee

        ),

        StatItem(

            "Total Produk",

            "16",

            "Menu tersedia",

            Icons.Outlined.Inventory2

        )

    )

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)

    ) {

        // ==========================
        // HEADER
        // ==========================
        Card(

            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.primary
            )

        ) {

            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)

            ) {

                Text(

                    text = "Dashboard Cafe",

                    color =
                        MaterialTheme
                            .colorScheme
                            .onPrimary,

                    fontSize = 24.sp,

                    fontWeight = FontWeight.Bold

                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(

                    text =
                        "Selamat datang di aplikasi kasir cafe",

                    color =
                        MaterialTheme
                            .colorScheme
                            .onPrimary

                )

            }

        }

        // ==========================
        // STAT ROW 1
        // ==========================
        Row(
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            stats.take(2).forEach { stat ->

                StatCard(

                    stat = stat,

                    modifier =
                        Modifier.weight(1f)

                )

            }

        }

        // ==========================
        // STAT ROW 2
        // ==========================
        Row(
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            stats.drop(2).forEach { stat ->

                StatCard(

                    stat = stat,

                    modifier =
                        Modifier.weight(1f)

                )

            }

        }

        // ==========================
        // TRANSAKSI TERBARU
        // ==========================
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(

                    text = "Transaksi Terbaru",

                    fontWeight = FontWeight.Bold,

                    fontSize = 18.sp

                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                if (transaksiRealtime.isEmpty()) {

                    Text(
                        text = "Belum ada transaksi"
                    )

                } else {

                    transaksiRealtime.forEachIndexed { index, tx ->

                        Row(

                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceBetween

                        ) {

                            Text(
                                "#00${index + 1}",
                                fontWeight = FontWeight.Bold
                            )

                            Text(tx)

                        }

                        HorizontalDivider(
                            modifier =
                                Modifier.padding(vertical = 8.dp)
                        )

                    }

                }

            }

        }

        // ==========================
        // MENU TERLARIS
        // ==========================
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(

                    text = "Menu Terlaris",

                    fontWeight = FontWeight.Bold,

                    fontSize = 18.sp

                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.SpaceBetween

                ) {

                    Column {

                        Text(

                            text = menuTerlaris,

                            fontWeight = FontWeight.Bold,

                            fontSize = 20.sp

                        )

                        Text(
                            text = "Menu Favorit"
                        )

                    }

                    Text(

                        text = "${jumlahMenuTerjual}x",

                        fontWeight = FontWeight.Bold,

                        color =
                            MaterialTheme
                                .colorScheme
                                .primary

                    )

                }

            }

        }

        Spacer(
            modifier = Modifier.height(100.dp)
        )

    }

}

// ==========================
// STAT CARD
// ==========================
@Composable
fun StatCard(
    stat: StatItem,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(

                    imageVector =
                        stat.icon,

                    contentDescription = null,

                    modifier = Modifier.size(18.dp)

                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(

                    text = stat.label,

                    fontSize = 11.sp

                )

            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(

                text = stat.value,

                fontWeight = FontWeight.Bold,

                fontSize = 24.sp

            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(

                text = stat.sub,

                fontSize = 11.sp,

                color =
                    MaterialTheme
                        .colorScheme
                        .primary

            )

        }

    }

}