package com.myapplication.kasir_app.ui.dashboard

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

fun seedKasirMasterData(
    db: FirebaseFirestore,
    onSuccess: () -> Unit = {},
    onFailure: (Exception) -> Unit = {}
) {
    val categories = listOf(
        categoryData("kopi", "Kopi", 9, 1),
        categoryData("non_kopi", "Non Kopi", 3, 2),
        categoryData("cemilan", "Cemilan", 4, 3)
    )

    val products = listOf(
        productData("americano", "Americano", "kopi", "Kopi", 10000, 20, 5),
        productData("espresso", "Espresso", "kopi", "Kopi", 10000, 15, 5),
        productData("cappuccino", "Cappuccino", "kopi", "Kopi", 15000, 18, 5),
        productData("coffee_latte", "Coffee Latte", "kopi", "Kopi", 15000, 12, 5),
        productData("caramel_macchiato", "Caramel Macchiato", "kopi", "Kopi", 18000, 10, 5),
        productData("es_kopi_susu", "Es Kopi Susu", "kopi", "Kopi", 18000, 25, 5),
        productData("kopi_susu_gula_aren", "Kopi Susu Gula Aren", "kopi", "Kopi", 18000, 30, 5),
        productData("majapahit_black_coffee", "Majapahit Black Coffee", "kopi", "Kopi", 5000, 40, 5),
        productData("majapahit_milk_coffee", "Majapahit Milk Coffee", "kopi", "Kopi", 6000, 35, 5),
        productData("coklat", "Coklat", "non_kopi", "Non Kopi", 10000, 12, 5),
        productData("milk_tea", "Milk Tea", "non_kopi", "Non Kopi", 10000, 20, 5),
        productData("lemon_tea", "Lemon Tea", "non_kopi", "Non Kopi", 6000, 8, 5),
        productData("oreo_lava_toast", "Oreo Lava Toast", "cemilan", "Cemilan", 10000, 15, 5),
        productData("lotus_biscoff_lava_toast", "Lotus Biscoff Lava Toast", "cemilan", "Cemilan", 12000, 10, 5),
        productData("chocolate_lava_toast", "Chocolate Lava Toast", "cemilan", "Cemilan", 10000, 9, 5),
        productData("cheese_chocolate_lava_toast", "Cheese Chocolate Lava Toast", "cemilan", "Cemilan", 15000, 7, 5)
    )

    val batch = db.batch()

    categories.forEach { category ->
        val id = category["id"] as String
        batch.set(db.collection("categories").document(id), category, SetOptions.merge())
    }

    products.forEach { product ->
        val id = product["id"] as String
        batch.set(db.collection("products").document(id), product, SetOptions.merge())
    }

    batch.commit()
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
}

private fun categoryData(
    id: String,
    nama: String,
    jumlahProduk: Int,
    urutan: Int
): HashMap<String, Any> {
    return hashMapOf(
        "id" to id,
        "nama" to nama,
        "jumlahProduk" to jumlahProduk,
        "urutan" to urutan
    )
}

private fun productData(
    id: String,
    nama: String,
    kategoriId: String,
    kategori: String,
    harga: Int,
    stok: Int,
    stokMinimum: Int
): HashMap<String, Any> {
    return hashMapOf(
        "id" to id,
        "nama" to nama,
        "kategoriId" to kategoriId,
        "kategori" to kategori,
        "harga" to harga,
        "hargaText" to "Rp ${formatRupiah(harga)}",
        "stok" to stok,
        "stokMinimum" to stokMinimum,
        "tersedia" to true
    )
}

private fun formatRupiah(value: Int): String {
    return value.toString()
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()
}
