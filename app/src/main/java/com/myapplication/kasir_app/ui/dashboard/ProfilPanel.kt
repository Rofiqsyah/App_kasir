package com.myapplication.kasir_app.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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

@Composable
fun ProfilPanel(onLogout: () -> Unit) {
    var nama         by remember { mutableStateOf("Budi Santoso") }
    var noHp         by remember { mutableStateOf("+62 812-3456-7890") }
    var email        by remember { mutableStateOf("budi@kasirapp.id") }
    var passwordLama by remember { mutableStateOf("") }
    var passwordBaru by remember { mutableStateOf("") }
    var konfirmasi   by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var savedSnackbar    by remember { mutableStateOf(false) }

    val passwordError = when {
        passwordBaru.isNotEmpty() && passwordBaru.length < 8 -> "Password minimal 8 karakter"
        passwordBaru.isNotEmpty() && passwordBaru != konfirmasi -> "Password tidak cocok"
        else -> null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Avatar card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = nama.split(" ")
                                .take(2)
                                .joinToString("") { it.take(1).uppercase() },
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Text(nama, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        "Kasir",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // Info stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoStatItem("1.284",  "Total transaksi")
                    InfoStatItem("12 Jan 2024", "Bergabung")
                    InfoStatItem("Pagi",   "Shift aktif")
                }
            }
        }

        // Edit data diri
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Person, contentDescription = null,
                        modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Edit data diri", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama lengkap") },
                    leadingIcon = { Icon(Icons.Outlined.Badge, null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = noHp,
                    onValueChange = { noHp = it },
                    label = { Text("No. HP") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Outlined.Email, null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Button(
                    onClick = { savedSnackbar = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Outlined.Save, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Simpan perubahan")
                }
            }
        }

        // Ganti password
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Lock, contentDescription = null,
                        modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Ganti password", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }

                PasswordField(value = passwordLama, onValueChange = { passwordLama = it }, label = "Password lama")
                PasswordField(value = passwordBaru, onValueChange = { passwordBaru = it }, label = "Password baru")
                PasswordField(
                    value = konfirmasi,
                    onValueChange = { konfirmasi = it },
                    label = "Konfirmasi password",
                    isError = passwordError != null && konfirmasi.isNotEmpty()
                )

                if (passwordError != null && (passwordBaru.isNotEmpty() || konfirmasi.isNotEmpty())) {
                    Text(passwordError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Button(
                    onClick = { /* handle change password */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = passwordLama.isNotBlank() && passwordBaru.isNotBlank()
                            && konfirmasi.isNotBlank() && passwordError == null
                ) {
                    Icon(Icons.Outlined.Lock, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Ganti password")
                }
            }
        }

        // Logout
        OutlinedButton(
            onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Outlined.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Keluar dari akun")
        }

        Spacer(Modifier.height(8.dp))
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon  = { Icon(Icons.Outlined.Logout, contentDescription = null) },
            title = { Text("Keluar?") },
            text  = { Text("Apakah kamu yakin ingin keluar dari akun ini?") },
            confirmButton = {
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Keluar") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
fun InfoStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Outlined.Lock, null) },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = if (visible) "Sembunyikan" else "Tampilkan"
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError
    )
}