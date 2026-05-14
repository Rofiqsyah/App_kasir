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

        // FORCE LOGOUT SAAT APP DIBUKA
        // AGAR TAMPIL LOGIN TERLEBIH DAHULU
        FirebaseAuth.getInstance().signOut()

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

    AppNavigation()

}