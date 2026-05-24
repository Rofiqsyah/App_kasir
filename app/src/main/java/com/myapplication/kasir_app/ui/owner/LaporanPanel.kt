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
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanPanel() {
    val db = FirebaseFirestore.getInstance()
    var selectedFilter by remember { mutableStateOf("Harian") }
    
    // State untuk data dari Firestore
    var totalPenjualan by remember { mutableStateOf(0) }
    var totalTransaksi by remember { mutableStateOf(0) }
    var bestSellerList by remember { mutableStateOf(listOf<Triple<String, String, Int>>()) }
    var isLoading by remember { mutableStateOf(true) }

    // Ambil data dari Firestore secara Realtime
    LaunchedEffect(selectedFilter) {
        isLoading = true
        db.collection("transactions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    isLoading = false
                    return@addSnapshotListener
                }

                val docs = snapshot.documents
                totalTransaksi = docs.size
                totalPenjualan = docs.sumOf { it.getLong("totalBayar")?.toInt() ?: 0 }

                // Hitung Produk Terlaris
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
                }.sortedByDescending { it.third }
                
                isLoading = false
            }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Filter Periode", fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("Harian", "Minggu", "Bulan", "Custom").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter, fontSize = 12.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Text("Ringkasan $selectedFilter", fontWeight = FontWeight.Bold)
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryItem("Total Penjualan", "Rp $totalPenjualan", Icons.Outlined.Payments, Modifier.weight(1f))
                SummaryItem("Transaksi", "$totalTransaksi", Icons.Outlined.ReceiptLong, Modifier.weight(1f))
            }
        }

        Text("Produk Terlaris", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (bestSellerList.isEmpty() && !isLoading) {
                    Text(
                        "Belum ada data transaksi.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    val maxVal = if(bestSellerList.isNotEmpty()) bestSellerList.maxOf { it.third }.toFloat() else 1f
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
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SummaryItem(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(label, fontSize = 11.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}