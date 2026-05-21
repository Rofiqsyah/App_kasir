package com.myapplication.kasir_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.myapplication.kasir_app.ui.theme.Kasir_ApkTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // INISIALISASI FIREBASE
        FirebaseApp.initializeApp(this)

        // EDGE TO EDGE
        enableEdgeToEdge()

        /*
           CATATAN:
           Baris di bawah ini (signOut) akan memaksa user login ulang SETIAP KALI aplikasi dibuka.
           Jika ingin user tetap login otomatis, hapus atau beri komentar pada baris ini.
        */
        // FirebaseAuth.getInstance().signOut()

        setContent {
            Kasir_ApkTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent(
    viewModel: AuthViewModel = viewModel()
) {
    // Memanggil Navigasi Utama
    AppNavigation()
}