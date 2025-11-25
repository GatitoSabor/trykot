package com.example.lvlup.ui.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (userId: Int) -> Unit,
    onRegister: () -> Unit
) {
    val loginError = viewModel.loginError
    var isLoggingIn by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color.White,
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(0.90f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Bienvenido a LevelUp",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.email = it },
                        label = { Text("Email", color = Color.Black) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        label = { Text("Password", color = Color.Black) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    if (loginError != null) {
                        Text(
                            text = loginError,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            isLoggingIn = true
                            viewModel.login { usuario ->
                                isLoggingIn = false
                                if (usuario != null) {
                                    guardarSesionUsuario(context, usuario.id)
                                    onLoginSuccess(usuario.id)
                                }
                            }
                        },
                        enabled = !isLoggingIn,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(if (isLoggingIn) "Cargando..." else "Ingresar")
                    }
                    TextButton(
                        onClick = onRegister,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text("¿No tienes cuenta? Regístrate", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

fun guardarSesionUsuario(context: Context, userId: Int) {
    val prefs = context.getSharedPreferences("lvlup_prefs", Context.MODE_PRIVATE)
    prefs.edit().putInt("usuario_id", userId).apply()
}
