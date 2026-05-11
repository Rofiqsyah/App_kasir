package com.myapplication.kasir_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp
import com.myapplication.kasir_app.ui.theme.Kasir_ApkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            Kasir_ApkTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent(viewModel: AuthViewModel = viewModel()) {
    val currentUser by viewModel.currentUserFlow.collectAsState()

    if (currentUser != null) {
        HomeScreen(onLogout = { /* handled in HomeScreen */ })
    } else {
        AppNavigation()
    }
}