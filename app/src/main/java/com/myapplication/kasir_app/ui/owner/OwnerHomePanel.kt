package com.myapplication.kasir_app.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OwnerHomePanel() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. HEADER CARD (Sama dengan Kasir)
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Dashboard Owner",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Selamat datang kembali, Owner!",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp
                )
            }
        }

        // 2. STAT CARDS (4 Kotak)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OwnerStatCard(
                label = "Pendapatan Hari Ini",
                value = "Rp 3,8jt",
                sub = "+12% vs kemarin",
                icon = Icons.Outlined.Payments,
                modifier = Modifier.weight(1f)
            )
            OwnerStatCard(
                label = "Total Transaksi",
                value = "52",
                sub = "+5 transaksi",
                icon = Icons.Outlined.Receipt,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OwnerStatCard(
                label = "Produk Terjual",
                value = "137",
                sub = "Peningkatan stok",
                icon = Icons.Outlined.Inventory2,
                modifier = Modifier.weight(1f)
            )
            OwnerStatCard(
                label = "Kasir Aktif",
                value = "2",
                sub = "Sesi sedang jalan",
                icon = Icons.Outlined.PeopleAlt,
                modifier = Modifier.weight(1f)
            )
        }

        // 3. TRANSAKSI TERBARU
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Transaksi Terbaru", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                listOf(
                    Triple("#0052", "16:45", "Rp 75.000"),
                    Triple("#0051", "16:30", "Rp 42.500"),
                    Triple("#0050", "16:10", "Rp 120.000")
                ).forEach { (id, waktu, total) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(id, fontWeight = FontWeight.Bold)
                        Text(waktu, fontSize = 12.sp)
                        Text(total)
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                "Lunas",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)
                }
            }
        }

        // 4. MENU TERLARIS (Dengan Progress Bar)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Menu Terlaris", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                listOf(
                    Triple("Americano", "Kopi", 48),
                    Triple("Cappuccino", "Kopi", 37),
                    Triple("Milk Tea", "Non Kopi", 29)
                ).forEach { (nama, kategori, qty) ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(nama, fontWeight = FontWeight.Bold)
                                Text(kategori, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text("$qty" + "x", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { qty.toFloat() / 50f },
                            modifier = Modifier.fillMaxWidth().height(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun OwnerStatCard(label: String, value: String, sub: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = label, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = sub, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}
