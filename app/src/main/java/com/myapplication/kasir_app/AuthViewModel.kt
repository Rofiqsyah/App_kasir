package com.myapplication.kasir_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUserFlow: StateFlow<FirebaseUser?> = _currentUser

    private val _justLoggedOut = MutableStateFlow(false)
    val justLoggedOut: StateFlow<Boolean> = _justLoggedOut

    private val _loggedInEmail = MutableStateFlow("")
    val loggedInEmail: StateFlow<String> = _loggedInEmail

    private val _userRole = MutableStateFlow("")
    val userRole: StateFlow<String> = _userRole

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _currentUser.value = user
            if (user != null) {
                fetchUserRole(user.uid)
            }
        }
    }

    private fun fetchUserRole(uid: String) {
        viewModelScope.launch {
            try {
                val doc = db.collection("users").document(uid).get().await()
                if (doc.exists()) {
                    _userRole.value = doc.getString("role") ?: ""
                }
            } catch (e: Exception) {
                _userRole.value = ""
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun clearLogoutFlag() {
        _justLoggedOut.value = false
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val uid = result.user?.uid ?: ""

                // Tentukan role berdasarkan konten email
                val expectedRole = if (email.contains("kasir", ignoreCase = true)) "kasir" else "owner"

                val doc = db.collection("users").document(uid).get().await()
                val currentRoleInFirestore = doc.getString("role") ?: ""

                // Sinkronisasi: Jika role di Firestore berbeda dengan pola email, update Firestore
                if (currentRoleInFirestore != expectedRole) {
                    val userData = hashMapOf(
                        "uid" to uid,
                        "email" to email,
                        "role" to expectedRole
                    )
                    db.collection("users").document(uid).set(userData).await()
                }

                _justLoggedOut.value = false
                _loggedInEmail.value = email.trim()
                _userRole.value = expectedRole
                _authState.value = AuthState.Success("Berhasil masuk sebagai $expectedRole")

            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login gagal")
            }
        }
    }

    fun logout() {
        auth.signOut()
        _currentUser.value = null
        _loggedInEmail.value = ""
        _userRole.value = ""
        _justLoggedOut.value = true
        _authState.value = AuthState.Idle
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val message: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}