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
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanPanel() {
    var selectedFilter by remember { mutableStateOf("Harian") }
    var showDatePicker by remember { mutableStateOf(false) }
    var customDateRange by remember { mutableStateOf("Pilih Tanggal") }
    
    // Data Statistik Dinamis
    val summaryData = remember(selectedFilter) {
        when(selectedFilter) {
            "Harian" -> Pair("Rp 1.250.000", "15")
            "Mingguan" -> Pair("Rp 8.450.000", "112")
            "Bulanan" -> Pair("Rp 32.150.000", "456")
            else -> Pair("Rp 4.200.000", "38")
        }
    }

    // Data Produk Terlaris Dinamis (Samakan dengan Dashboard)
    val bestSeller = remember(selectedFilter) {
        when(selectedFilter) {
            "Harian" -> listOf(
                Triple("Americano", "Kopi", 12),
                Triple("Es Teh Manis", "Non Kopi", 20)
            )
            "Mingguan" -> listOf(
                Triple("Americano", "Kopi", 48),
                Triple("Cappuccino", "Kopi", 37),
                Triple("Milk Tea", "Non Kopi", 29)
            )
            "Bulanan" -> listOf(
                Triple("Americano", "Kopi", 185),
                Triple("Cappuccino", "Kopi", 142),
                Triple("Milk Tea", "Non Kopi", 115),
                Triple("Ayam Bakar", "Makanan", 98)
            )
            else -> listOf(
                Triple("Americano", "Kopi", 25),
                Triple("Milk Tea", "Non Kopi", 18)
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { 
                    customDateRange = "10 Mei 2026 - 19 Mei 2026"
                    selectedFilter = "Custom"
                    showDatePicker = false 
                }) { Text("Pilih") }
            }
        ) {
            DatePicker(state = datePickerState)
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
                    selected = selectedFilter == filter || (selectedFilter == "Mingguan" && filter == "Minggu") || (selectedFilter == "Bulanan" && filter == "Bulan"),
                    onClick = { 
                        if (filter == "Custom") {
                            showDatePicker = true 
                        } else {
                            selectedFilter = when(filter) {
                                "Minggu" -> "Mingguan"
                                "Bulan" -> "Bulanan"
                                else -> filter
                            }
                        }
                    },
                    label = { 
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            maxLines = 1
                        ) 
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (selectedFilter == "Custom") {
            Text("Rentang: $customDateRange", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }

        Text("Ringkasan $selectedFilter", fontWeight = FontWeight.Bold)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryItem("Total Penjualan", summaryData.first, Icons.Outlined.Payments, Modifier.weight(1f))
            SummaryItem("Transaksi", summaryData.second, Icons.Outlined.ReceiptLong, Modifier.weight(1f))
        }

        // BAGIAN PRODUK TERLARIS - DISAMAKAN DENGAN DASHBOARD
        Text("Produk Terlaris", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                val maxVal = if (bestSeller.isNotEmpty()) bestSeller.maxOf { it.third }.toFloat() else 1f
                
                bestSeller.forEach { (nama, kategori, qty) ->
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
