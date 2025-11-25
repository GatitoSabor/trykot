package com.example.lvlup.ui.puntos

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PuntosStoreScreen(
    puntosViewModel: PuntosViewModel,
    userId: Int,
    onBack: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color.White,
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tienda Level Up", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                    Text("Tienes: ${puntosViewModel.puntos} puntos", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(12.dp))

                    Text("Canjea tus cupones:", style = MaterialTheme.typography.titleMedium)
                    puntosViewModel.coupons.forEach { coupon ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(3.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(coupon.description, style = MaterialTheme.typography.titleMedium)
                                    Text("${coupon.discountPercent}% de descuento", style = MaterialTheme.typography.bodySmall)
                                    Text("${coupon.pointsCost} puntos", color = MaterialTheme.colorScheme.primary)
                                }
                                Button(
                                    onClick = { puntosViewModel.addToCart(coupon) },
                                    enabled = !puntosViewModel.cart.contains(coupon) && coupon.pointsCost <= puntosViewModel.puntos
                                ) { Text("Agregar") }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Divider()
                    Text("Carrito de cupones", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
                    puntosViewModel.cart.forEach { coupon ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("${coupon.description} (${coupon.pointsCost} pts)", Modifier.weight(1f))
                            IconButton(onClick = { puntosViewModel.removeFromCart(coupon) }) {
                                Icon(Icons.Default.Remove, contentDescription = "Quitar")
                            }
                        }
                    }
                    val totalCart = puntosViewModel.cart.sumOf { it.pointsCost }
                    Text("Total: $totalCart puntos", Modifier.padding(vertical = 8.dp))
                    Button(
                        onClick = {
                            puntosViewModel.canjearCupon(userId)
                        },
                        enabled = puntosViewModel.cart.isNotEmpty() && totalCart <= puntosViewModel.puntos,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Canjear") }
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Volver") }
                }
            }
        }
    }
}
