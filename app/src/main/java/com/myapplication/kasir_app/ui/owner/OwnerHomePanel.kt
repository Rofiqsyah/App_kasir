package com.myapplication.kasir_app.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun OwnerHomePanel() {
    val db = FirebaseFirestore.getInstance()
    
    // State untuk data Firestore
    var pendapatanHariIni by remember { mutableStateOf(0) }
    var totalTransaksi by remember { mutableStateOf(0) }
    var totalProdukTerjual by remember { mutableStateOf(0) }
    var transaksiTerbaru by remember { mutableStateOf(listOf<Triple<String, String, Int>>()) }
    var bestSellerList by remember { mutableStateOf(listOf<Triple<String, String, Int>>()) }

    LaunchedEffect(Unit) {
        db.collection("transactions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) return@addSnapshotListener

                val docs = snapshot.documents
                totalTransaksi = docs.size
                pendapatanHariIni = docs.sumOf { it.getLong("totalBayar")?.toInt() ?: 0 }
                totalProdukTerjual = docs.sumOf { it.getLong("qtyTerjual")?.toInt() ?: 0 }

                // Transaksi Terbaru
                transaksiTerbaru = docs.take(3).map { 
                    Triple(
                        "TX-${it.id.takeLast(4).uppercase()}", 
                        "Hari ini", // Bisa diformat dari timestamp jika perlu
                        it.getLong("totalBayar")?.toInt() ?: 0
                    )
                }

                // Menu Terlaris
                val counts = mutableMapOf<String, Int>()
                docs.forEach { doc ->
                    val nama = doc.getString("menuTerlaris") ?: ""
                    val qty = doc.getLong("qtyTerjual")?.toInt() ?: 0
                    if (nama.isNotEmpty() && nama != "-") {
                        counts[nama] = (counts[nama] ?: 0) + qty
                    }
                }
                bestSellerList = counts.map { 
                    Triple(it.key, "Menu Favorit", it.value) 
                }.sortedByDescending { it.third }.take(3)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. HEADER CARD
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
                    text = "Monitoring bisnis Anda secara realtime",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 14.sp
                )
            }
        }

        // 2. STAT CARDS
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OwnerStatCard(
                label = "Pendapatan",
                value = "Rp $pendapatanHariIni",
                sub = "Total hari ini",
                icon = Icons.Outlined.Payments,
                modifier = Modifier.weight(1f)
            )
            OwnerStatCard(
                label = "Transaksi",
                value = "$totalTransaksi",
                sub = "Transaksi sukses",
                icon = Icons.Outlined.Receipt,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OwnerStatCard(
                label = "Produk Terjual",
                value = "$totalProdukTerjual",
                sub = "Item keluar",
                icon = Icons.Outlined.Inventory2,
                modifier = Modifier.weight(1f)
            )
            OwnerStatCard(
                label = "Kasir Aktif",
                value = "1", // Bisa diupdate jika ada data session
                sub = "Standby",
                icon = Icons.Outlined.PeopleAlt,
                modifier = Modifier.weight(1f)
            )
        }

        // 3. TRANSAKSI TERBARU
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Transaksi Terbaru", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                if (transaksiTerbaru.isEmpty()) {
                    Text("Belum ada transaksi", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    transaksiTerbaru.forEach { (id, waktu, total) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(id, fontWeight = FontWeight.Bold)
                            Text(waktu, fontSize = 12.sp)
                            Text("Rp $total")
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
        }

        // 4. MENU TERLARIS
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Menu Terlaris", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                if (bestSellerList.isEmpty()) {
                    Text("Belum ada data", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    val maxVal = bestSellerList.maxOf { it.third }.toFloat()
                    bestSellerList.forEach { (nama, kategori, qty) ->
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
                                progress = { qty.toFloat() / maxVal },
                                modifier = Modifier.fillMaxWidth().height(4.dp),
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
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
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = sub, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}