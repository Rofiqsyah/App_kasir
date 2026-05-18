package com.myapplication.kasir_app.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Produk(
    val id: String,
    val nama: String,
    val kategori: String,
    val harga: String,
    val stok: Int,
    val stokMinimum: Int = 5
)

@Composable
fun ProdukPanel() {

    var searchQuery by remember {
        mutableStateOf("")
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var produkDiedit by remember {
        mutableStateOf<Produk?>(null)
    }

    // POPUP HAPUS
    var produkAkanDihapus by remember {
        mutableStateOf<Produk?>(null)
    }

    val produkList = remember {

        mutableStateListOf(

            // MENU KOPI
            Produk("1", "Americano", "Kopi", "Rp 10.000", 20),
            Produk("2", "Espresso", "Kopi", "Rp 10.000", 15),
            Produk("3", "Cappuccino", "Kopi", "Rp 15.000", 18),
            Produk("4", "Coffee Latte", "Kopi", "Rp 15.000", 12),
            Produk("5", "Caramel Macchiato", "Kopi", "Rp 18.000", 10),
            Produk("6", "Es Kopi Susu", "Kopi", "Rp 18.000", 25),
            Produk("7", "Kopi Susu Gula Aren", "Kopi", "Rp 18.000", 30),
            Produk("8", "Majapahit Black Coffee", "Kopi", "Rp 5.000", 40),
            Produk("9", "Majapahit Milk Coffee", "Kopi", "Rp 6.000", 35),

            // NON KOPI
            Produk("10", "Coklat", "Non Kopi", "Rp 10.000", 12),
            Produk("11", "Milk Tea", "Non Kopi", "Rp 10.000", 20),
            Produk("12", "Lemon Tea", "Non Kopi", "Rp 6.000", 8),

            // CEMILAN
            Produk("13", "Oreo Lava Toast", "Cemilan", "Rp 10.000", 15),
            Produk("14", "Lotus Biscoff Lava Toast", "Cemilan", "Rp 12.000", 10),
            Produk("15", "Chocolate Lava Toast", "Cemilan", "Rp 10.000", 9),
            Produk("16", "Cheese Chocolate Lava Toast", "Cemilan", "Rp 15.000", 7)

        )

    }

    val filtered = produkList.filter {

        it.nama.contains(searchQuery, ignoreCase = true) ||
                it.kategori.contains(searchQuery, ignoreCase = true)

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Kelola Produk",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(8.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            OutlinedTextField(

                value = searchQuery,

                onValueChange = {
                    searchQuery = it
                },

                placeholder = {
                    Text("Cari menu...")
                },

                leadingIcon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = null
                    )
                },

                modifier = Modifier.weight(1f),

                singleLine = true

            )

            Button(
                onClick = {

                    produkDiedit = null
                    showDialog = true

                }
            ) {

                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text("Tambah")

            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(

            columns = GridCells.Fixed(2),

            modifier = Modifier.fillMaxSize(),

            horizontalArrangement =
                Arrangement.spacedBy(10.dp),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)

        ) {

            items(filtered) { produk ->

                ProdukCard(

                    produk = produk,

                    onEdit = {

                        produkDiedit = it
                        showDialog = true

                    },

                    onDelete = {

                        produkAkanDihapus = it

                    }

                )

            }

        }

    }

    // DIALOG TAMBAH / EDIT
    if (showDialog) {

        ProdukDialog(

            produk = produkDiedit,

            onDismiss = {
                showDialog = false
            },

            onSave = { nama, kategori, harga, stok ->

                val existing = produkDiedit

                if (existing != null) {

                    val idx =
                        produkList.indexOfFirst {
                            it.id == existing.id
                        }

                    if (idx >= 0) {

                        produkList[idx] = existing.copy(
                            nama = nama,
                            kategori = kategori,
                            harga = harga,
                            stok = stok
                        )

                    }

                } else {

                    produkList.add(

                        Produk(
                            id = (produkList.size + 1).toString(),
                            nama = nama,
                            kategori = kategori,
                            harga = harga,
                            stok = stok
                        )

                    )

                }

                showDialog = false

            }

        )

    }

    // DIALOG HAPUS
    if (produkAkanDihapus != null) {

        AlertDialog(

            onDismissRequest = {
                produkAkanDihapus = null
            },

            title = {

                Text(
                    text = "Hapus Produk"
                )

            },

            text = {

                Text(
                    text =
                        "Apakah anda yakin akan menghapus produk ini?"
                )

            },

            confirmButton = {

                Button(

                    onClick = {

                        produkList.remove(produkAkanDihapus)
                        produkAkanDihapus = null

                    },

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                MaterialTheme.colorScheme.error
                        )

                ) {

                    Text("Hapus")

                }

            },

            dismissButton = {

                TextButton(

                    onClick = {
                        produkAkanDihapus = null
                    }

                ) {

                    Text("Batal")

                }

            }

        )

    }

}

