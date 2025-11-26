package com.example.lvlup.ui.adminproductos

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.lvlup.data.ProductoDto
// ⚠️ IMPORTAR LA FUNCIÓN DE UTILIDAD
import com.example.lvlup.util.displayToast
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductosScreen() {
    val context = LocalContext.current
    val viewModel: AdminProductosViewModel = viewModel(factory = AdminProductosViewModelFactory(context))

    // ⚠️ CORRECCIÓN 1: Pasar el valor inicial (emptyList()) a collectAsState para el StateFlow
    val productos by viewModel.uiState.collectAsState(initial = emptyList())

    // ⚠️ CORRECCIÓN 2: Acceder a estadoMensaje directamente, ya que es un Compose State (MutableState)
    val mensaje = viewModel.estadoMensaje

    var showForm by remember { mutableStateOf(false) }

    // Mostrar Toast cuando el mensaje de estado cambia
    LaunchedEffect(mensaje) {
        mensaje?.let {
            // ⚠️ CORRECCIÓN 3: Ahora 'displayToast' está disponible
            displayToast(context, it)
            viewModel.mensajeMostrado()
        }
    }

    Scaffold(
// ... (resto del código de Scaffold y FAB es correcto)
// ...
        topBar = {
            TopAppBar(title = { Text("Administración de Productos") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.limpiarSeleccion()
                showForm = true
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Producto")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    "Total de Productos: ${productos.size}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Divider()

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(productos) { producto ->
                        ProductoAdminItem(
                            producto = producto,
                            onEdit = {
                                viewModel.seleccionarProducto(it)
                                showForm = true
                            },
                            onDelete = { viewModel.eliminarProducto(it.idProducto) }
                        )
                    }
                }
            }

            // Mostrar el formulario en un diálogo
            if (showForm) {
                ProductoFormDialog(
                    producto = viewModel.productoSeleccionado,
                    onDismiss = { showForm = false; viewModel.limpiarSeleccion() },
                    onSave = { producto, imagenFile ->
                        if (producto.idProducto == 0L) {
                            // Crear (POST)
                            viewModel.crearProducto(
                                nombre = producto.nombre,
                                descripcion = producto.descripcion,
                                precio = producto.precio,
                                categoria = producto.categoria,
                                marca = producto.marca,
                                descuento = producto.descuento,
                                envioGratis = producto.envioGratis,
                                juego = producto.juego ?: "",
                                imagen = imagenFile
                            )
                        } else {
                            // Actualizar (PUT)
                            viewModel.actualizarProducto(producto)
                            // Nota: La imagenFile se ignora en PUT.
                        }
                        showForm = false
                    }
                )
            }
        }
    )
}

@Composable
fun ProductoAdminItem(producto: ProductoDto, onEdit: (ProductoDto) -> Unit, onDelete: (ProductoDto) -> Unit) {
    // ... (El resto de esta función no necesita cambios)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            if (!producto.imagen.isNullOrBlank()) {
                Image(
                    painter = rememberAsyncImagePainter(producto.imagen),
                    contentDescription = producto.nombre,
                    modifier = Modifier.size(50.dp).padding(end = 8.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text("ID: ${producto.idProducto} | ${producto.categoria}", style = MaterialTheme.typography.bodySmall)
                Text("$${"%.2f".format(producto.precio)} | Stock: ${producto.stock}", style = MaterialTheme.typography.bodyMedium)
            }

            // Botones de acción
            IconButton(onClick = { onEdit(producto) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = { onDelete(producto) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}