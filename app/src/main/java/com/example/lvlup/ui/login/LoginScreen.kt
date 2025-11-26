package com.example.lvlup.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (Int) -> Unit, // El ID suele ser Int en la navegación de Android
    onRegister: () -> Unit
) {
    // No necesitamos estados locales aquí, usamos los del ViewModel
    val email = viewModel.email
    val password = viewModel.password
    val isLoading = viewModel.isLoading
    val errorMensaje = viewModel.errorMensaje // Usar la propiedad correcta

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) }, // Usar la función del VM
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) }, // Usar la función del VM
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        // Llamar a login y convertir el Long a Int para la navegación
                        viewModel.login { userIdLong ->
                            onLoginSuccess(userIdLong.toInt())
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotBlank() && password.isNotBlank()
                ) {
                    Text("Ingresar")
                }

                Spacer(Modifier.height(12.dp))

                TextButton(onClick = onRegister) {
                    Text("¿No tienes cuenta? Regístrate")
                }
            }

            // Mostrar mensaje de error si existe
            if (errorMensaje != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = errorMensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}