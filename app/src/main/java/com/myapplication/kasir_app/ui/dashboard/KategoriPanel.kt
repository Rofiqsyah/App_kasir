package com.myapplication.kasir_app.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Kategori(
    val id: String,
    val nama: String,
    val jumlahProduk: Int,
    val icon: ImageVector,
    val warna: Color
)

@Composable
fun KategoriPanel() {

    var showDialog by remember {
        mutableStateOf(false)
    }

    var kategoriDiedit by remember {
        mutableStateOf<Kategori?>(null)
    }

    // POPUP HAPUS
    var kategoriAkanDihapus by remember {
        mutableStateOf<Kategori?>(null)
    }

    val kategoriList = remember {

        mutableStateListOf(

            Kategori(
                id = "1",
                nama = "Kopi",
                jumlahProduk = 9,
                icon = Icons.Outlined.Coffee,
                warna = Color(0xFF6F4E37)
            ),

            Kategori(
                id = "2",
                nama = "Non Kopi",
                jumlahProduk = 3,
                icon = Icons.Outlined.LocalDrink,
                warna = Color(0xFF185FA5)
            ),

            Kategori(
                id = "3",
                nama = "Cemilan",
                jumlahProduk = 4,
                icon = Icons.Outlined.Fastfood,
                warna = Color(0xFFB26A00)
            )

        )

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "${kategoriList.size} kategori aktif",

                fontSize = 13.sp,

                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = {

                    kategoriDiedit = null
                    showDialog = true

                }
            ) {

                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.width(4.dp)
                )

                Text("Kategori Baru")

            }

        }

        // LIST KATEGORI
        LazyColumn(

            modifier = Modifier.fillMaxSize(),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)

        ) {

            items(kategoriList) { kategori ->

                KategoriRow(

                    kategori = kategori,

                    onEdit = {

                        kategoriDiedit = it
                        showDialog = true

                    },

                    onDelete = {

                        kategoriAkanDihapus = it

                    }

                )

            }

            item {

                Spacer(
                    modifier = Modifier.height(100.dp)
                )

            }

        }

    }

    // DIALOG TAMBAH / EDIT
    if (showDialog) {

        KategoriDialog(

            kategori = kategoriDiedit,

            onDismiss = {
                showDialog = false
            },

            onSave = { nama ->

                val existing = kategoriDiedit

                if (existing != null) {

                    val idx =
                        kategoriList.indexOfFirst {
                            it.id == existing.id
                        }

                    if (idx >= 0) {

                        kategoriList[idx] =
                            existing.copy(
                                nama = nama
                            )

                    }

                } else {

                    kategoriList.add(

                        Kategori(
                            id = (kategoriList.size + 1).toString(),
                            nama = nama,
                            jumlahProduk = 0,
                            icon = Icons.Outlined.Category,
                            warna = Color(0xFF6F4E37)
                        )

                    )

                }

                showDialog = false

            }

        )

    }

    // DIALOG HAPUS
    if (kategoriAkanDihapus != null) {

        AlertDialog(

            onDismissRequest = {
                kategoriAkanDihapus = null
            },

            title = {

                Text(
                    text = "Hapus Kategori"
                )

            },

            text = {

                Text(
                    text =
                        "Apakah anda yakin akan menghapus kategori ini?"
                )

            },

            confirmButton = {

                Button(

                    onClick = {

                        kategoriList.remove(kategoriAkanDihapus)
                        kategoriAkanDihapus = null

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
                        kategoriAkanDihapus = null
                    }

                ) {

                    Text("Batal")

                }

            }

        )

    }

}

@Composable
fun KategoriRow(
    kategori: Kategori,
    onEdit: (Kategori) -> Unit,
    onDelete: (Kategori) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Surface(

                    modifier = Modifier.size(45.dp),

                    shape =
                        MaterialTheme.shapes.medium,

                    color =
                        kategori.warna.copy(alpha = 0.12f)

                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = kategori.icon,

                            contentDescription = null,

                            modifier = Modifier.size(22.dp),

                            tint = kategori.warna
                        )

                    }

                }

                Column {

                    Text(
                        text = kategori.nama,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Text(
                        text = "${kategori.jumlahProduk} produk",

                        fontSize = 12.sp,

                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }

            }

            Row {

                IconButton(
                    onClick = {
                        onEdit(kategori)
                    }
                ) {

                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "Edit"
                    )

                }

                IconButton(
                    onClick = {
                        onDelete(kategori)
                    }
                ) {

                    Icon(
                        Icons.Outlined.Delete,

                        contentDescription = "Hapus",

                        tint =
                            MaterialTheme.colorScheme.error
                    )

                }

            }

        }

    }

}

@Composable
fun KategoriDialog(
    kategori: Kategori?,
    onDismiss: () -> Unit,
    onSave: (nama: String) -> Unit
) {

    var nama by remember {
        mutableStateOf(kategori?.nama ?: "")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                if (kategori == null)
                    "Tambah Kategori"
                else
                    "Edit Kategori"
            )

        },

        text = {

            OutlinedTextField(

                value = nama,

                onValueChange = {
                    nama = it
                },

                label = {
                    Text("Nama Kategori")
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true

            )

        },

        confirmButton = {

            Button(

                onClick = {

                    if (nama.isNotBlank()) {
                        onSave(nama)
                    }

                },

                enabled = nama.isNotBlank()

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