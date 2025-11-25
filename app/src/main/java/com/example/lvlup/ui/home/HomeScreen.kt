package com.example.lvlup.ui.home

import ProductListViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter
import com.example.lvlup.data.ProductEntity
import com.example.lvlup.ui.cart.CartViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productListViewModel: ProductListViewModel,
    cartViewModel: CartViewModel,
    goToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    var productoSeleccionado by remember { mutableStateOf<ProductEntity?>(null) }
    var soloOfertas by remember { mutableStateOf(false) }

    var categoriaSeleccionada by remember { mutableStateOf("Todas") }
    var expandedCategoria by remember { mutableStateOf(false) }

    var marcaSeleccionada by remember { mutableStateOf("Todas") }
    var expandedMarca by remember { mutableStateOf(false) }

    val products by productListViewModel.productsFlow.collectAsState(initial = emptyList())

    val categories = remember(products) { listOf("Todas") + products.map { it.category }.distinct() }
    val marcas = remember(products) { listOf("Todas") + products.map { it.brand }.distinct() }

    Surface(color = MaterialTheme.colorScheme.background, modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                color = Color.White,
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 10.dp,
                modifier = Modifier
                    .padding(top = 24.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                            )
                            .padding(horizontal = 18.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Catálogo",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = goToCart,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Filled.ShoppingCart,
                                contentDescription = "Ver Carrito",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedCategoria,
                        onExpandedChange = { expandedCategoria = !expandedCategoria },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        OutlinedTextField(
                            value = categoriaSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        AnimatedVisibility(
                            visible = expandedCategoria,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            ExposedDropdownMenu(
                                expanded = expandedCategoria,
                                onDismissRequest = { expandedCategoria = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            categoriaSeleccionada = cat
                                            expandedCategoria = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedMarca,
                        onExpandedChange = { expandedMarca = !expandedMarca },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        OutlinedTextField(
                            value = marcaSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Marca") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMarca)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        AnimatedVisibility(
                            visible = expandedMarca,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            ExposedDropdownMenu(
                                expanded = expandedMarca,
                                onDismissRequest = { expandedMarca = false }
                            ) {
                                marcas.forEach { marca ->
                                    DropdownMenuItem(
                                        text = { Text(marca) },
                                        onClick = {
                                            marcaSeleccionada = marca
                                            expandedMarca = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    ) {
                        Checkbox(
                            checked = soloOfertas,
                            onCheckedChange = { soloOfertas = it }
                        )
                        Text("Solo ofertas", modifier = Modifier.padding(start = 4.dp))
                    }

                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 0.dp)
                            .fillMaxSize()
                    ) {
                        items(products
                            .filter { categoriaSeleccionada == "Todas" || it.category == categoriaSeleccionada }
                            .filter { marcaSeleccionada == "Todas" || it.brand == marcaSeleccionada }
                            .filter { !soloOfertas || (it.discountPercent != null && it.discountPercent > 0) }
                        ) { prod ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable { productoSeleccionado = prod },
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!prod.imageUrl.isNullOrBlank()) {
                                        Image(
                                            painter = rememberAsyncImagePainter(prod.imageUrl),
                                            contentDescription = prod.name,
                                            modifier = Modifier
                                                .size(72.dp)
                                                .padding(end = 12.dp)
                                                .clickable { productoSeleccionado = prod }
                                        )
                                    }
                                    val tieneDescuento = prod.discountPercent != null && prod.discountPercent > 0
                                    val precioFinal = if (tieneDescuento)
                                        prod.price * (1 - (prod.discountPercent!! / 100))
                                    else
                                        prod.price

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(prod.name, style = MaterialTheme.typography.titleLarge)
                                        Text("Marca: ${prod.brand}", style = MaterialTheme.typography.bodyMedium)
                                        if (tieneDescuento) {
                                            Text(
                                                "Precio original: $${prod.price}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.padding(bottom = 2.dp)
                                            )
                                            Text(
                                                "Descuento: ${prod.discountPercent?.toInt()}%",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                "Precio final: $${"%.2f".format(precioFinal)}",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        } else {
                                            Text("${prod.category} - $${prod.price}", style = MaterialTheme.typography.bodyMedium)
                                        }
                                        Text(prod.description, style = MaterialTheme.typography.bodySmall)
                                    }
                                    Button(onClick = { cartViewModel.addToCart(prod) }) {
                                        Text("Agregar")
                                    }
                                }
                            }
                        }
                    }

                    if (productoSeleccionado != null) {
                        AlertDialog(
                            onDismissRequest = { productoSeleccionado = null },
                            title = { Text(productoSeleccionado!!.name) },
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    if (!productoSeleccionado!!.imageUrl.isNullOrBlank()) {
                                        Image(
                                            painter = rememberAsyncImagePainter(productoSeleccionado!!.imageUrl),
                                            contentDescription = productoSeleccionado!!.name,
                                            modifier = Modifier.size(120.dp)
                                        )
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(productoSeleccionado!!.description)
                                    Text("Marca: ${productoSeleccionado!!.brand}", style = MaterialTheme.typography.bodyMedium)
                                }
                            },
                            confirmButton = {
                                Button(onClick = { productoSeleccionado = null }) {
                                    Text("Cerrar")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
