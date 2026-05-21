package com.myapplication.kasir_app.ui.owner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DataKasir(
    val id: String = "",
    val nama: String = "",
    val email: String = "",
    val aktif: Boolean = true
)

@Composable
fun KelolakasirPanel() {
    // Menggunakan data lokal (bukan Firebase) agar langsung tampil
    var listKasir by remember { 
        mutableStateOf(listOf(
            DataKasir("1", "Rina Wati", "rina@kasir.id", true),
            DataKasir("2", "Doni Prasetyo", "doni@kasir.id", true),
            DataKasir("3", "Siti Aminah", "siti@kasir.id", false)
        )) 
    }

    var showDialog by remember { mutableStateOf(false) }
    var editKasir by remember { mutableStateOf<DataKasir?>(null) }
    var showDeleteDialog by remember { mutableStateOf<DataKasir?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Kelola Kasir (${listKasir.size})", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listKasir) { kasir ->
                KasirCard(
                    kasir = kasir,
                    onEdit = { editKasir = kasir; showDialog = true },
                    onDelete = { showDeleteDialog = kasir },
                    onToggleAktif = { 
                        listKasir = listKasir.map { 
                            if (it.id == kasir.id) it.copy(aktif = !it.aktif) else it 
                        }
                    }
                )
            }
        }

        Button(
            onClick = { editKasir = null; showDialog = true },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Outlined.PersonAdd, null)
            Spacer(Modifier.width(8.dp))
            Text("Tambah Kasir Baru")
        }
    }

    if (showDialog) {
        KasirFormDialog(
            existing = editKasir,
            onDismiss = { showDialog = false },
            onSave = { nama, email ->
                if (editKasir != null) {
                    listKasir = listKasir.map { if (it.id == editKasir!!.id) it.copy(nama = nama, email = email) else it }
                } else {
                    listKasir = listKasir + DataKasir(System.currentTimeMillis().toString(), nama, email, true)
                }
                showDialog = false
            }
        )
    }

    showDeleteDialog?.let { kasir ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Hapus Kasir") },
            text = { Text("Yakin ingin menghapus ${kasir.nama}?") },
            confirmButton = {
                TextButton(onClick = { 
                    listKasir = listKasir.filter { it.id != kasir.id }
                    showDeleteDialog = null 
                }) { Text("Hapus", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = null }) { Text("Batal") } }
        )
    }
}

@Composable
private fun KasirCard(kasir: DataKasir, onEdit: () -> Unit, onDelete: () -> Unit, onToggleAktif: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(kasir.nama, fontWeight = FontWeight.Bold)
                    Text(kasir.email, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = if (kasir.aktif) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(if (kasir.aktif) "Aktif" else "Nonaktif", Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onEdit) { Icon(Icons.Outlined.Edit, null, tint = MaterialTheme.colorScheme.primary) }
                IconButton(onClick = onToggleAktif) { Icon(if (kasir.aktif) Icons.Outlined.Block else Icons.Outlined.CheckCircle, null, tint = if (kasir.aktif) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary) }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDelete) { Icon(Icons.Outlined.Delete, null, tint = MaterialTheme.colorScheme.error) }
            }
        }
    }
}

@Composable
private fun KasirFormDialog(existing: DataKasir?, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var nama by remember { mutableStateOf(existing?.nama ?: "") }
    var email by remember { mutableStateOf(existing?.email ?: "") }
    var pass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    val isEdit = existing != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEdit) "Edit Kasir" else "Tambah Kasir Baru") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = nama, onValueChange = { nama = it }, label = { Text("Nama") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                if (!isEdit) {
                    OutlinedTextField(
                        value = pass, onValueChange = { pass = it }, label = { Text("Password") },
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmPass, onValueChange = { confirmPass = it }, label = { Text("Konfirmasi") },
                        visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(nama, email) }, enabled = nama.isNotEmpty() && email.isNotEmpty() && (isEdit || (pass.isNotEmpty() && pass == confirmPass))) {
                Text("Simpan")
            }
        },
        dismissButton = { TextButton(onClick = { onDismiss() }) { Text("Batal") } }
    )
}
