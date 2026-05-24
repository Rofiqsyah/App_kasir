package com.myapplication.kasir_app.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

object DashboardData {
    var totalTransaksi by mutableStateOf(0)
    var pendapatanHariIni by mutableStateOf(0)
    var menuTerlaris by mutableStateOf("Belum Ada")
    var jumlahMenuTerjual by mutableStateOf(0)
    var transaksiTerbaru by mutableStateOf(listOf<String>())
}

sealed class DashboardTab(val route: String, val label: String, val icon: ImageVector) {
    object Home : DashboardTab("home", "Dashboard", Icons.Outlined.Home)
    object Transaksi : DashboardTab("transaksi", "Transaksi", Icons.Outlined.Receipt)
    object Produk : DashboardTab("produk", "Produk", Icons.Outlined.Inventory2)
    object Kategori : DashboardTab("kategori", "Kategori", Icons.Outlined.Label)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(onLogout: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf<DashboardTab>(DashboardTab.Home) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val tabs = listOf(DashboardTab.Home, DashboardTab.Transaksi, DashboardTab.Produk, DashboardTab.Kategori)

    // Listener Realtime
    LaunchedEffect(Unit) {
        seedKasirMasterData(
            db = db,
            onSuccess = {
                Toast.makeText(context, "Kategori dan produk tersinkron ke Firestore", Toast.LENGTH_SHORT).show()
            },
            onFailure = { err ->
                Toast.makeText(context, "Gagal sinkron produk: ${err.localizedMessage}", Toast.LENGTH_LONG).show()
                android.util.Log.e("Firestore", "Gagal seed kategori/produk: ${err.message}", err)
            }
        )

        db.collection("transactions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    android.util.Log.e("Firestore", "Gagal load data: ${e.message}")
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val docs = snapshot.documents
                    DashboardData.totalTransaksi = docs.size
                    DashboardData.pendapatanHariIni = docs.sumOf { it.getLong("totalBayar")?.toInt() ?: 0 }
                    DashboardData.transaksiTerbaru = docs.take(5).map { "Rp ${it.getLong("totalBayar")}" }

                    val counts = mutableMapOf<String, Int>()
                    docs.forEach { doc ->
                        val menu = doc.getString("menuTerlaris") ?: ""
                        val qty = doc.getLong("qtyTerjual")?.toInt() ?: 0
                        if (menu.isNotEmpty() && menu != "-") {
                            counts[menu] = (counts[menu] ?: 0) + qty
                        }
                    }
                    val top = counts.maxByOrNull { it.value }
                    DashboardData.menuTerlaris = top?.key ?: "Belum Ada"
                    DashboardData.jumlahMenuTerjual = top?.value ?: 0
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = selectedTab.label) },
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
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                DashboardTab.Home -> HomePanel(
                    DashboardData.totalTransaksi, DashboardData.pendapatanHariIni,
                    DashboardData.menuTerlaris, DashboardData.jumlahMenuTerjual, DashboardData.transaksiTerbaru
                )
                DashboardTab.Transaksi -> TransaksiPanel(
                    onTransaksiSelesai = { total, menu, qty ->
                        val data = hashMapOf(
                            "totalBayar" to total,
                            "menuTerlaris" to menu,
                            "qtyTerjual" to qty,
                            "timestamp" to com.google.firebase.Timestamp.now()
                        )
                        
                        db.collection("transactions")
                            .add(data)
                            .addOnSuccessListener {
                                Toast.makeText(context, "✅ Transaksi Berhasil Disimpan!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { err ->
                                Toast.makeText(context, "❌ Gagal Simpan: ${err.localizedMessage}", Toast.LENGTH_LONG).show()
                                android.util.Log.e("Firestore", "Error Write: ", err)
                            }
                    }
                )
                DashboardTab.Produk -> ProdukPanel()
                DashboardTab.Kategori -> KategoriPanel()
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Yakin ingin keluar?") },
            confirmButton = {
                TextButton(onClick = { onLogout() }) { Text("Keluar", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Batal") } }
        )
    }
}
