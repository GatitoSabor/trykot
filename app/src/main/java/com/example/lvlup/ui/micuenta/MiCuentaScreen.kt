package com.example.lvlup.ui.micuenta

import ProfileViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.lvlup.data.CouponEntity

@Composable
fun MiCuentaScreen(
    viewModel: ProfileViewModel,
    usuarioId: Int?,
    onBack: () -> Unit
) {
    var editPassword by remember { mutableStateOf(false) }
    var tempPass by remember { mutableStateOf("") }
    val user = viewModel.usuario
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var passError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(usuarioId) {
        usuarioId?.let { viewModel.cargarUsuarioCompleto(it) }
    }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
            Surface(
                color = Color.White,
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Mi Cuenta", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(16.dp))

                    Text("Nombre:", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (viewModel.editarNombre) {
                            OutlinedTextField(
                                value = viewModel.tempNombre,
                                onValueChange = { viewModel.tempNombre = it },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                viewModel.updateNombre(viewModel.tempNombre)
                            }) { Text("Guardar") }
                        } else {
                            Text(user?.nombre ?: "", modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(8.dp))
                            TextButton(onClick = {
                                viewModel.tempNombre = user?.nombre ?: ""
                                viewModel.editarNombre = true
                            }) { Text("Editar") }
                        }
                    }
                    Spacer(Modifier.height(12.dp))

                    Text("Email:", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (viewModel.editarEmail) {
                            OutlinedTextField(
                                value = viewModel.tempEmail,
                                onValueChange = { viewModel.tempEmail = it },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                viewModel.updateEmail(viewModel.tempEmail)
                            }) { Text("Guardar") }
                        } else {
                            Text(user?.email ?: "", modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(8.dp))
                            TextButton(onClick = {
                                viewModel.tempEmail = user?.email ?: ""
                                viewModel.editarEmail = true
                            }) { Text("Editar") }
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    Divider()
                    Spacer(Modifier.height(12.dp))

                    Text("Direcciones:", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    viewModel.direcciones.forEach { direccion ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            if (viewModel.editandoDireccionId == direccion.id) {
                                OutlinedTextField(
                                    value = direccion.value,
                                    onValueChange = { viewModel.updateDireccionInPlace(direccion.id, it) },
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(Modifier.width(8.dp))
                                TextButton(onClick = { viewModel.stopEditDireccion() }) { Text("Listo") }
                                IconButton(onClick = { viewModel.removeAddress(direccion) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Quitar")
                                }
                            } else {
                                Text(direccion.value, Modifier.weight(1f))
                                Spacer(Modifier.width(8.dp))
                                TextButton(onClick = { viewModel.startEditDireccion(direccion) }) { Text("Editar") }
                                IconButton(onClick = { viewModel.removeAddress(direccion) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Quitar")
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = viewModel.newAddressText,
                            onValueChange = { viewModel.newAddressText = it },
                            label = { Text("Nueva dirección") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { viewModel.addAddress(viewModel.newAddressText) }) { Text("Agregar") }
                    }

                    Spacer(Modifier.height(20.dp))
                    Divider()
                    Spacer(Modifier.height(12.dp))

                    Text("Contraseña:", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (editPassword) {
                            Column(Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = oldPass,
                                    onValueChange = { oldPass = it },
                                    label = { Text("Contraseña actual") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    visualTransformation = PasswordVisualTransformation()
                                )
                                Spacer(Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = newPass,
                                    onValueChange = { newPass = it },
                                    label = { Text("Nueva contraseña") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    visualTransformation = PasswordVisualTransformation()
                                )
                                if (passError != null) {
                                    Text(passError ?: "", color = MaterialTheme.colorScheme.error)
                                }
                                Spacer(Modifier.height(8.dp))
                                Row {
                                    Button(
                                        onClick = {
                                            if (oldPass == user?.password) {
                                                if(newPass.isBlank()) {
                                                    passError = "La nueva contraseña no puede estar vacía"
                                                } else {
                                                    viewModel.updatePassword(newPass)
                                                    editPassword = false
                                                    oldPass = ""
                                                    newPass = ""
                                                    passError = null
                                                }
                                            } else {
                                                passError = "La contraseña actual no es correcta"
                                            }
                                        }
                                    ) { Text("Guardar") }
                                    Spacer(Modifier.width(8.dp))
                                    OutlinedButton(
                                        onClick = {
                                            editPassword = false
                                            oldPass = ""
                                            newPass = ""
                                            passError = null
                                        }
                                    ) { Text("Cancelar") }
                                }
                            }
                        } else {
                            Text("********", modifier = Modifier.weight(1f))
                            Spacer(Modifier.width(8.dp))
                            TextButton(onClick = { editPassword = true }) { Text("Cambiar") }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    Text("Mis Cupones", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(10.dp))
                    if (viewModel.cupones.isEmpty()) {
                        Text("No tienes cupones canjeados aún.", style = MaterialTheme.typography.bodySmall)
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 160.dp)
                        ) {
                            items(viewModel.cupones) { cupon ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Text(
                                            "${cupon.description}: ${cupon.discountPercent}% OFF",
                                            modifier = Modifier.weight(1f)
                                        )
                                        if (cupon.isUsed) {
                                            Text("Usado", color = Color.Gray)
                                        } else {
                                            Text("Activo", color = Color(0xFF388E3C))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) { Text("Volver") }
                }
            }
        }
    }
}