@Composable
fun ProdukCard(
    produk: Produk,
    onEdit: (Produk) -> Unit,
    onDelete: (Produk) -> Unit
) {

    val isLowStock =
        produk.stok <= produk.stokMinimum

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Surface(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),

                color =
                    MaterialTheme.colorScheme.surfaceVariant,

                shape =
                    MaterialTheme.shapes.medium

            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Icon(

                        imageVector = when (produk.kategori) {

                            "Kopi" ->
                                Icons.Outlined.Coffee

                            "Non Kopi" ->
                                Icons.Outlined.LocalDrink

                            else ->
                                Icons.Outlined.Fastfood

                        },

                        contentDescription = null,

                        modifier = Modifier.size(30.dp)

                    )

                }

            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = produk.nama,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            Text(
                text = produk.kategori,
                fontSize = 11.sp,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = produk.harga,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (isLowStock) {

                    Icon(
                        Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                }

                Text(
                    text = "Stok: ${produk.stok}",
                    fontSize = 11.sp,

                    color =
                        if (isLowStock)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                OutlinedButton(

                    onClick = {
                        onEdit(produk)
                    },

                    modifier = Modifier.weight(1f),

                    contentPadding =
                        PaddingValues(vertical = 4.dp)

                ) {

                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Edit",
                        fontSize = 11.sp
                    )

                }

                OutlinedButton(

                    onClick = {
                        onDelete(produk)
                    },

                    modifier = Modifier.weight(1f),

                    contentPadding =
                        PaddingValues(vertical = 4.dp),

                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor =
                                MaterialTheme.colorScheme.error
                        )

                ) {

                    Icon(
                        Icons.Outlined.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Hapus",
                        fontSize = 11.sp
                    )

                }

            }

        }

    }

}

@Composable
fun ProdukDialog(
    produk: Produk?,
    onDismiss: () -> Unit,
    onSave: (
        nama: String,
        kategori: String,
        harga: String,
        stok: Int
    ) -> Unit
) {

    var nama by remember {
        mutableStateOf(produk?.nama ?: "")
    }

    var kategori by remember {
        mutableStateOf(produk?.kategori ?: "")
    }

    var harga by remember {
        mutableStateOf(produk?.harga ?: "")
    }

    var stok by remember {
        mutableStateOf(produk?.stok?.toString() ?: "")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                if (produk == null)
                    "Tambah Produk"
                else
                    "Edit Produk"
            )

        },

        text = {

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Produk") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = kategori,
                    onValueChange = { kategori = it },
                    label = { Text("Kategori") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = harga,
                    onValueChange = { harga = it },
                    label = { Text("Harga") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = stok,
                    onValueChange = { stok = it },
                    label = { Text("Stok") },
                    modifier = Modifier.fillMaxWidth()
                )

            }

        },

        confirmButton = {

            Button(

                onClick = {

                    if (
                        nama.isNotBlank() &&
                        harga.isNotBlank()
                    ) {

                        onSave(
                            nama,
                            kategori,
                            harga,
                            stok.toIntOrNull() ?: 0
                        )

                    }

                }

            ) {

                Text("Simpan")

            }

        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Batal")

            }

        }

    )

}