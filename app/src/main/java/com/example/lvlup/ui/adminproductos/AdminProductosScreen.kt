package com.example.lvlup.ui.adminproductos

import ProductoForm
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import com.example.lvlup.data.ProductEntity
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource

@Composable
fun AdminProductosScreenWrapper() {
    val context = LocalContext.current.applicationContext
    val adminVM: AdminProductosViewModel = viewModel(factory = AdminProductosViewModelFactory(context))
    AdminProductosScreen(adminVM)
}

@Composable
fun AdminProductosScreen(viewModel: AdminProductosViewModel) {
    val productos by viewModel.productos.collectAsState()
    var productoEdit by remember { mutableStateOf<ProductEntity?>(null) }
    var mostrarForm by remember { mutableStateOf(false) }
    var productoDetalle by remember { mutableStateOf<ProductEntity?>(null) }

    Surface(
        color = Color(0xFF1E1E22),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = Color(0xFFF5F0FF),
                shape = RoundedCornerShape(25.dp),
                shadowElevation = 16.dp,
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp, horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory,
                                contentDescription = "Inventario",
                                tint = Color(0xFF7C4DFF),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Gestión de Productos",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C4DFF)
                            )
                        }
                        Text(
                            text = "Administra tus productos y agrega más.",
                            fontSize = 15.sp,
                            color = Color(0xFF5B5B69),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp),
                            color = Color(0xFF7C4DFF),
                            thickness = 2.dp
                        )
                        Text(
                            text = "Total productos: ${productos.size}",
                            color = Color(0xFF7C4DFF),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEBE6FC), RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                                .padding(bottom = 16.dp),
                            textAlign = TextAlign.End
                        )
                        Button(
                            onClick = { mostrarForm = true; productoEdit = null },
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7C4DFF),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Text("Agregar producto", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        }
                        if (mostrarForm) {
                            ProductoForm(
                                producto = productoEdit,
                                onSubmit = { prod ->
                                    if (productoEdit == null)
                                        viewModel.agregarProducto(prod)
                                    else
                                        viewModel.actualizarProducto(prod)
                                    mostrarForm = false; productoEdit = null
                                },
                                onCancel = {
                                    mostrarForm = false; productoEdit = null
                                }
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                        Text(
                            text = "Listado de productos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF7C4DFF),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = 4.dp)
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            color = Color(0xFFCCCCCC),
                            thickness = 1.dp
                        )
                    }
                    items(productos) { prod ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .clickable { productoDetalle = prod },
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE6D6EA)
                            ),
                            elevation = CardDefaults.cardElevation(12.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mostrar imagen desde drawable o galería
                                when {
                                    prod.imageResId != null -> {
                                        Image(
                                            painter = painterResource(prod.imageResId!!),
                                            contentDescription = prod.name,
                                            modifier = Modifier
                                                .size(62.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .padding(end = 12.dp)
                                        )
                                    }
                                    !prod.imageUrl.isNullOrBlank() -> {
                                        Image(
                                            painter = rememberAsyncImagePainter(prod.imageUrl),
                                            contentDescription = prod.name,
                                            modifier = Modifier
                                                .size(62.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .padding(end = 12.dp)
                                        )
                                    }
                                }
                                Column(
                                    Modifier.weight(1f)
                                ) {
                                    Text(prod.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text("${prod.category} - ${prod.brand} - \$${prod.price}", color = Color(0xFF7C4DFF), fontSize = 15.sp)
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { productoEdit = prod; mostrarForm = true }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF7C4DFF))
                                    }
                                    Spacer(Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { viewModel.eliminarProducto(prod) }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD84328))
                                    }
                                }
                            }
                        }
                    }
                }
                if (productoDetalle != null) {
                    AlertDialog(
                        onDismissRequest = { productoDetalle = null },
                        confirmButton = {
                            Button(
                                onClick = { productoDetalle = null },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF))
                            ) {
                                Text("Cerrar", color = Color.White)
                            }
                        },
                        title = {
                            Text(productoDetalle!!.name, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                        },
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                when {
                                    productoDetalle!!.imageResId != null -> {
                                        Image(
                                            painter = painterResource(productoDetalle!!.imageResId!!),
                                            contentDescription = productoDetalle!!.name,
                                            modifier = Modifier
                                                .size(128.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .padding(bottom = 12.dp)
                                        )
                                    }
                                    !productoDetalle!!.imageUrl.isNullOrBlank() -> {
                                        Image(
                                            painter = rememberAsyncImagePainter(productoDetalle!!.imageUrl),
                                            contentDescription = productoDetalle!!.name,
                                            modifier = Modifier
                                                .size(128.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .padding(bottom = 12.dp)
                                        )
                                    }
                                }
                                Text("Categoría: ${productoDetalle!!.category}")
                                Text("Marca: ${productoDetalle!!.brand}")
                                Text("Descripción: ${productoDetalle!!.description}")
                                Text("Precio: \$${productoDetalle!!.price}", fontWeight = FontWeight.Bold)
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = Color(0xFFF5F0FF)
                    )
                }
            }
        }
    }
}
