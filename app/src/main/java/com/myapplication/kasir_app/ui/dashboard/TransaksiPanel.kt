package com.myapplication.kasir_app.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapplication.kasir_app.R

data class ProdukKasir(
    val nama: String,
    val harga: Int,
    val gambar: Int
)

data class KeranjangItem(
    val nama: String,
    val harga: Int,
    val qty: Int
)

@Composable
fun TransaksiPanel(

    onTransaksiSelesai: (
        totalBayar: Int,
        menuTerlaris: String,
        qtyTerjual: Int
    ) -> Unit

) {

    // ==========================
    // DAFTAR MENU
    // ==========================
    val daftarProduk = listOf(

        // KOPI
        ProdukKasir(
            "Americano",
            10000,
            R.drawable.americano
        ),

        ProdukKasir(
            "Espresso",
            10000,
            R.drawable.espresso
        ),

        ProdukKasir(
            "Cappuccino",
            15000,
            R.drawable.cappucino
        ),

        ProdukKasir(
            "Coffee Latte",
            15000,
            R.drawable.coffee_latte
        ),

        ProdukKasir(
            "Caramel Macchiato",
            18000,
            R.drawable.caramel_macciato
        ),

        ProdukKasir(
            "Es Kopi Susu",
            18000,
            R.drawable.es_kopisusu
        ),

        // NON KOPI
        ProdukKasir(
            "Milk Tea",
            10000,
            R.drawable.milk_tea
        ),

        ProdukKasir(
            "Lemon Tea",
            6000,
            R.drawable.lemon_tea
        ),

        ProdukKasir(
            "Chocolate",
            10000,
            R.drawable.chocolate
        ),

        // CEMILAN
        ProdukKasir(
            "Oreo Lava Toast",
            10000,
            R.drawable.orea_lavatoast
        ),

        ProdukKasir(
            "Chocolate Lava Toast",
            10000,
            R.drawable.chocolate_lavatoast
        ),

        ProdukKasir(
            "Cheese Chocolate Lava Toast",
            15000,
            R.drawable.cheese_chocolate_lavatoast
        )

    )

    // ==========================
    // KERANJANG
    // ==========================
    val keranjang = remember {
        mutableStateListOf<KeranjangItem>()
    }

    // ==========================
    // DIALOG
    // ==========================
    var showPembayaran by remember {
        mutableStateOf(false)
    }

    var showStruk by remember {
        mutableStateOf(false)
    }

    // ==========================
    // NOMINAL BAYAR
    // ==========================
    var uangBayar by remember {
        mutableStateOf("")
    }

    // ==========================
    // TOTAL
    // ==========================
    val totalHarga = keranjang.sumOf {
        it.harga * it.qty
    }

    // ==========================
    // CONTENT
    // ==========================
    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)

    ) {

        // ==========================
        // JUDUL
        // ==========================
        item {

            Text(

                text = "Transaksi Kasir",

                fontSize = 24.sp,

                fontWeight = FontWeight.Bold

            )

        }

        // ==========================
        // DAFTAR MENU
        // ==========================
        item {

            Text(

                text = "Daftar Menu",

                fontWeight = FontWeight.Bold

            )

        }

        // ==========================
        // LIST PRODUK
        // ==========================
        itemsIndexed(daftarProduk) { _, produk ->

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically

                ) {

                    // KIRI
                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        // GAMBAR
                        Image(

                            painter =
                                painterResource(
                                    id = produk.gambar
                                ),

                            contentDescription =
                                produk.nama,

                            modifier = Modifier
                                .size(90.dp)
                                .clip(
                                    RoundedCornerShape(12.dp)
                                ),

                            contentScale =
                                ContentScale.Crop

                        )

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        // TEXT
                        Column {

                            Text(

                                text = produk.nama,

                                fontWeight =
                                    FontWeight.Bold

                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Rp ${produk.harga}"
                            )

                        }

                    }

                    // BUTTON TAMBAH
                    IconButton(

                        onClick = {

                            val existingIndex =
                                keranjang.indexOfFirst {
                                    it.nama == produk.nama
                                }

                            if (existingIndex >= 0) {

                                val existing =
                                    keranjang[existingIndex]

                                keranjang[existingIndex] =
                                    existing.copy(
                                        qty = existing.qty + 1
                                    )

                            } else {

                                keranjang.add(

                                    KeranjangItem(
                                        produk.nama,
                                        produk.harga,
                                        1
                                    )

                                )

                            }

                        }

                    ) {

                        Icon(

                            Icons.Outlined.AddShoppingCart,

                            contentDescription = null

                        )

                    }

                }

            }

        }

        // ==========================
        // JUDUL KERANJANG
        // ==========================
        item {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(

                text = "Keranjang",

                fontWeight = FontWeight.Bold,

                fontSize = 18.sp

            )

        }

        // ==========================
        // JIKA KOSONG
        // ==========================
        if (keranjang.isEmpty()) {

            item {

                Text(
                    text = "Belum ada menu dipilih"
                )

            }

        } else {

            // ==========================
            // LIST KERANJANG
            // ==========================
            itemsIndexed(keranjang) { index, item ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {

                        Column {

                            Text(

                                text = item.nama,

                                fontWeight =
                                    FontWeight.Bold

                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Rp ${item.harga}"
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(

                                text =
                                    "Subtotal: Rp ${item.harga * item.qty}",

                                color =
                                    MaterialTheme
                                        .colorScheme
                                        .primary

                            )

                        }

                        // QTY
                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            // MINUS
                            IconButton(

                                onClick = {

                                    if (item.qty > 1) {

                                        keranjang[index] =
                                            item.copy(
                                                qty = item.qty - 1
                                            )

                                    } else {

                                        keranjang.removeAt(index)

                                    }

                                }

                            ) {

                                Icon(

                                    Icons.Outlined.Remove,

                                    contentDescription = null

                                )

                            }

                            Text(

                                text = item.qty.toString(),

                                fontWeight =
                                    FontWeight.Bold,

                                fontSize = 18.sp

                            )

                            // PLUS
                            IconButton(

                                onClick = {

                                    keranjang[index] =
                                        item.copy(
                                            qty = item.qty + 1
                                        )

                                }

                            ) {

                                Icon(

                                    Icons.Outlined.Add,

                                    contentDescription = null

                                )

                            }

                        }

                    }

                }

            }

        }

        // ==========================
        // TOTAL
        // ==========================
        item {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),

                    horizontalArrangement =
                        Arrangement.SpaceBetween

                ) {

                    Text(

                        text = "Total",

                        fontWeight =
                            FontWeight.Bold,

                        fontSize = 18.sp

                    )

                    Text(

                        text = "Rp $totalHarga",

                        fontWeight =
                            FontWeight.Bold,

                        fontSize = 18.sp,

                        color =
                            MaterialTheme
                                .colorScheme
                                .primary

                    )

                }

            }

        }

        // ==========================
        // BUTTON BAYAR
        // ==========================
        item {

            Button(

                onClick = {

                    showPembayaran = true

                },

                modifier = Modifier.fillMaxWidth(),

                enabled = keranjang.isNotEmpty()

            ) {

                Icon(

                    Icons.Outlined.Receipt,

                    contentDescription = null

                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "Lanjut Pembayaran"
                )

            }

        }

        item {

            Spacer(
                modifier = Modifier.height(100.dp)
            )

        }

    }

    // ==========================
    // DIALOG PEMBAYARAN
    // ==========================
    if (showPembayaran) {

        AlertDialog(

            onDismissRequest = {

                showPembayaran = false

            },

            title = {

                Text("Pembayaran")

            },

            text = {

                Column {

                    Text(

                        text =
                            "Total Bayar: Rp $totalHarga",

                        fontWeight =
                            FontWeight.Bold

                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedTextField(

                        value = uangBayar,

                        onValueChange = { input ->

                            val cleanInput =

                                input
                                    .replace(".", "")
                                    .filter {
                                        it.isDigit()
                                    }

                            uangBayar =

                                if (cleanInput.isEmpty()) {

                                    ""

                                } else {

                                    cleanInput
                                        .reversed()
                                        .chunked(3)
                                        .joinToString(".")
                                        .reversed()

                                }

                        },

                        label = {

                            Text("Nominal Uang")

                        },

                        prefix = {

                            Text("Rp ")

                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        singleLine = true

                    )

                }

            },

            confirmButton = {

                Button(

                    onClick = {
                        // ==========================
                        // MENU TERLARIS
                        // ==========================
                        val menuTerlarisItem =
                            keranjang.maxByOrNull {
                                it.qty
                            }

                        // ==========================
                        // SIMPAN TRANSAKSI
                        // ==========================
                        onTransaksiSelesai(

                            totalHarga,

                            menuTerlarisItem?.nama ?: "-",

                            menuTerlarisItem?.qty ?: 0

                        )

                        showPembayaran = false
                        showStruk = true

                    },

                    enabled =

                        (
                                uangBayar
                                    .replace(".", "")
                                    .toIntOrNull() ?: 0
                                ) >= totalHarga

                ) {

                    Text("Bayar")

                }

            },

            dismissButton = {

                TextButton(

                    onClick = {

                        showPembayaran = false

                    }

                ) {

                    Text("Batal")

                }

            }

        )

    }

    // ==========================
    // STRUK
    // ==========================
    if (showStruk) {

        val bayar =

            uangBayar
                .replace(".", "")
                .toIntOrNull() ?: 0

        val kembalian =
            bayar - totalHarga

        AlertDialog(

            onDismissRequest = {

                showStruk = false

            },

            title = {

                Text("Struk Pembayaran")

            },

            text = {

                Column {

                    Text("====================")

                    keranjang.forEach {

                        Row(

                            modifier =
                                Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceBetween

                        ) {

                            Text(
                                "${it.nama} x${it.qty}"
                            )

                            Text(
                                "Rp ${it.harga * it.qty}"
                            )

                        }

                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text("====================")

                    // TOTAL
                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween

                    ) {

                        Text(

                            "TOTAL",

                            fontWeight =
                                FontWeight.Bold

                        )

                        Text(

                            "Rp $totalHarga",

                            fontWeight =
                                FontWeight.Bold

                        )

                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    // BAYAR
                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween

                    ) {

                        Text("Bayar")

                        Text("Rp $bayar")

                    }

                    // KEMBALIAN
                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween

                    ) {

                        Text(

                            "Kembalian",

                            fontWeight =
                                FontWeight.Bold

                        )

                        Text(

                            "Rp $kembalian",

                            fontWeight =
                                FontWeight.Bold

                        )

                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "Terima kasih telah berbelanja"
                    )

                }

            },

            confirmButton = {

                Button(

                    onClick = {

                        // RESET
                        showStruk = false

                        keranjang.clear()

                        uangBayar = ""

                    }

                ) {

                    Text("Selesai")

                }

            }

        )

    }

}
