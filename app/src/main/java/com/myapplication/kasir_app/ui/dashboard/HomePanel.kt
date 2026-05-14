package com.myapplication.kasir_app.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StatItem(
    val label: String,
    val value: String,
    val sub: String,
    val icon: ImageVector,
    val isPositive: Boolean = true
)

data class TransaksiItem(
    val id: String,
    val waktu: String,
    val total: String,
    val status: StatusTransaksi
)

data class TopProdukItem(
    val nama: String,
    val kategori: String,
    val terjual: Int,
    val maxTerjual: Int
)

enum class StatusTransaksi {
    LUNAS,
    PENDING,
    BATAL
}

@Composable
fun HomePanel() {

    // STATISTIK DASHBOARD
    val stats = listOf(

        StatItem(
            "Pendapatan Hari Ini",
            "Rp 2,4jt",
            "+12% vs kemarin",
            Icons.Outlined.Payments,
            true
        ),

        StatItem(
            "Total Transaksi",
            "38",
            "+5 transaksi",
            Icons.Outlined.Receipt,
            true
        ),

        StatItem(
            "Menu Kopi",
            "9",
            "+2 menu baru",
            Icons.Outlined.Coffee,
            true
        ),

        StatItem(
            "Cemilan",
            "4",
            "Best seller hari ini",
            Icons.Outlined.Fastfood,
            true
        )

    )

    // TRANSAKSI TERBARU
    val transaksiTerbaru = listOf(

        TransaksiItem(
            "#0038",
            "14:22",
            "Rp 47.500",
            StatusTransaksi.LUNAS
        ),

        TransaksiItem(
            "#0037",
            "14:05",
            "Rp 12.000",
            StatusTransaksi.LUNAS
        ),

        TransaksiItem(
            "#0036",
            "13:51",
            "Rp 98.000",
            StatusTransaksi.LUNAS
        ),

        TransaksiItem(
            "#0035",
            "13:30",
            "Rp 25.000",
            StatusTransaksi.PENDING
        ),

        TransaksiItem(
            "#0034",
            "13:10",
            "Rp 63.000",
            StatusTransaksi.BATAL
        )

    )

    // PRODUK TERLARIS
    val topProduk = listOf(

        TopProdukItem(
            "Americano",
            "Kopi",
            48,
            48
        ),

        TopProdukItem(
            "Cappuccino",
            "Kopi",
            37,
            48
        ),

        TopProdukItem(
            "Milk Tea",
            "Non Kopi",
            29,
            48
        ),

        TopProdukItem(
            "Oreo Lava Toast",
            "Cemilan",
            21,
            48
        )

    )

    // LAYOUT DASHBOARD
    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        // HEADER
        Card(

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    text = "Dashboard Cafe",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Selamat datang di aplikasi kasir cafe",
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }

        }

        // STAT CARD BARIS 1
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            stats.take(2).forEach { stat ->

                StatCard(
                    stat = stat,
                    modifier = Modifier.weight(1f)
                )

            }

        }

        // STAT CARD BARIS 2
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            stats.drop(2).forEach { stat ->

                StatCard(
                    stat = stat,
                    modifier = Modifier.weight(1f)
                )

            }

        }

        // TRANSAKSI TERBARU
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

                Spacer(modifier = Modifier.height(12.dp))

                transaksiTerbaru.forEach { tx ->

                    TransaksiRow(tx)

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                }

            }

        }

        // PRODUK TERLARIS
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

                Spacer(modifier = Modifier.height(12.dp))

                topProduk.forEach { produk ->

                    TopProdukRow(produk)

                    Spacer(modifier = Modifier.height(10.dp))

                }

            }

        }

        Spacer(modifier = Modifier.height(100.dp))

    }

}

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
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = stat.icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = stat.label,
                    fontSize = 11.sp
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stat.value,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stat.sub,
                fontSize = 11.sp,

                color =
                    if (stat.isPositive)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
            )

        }

    }

}

@Composable
fun TransaksiRow(
    tx: TransaksiItem
) {

    Row(

        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically

    ) {

        Text(
            text = tx.id,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = tx.waktu,
            fontSize = 12.sp
        )

        Text(
            text = tx.total
        )

        StatusBadge(tx.status)

    }

}

@Composable
fun StatusBadge(
    status: StatusTransaksi
) {

    val (text, containerColor, contentColor) = when (status) {

        StatusTransaksi.LUNAS -> Triple(
            "Lunas",
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary
        )

        StatusTransaksi.PENDING -> Triple(
            "Pending",
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.tertiary
        )

        StatusTransaksi.BATAL -> Triple(
            "Batal",
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.error
        )

    }

    Surface(
        color = containerColor,
        shape = MaterialTheme.shapes.small
    ) {

        Text(
            text = text,
            color = contentColor,
            fontSize = 10.sp,

            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 3.dp
            )

        )

    }

}

@Composable
fun TopProdukRow(
    produk: TopProdukItem
) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Column {

                Text(
                    text = produk.nama,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = produk.kategori,
                    fontSize = 11.sp
                )

            }

            Text(
                text = "${produk.terjual}x",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(

            progress = {
                produk.terjual.toFloat() /
                        produk.maxTerjual.toFloat()
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)

        )

    }

}