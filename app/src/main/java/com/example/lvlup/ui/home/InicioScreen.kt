package com.example.lvlup.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lvlup.data.ProductEntity
import coil.compose.rememberAsyncImagePainter

@Composable
fun InicioScreen(
    productosEnOferta: List<ProductEntity>,
    onGoCatalogo: () -> Unit,
    onGoComunidad: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = Color.White,
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "¡Bienvenido a LvlUp!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("Productos en oferta:", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    productosEnOferta.take(3).forEach { producto ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                if (!producto.imageUrl.isNullOrBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(producto.imageUrl),
                                        contentDescription = producto.name,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .padding(end = 10.dp)
                                    )
                                }
                                Column(Modifier.weight(1f)) {
                                    Text(producto.name, style = MaterialTheme.typography.bodyLarge)
                                    Text("Oferta especial", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                                    Text(
                                        "${producto.discountPercent?.toInt() ?: 0}% OFF",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF388E3C)
                                    )
                                }
                            }
                        }
                    }


                    Spacer(Modifier.height(24.dp))
                    Button(onClick = onGoCatalogo, modifier = Modifier.fillMaxWidth()) {
                        Text("Ver Catálogo Completo")
                    }

                    Spacer(Modifier.height(40.dp))
                    Text(
                        "¡Prueba nuestra nueva función comunidad!",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onGoComunidad, modifier = Modifier.fillMaxWidth()) {
                        Text("Entrar a Comunidad")
                    }
                }
            }
        }
    }
}