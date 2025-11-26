package com.example.lvlup.ui.micuenta

import ads_mobile_sdk.h5
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MiCuentaScreen(
    miCuentaVM: ProfileViewModel // Recibe el ViewModel como parámetro
) {
    // Observa el usuario reactivo del ViewModel
    val usuario by miCuentaVM.usuario.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Mi Cuenta", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(24.dp))

        usuario?.let { user ->
            Text("ID: ${user.id}")
            Text("Nombre: ${user.nombre}")
            Text("Email: ${user.email}")
            // Agrega aquí los campos que tenga tu UserEntity
        } ?: run {
            CircularProgressIndicator()
            Text("Cargando información del usuario…")
        }
    }
}
