package com.myapplication.kasir_app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.myapplication.kasir_app.AuthViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    // STATUS LIHAT PASSWORD
    var passwordVisible by remember {
        mutableStateOf(false)
    }

    val authState by viewModel.authState.collectAsState()

    // AUTO LOGIN JIKA USER SUDAH LOGIN
    LaunchedEffect(Unit) {

        viewModel.currentUserFlow.collect { user ->

            if (user != null) {

                onLoginSuccess()

            }

        }

    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        // JUDUL
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // EMAIL
        OutlinedTextField(

            value = email,

            onValueChange = {
                email = it
            },

            label = {
                Text("Email")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true

        )

        Spacer(modifier = Modifier.height(8.dp))

        // PASSWORD
        OutlinedTextField(

            value = password,

            onValueChange = {
                password = it
            },

            label = {
                Text("Password")
            },

            modifier = Modifier.fillMaxWidth(),

            singleLine = true,

            visualTransformation =

                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

            trailingIcon = {

                IconButton(

                    onClick = {

                        passwordVisible =
                            !passwordVisible

                    }

                ) {

                    Icon(

                        imageVector =

                            if (passwordVisible)
                                Icons.Outlined.Visibility
                            else
                                Icons.Outlined.VisibilityOff,

                        contentDescription =
                            "Toggle Password"

                    )

                }

            }

        )

        Spacer(modifier = Modifier.height(16.dp))

        // BUTTON LOGIN
        Button(

            onClick = {

                viewModel.login(
                    email,
                    password
                )

            },

            modifier = Modifier.fillMaxWidth(),

            enabled =
                authState !is AuthViewModel.AuthState.Loading

        ) {

            if (authState is AuthViewModel.AuthState.Loading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp)
                )

            } else {

                Text("Login")

            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        // REGISTER
        TextButton(
            onClick = onNavigateToRegister
        ) {

            Text(
                "Don't have an account? Register"
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        // ERROR / SUCCESS
        when (authState) {

            is AuthViewModel.AuthState.Error -> {

                Text(

                    text =
                        (authState as AuthViewModel.AuthState.Error).message,

                    color =
                        MaterialTheme.colorScheme.error

                )

            }

            is AuthViewModel.AuthState.Success -> {

                Text(

                    text =
                        (authState as AuthViewModel.AuthState.Success).message,

                    color =
                        MaterialTheme.colorScheme.primary

                )

            }

            else -> {}

        }

    }

}