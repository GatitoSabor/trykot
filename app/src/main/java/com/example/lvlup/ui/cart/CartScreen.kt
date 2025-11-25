package com.example.lvlup.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit
) {
    var compraFinalizada by remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                compraFinalizada -> {
                    Surface(
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "¡Compra realizada con éxito!",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    compraFinalizada = false
                                    viewModel.cartItems.clear()
                                    onBack()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Volver a la tienda")
                            }
                        }
                    }
                }
                viewModel.cartItems.isEmpty() -> {
                    Surface(
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No hay productos en el carrito", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = onBack,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Volver a la tienda")
                            }
                        }
                    }
                }
                else -> {
                    Surface(
                        color = Color.White,
                        shape = MaterialTheme.shapes.medium,
                        shadowElevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Mi Carrito", style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 350.dp)
                            ) {
                                items(viewModel.cartItems) { item ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        elevation = CardDefaults.cardElevation(4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (!item.producto.imageUrl.isNullOrBlank()) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(item.producto.imageUrl),
                                                    contentDescription = item.producto.name,
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .padding(end = 12.dp)
                                                )
                                            }
                                            val tieneDescuento = item.producto.discountPercent != null && item.producto.discountPercent > 0
                                            val precioUnidad = if (tieneDescuento)
                                                item.producto.price * (1 - (item.producto.discountPercent!! / 100))
                                            else
                                                item.producto.price

                                            Column(Modifier.weight(1f)) {
                                                Text(item.producto.name, style = MaterialTheme.typography.titleMedium)
                                                if (tieneDescuento) {
                                                    Text(
                                                        "Precio original: $${item.producto.price}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.error
                                                    )
                                                    Text(
                                                        "Descuento: ${item.producto.discountPercent?.toInt()}%",
                                                        style = MaterialTheme.typography.labelMedium,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                    Text(
                                                        "Precio final: $${"%.2f".format(precioUnidad)} x${item.cantidad}",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                    Text(
                                                        "Subtotal: $${"%.2f".format(precioUnidad * item.cantidad)}",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                } else {
                                                    Text(
                                                        "Precio: $${item.producto.price} x${item.cantidad}",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                    Text(
                                                        "Subtotal: $${"%.2f".format(item.producto.price * item.cantidad)}",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )
                                                }
                                            }
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                IconButton(onClick = { viewModel.removeFromCart(item.producto) }) {
                                                    Icon(Icons.Default.Remove, contentDescription = "Restar")
                                                }
                                                Text("${item.cantidad}", modifier = Modifier.width(32.dp))
                                                IconButton(onClick = { viewModel.addToCart(item.producto) }) {
                                                    Icon(Icons.Default.Add, contentDescription = "Sumar")
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            val total = viewModel.total
                            Text("Total: $${"%.2f".format(total)}", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    compraFinalizada = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Finalizar compra")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                                Text("Seguir comprando")
                            }
                        }
                    }
                }
            }
        }
    }
}
