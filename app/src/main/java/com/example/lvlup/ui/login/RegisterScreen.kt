package com.example.lvlup.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    viewModel: LoginViewModel,
    onBack: () -> Unit,
    onLogin: () -> Unit // Callback para ir al login tras registro exitoso
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Usamos el estado de carga y error del ViewModel
    // Nota: Si quieres estados separados para registro, deberías añadirlos al ViewModel.
    // Por simplicidad, reutilizamos o usamos estados locales para la UI inmediata.
    var localError by remember { mutableStateOf<String?>(null) }

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
                "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        // ⚠️ CORRECCIÓN: Usar 'register' en lugar de 'registerNewUser'
                        viewModel.register(nombre, email, password) {
                            // Callback de éxito: Navegar al login
                            onLogin()
                        }
                    } else {
                        localError = "Las contraseñas no coinciden"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Registrarse")
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onBack) {
                Text("Volver al Login")
            }

            // Mostrar errores locales o del ViewModel
            if (localError != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = localError!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Mostrar errores que vengan del ViewModel (ej: fallo de red)
            if (viewModel.errorMensaje != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = viewModel.errorMensaje!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
