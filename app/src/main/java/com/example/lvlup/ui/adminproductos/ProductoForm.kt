import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lvlup.data.ProductEntity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import androidx.core.net.toUri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.lvlup.R

@Composable
fun ProductoForm(
    producto: ProductEntity? = null,
    onSubmit: (ProductEntity) -> Unit,
    onCancel: () -> Unit
) {
    var nombre by remember { mutableStateOf(producto?.name ?: "") }
    var categoria by remember { mutableStateOf(producto?.category ?: "") }
    var marca by remember { mutableStateOf(producto?.brand ?: "") }
    var precio by remember { mutableStateOf(producto?.price?.toString() ?: "") }
    var descripcion by remember { mutableStateOf(producto?.description ?: "") }
    var descuento by remember { mutableStateOf(producto?.discountPercent?.toString() ?: "") }
    var error by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(producto?.imageUrl?.toUri()) }
    var imageResId by remember { mutableStateOf(producto?.imageResId) }
    var mostrarPicker by remember { mutableStateOf(false) }

    val drawableImages = listOf(
        R.drawable.monitor, // Cambia estos por los nombres de tus drawables reales
        R.drawable.mouse,
        R.drawable.teclado
    )

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            imageResId = null
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })
        OutlinedTextField(value = marca, onValueChange = { marca = it }, label = { Text("Marca") })
        OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(value = descuento, onValueChange = { descuento = it }, label = { Text("Descuento (%)") })

        Spacer(modifier = Modifier.height(10.dp))

        // BOTÓN QUE DESPLIEGA EL PICKER DE IMÁGENES
        Button(
            onClick = { mostrarPicker = true },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Seleccionar imagen")
        }

        // Modal para seleccionar imagen
        if (mostrarPicker) {
            AlertDialog(
                onDismissRequest = { mostrarPicker = false },
                confirmButton = {},
                title = { Text("Seleccione una imagen") },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Imágenes predeterminadas:")
                        Row(Modifier.horizontalScroll(rememberScrollState())) {
                            drawableImages.forEach { resId ->
                                Card(
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .size(64.dp)
                                        .clickable {
                                            imageResId = resId
                                            imageUri = null
                                            mostrarPicker = false
                                        },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Image(
                                        painter = painterResource(resId),
                                        contentDescription = null,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {
                                pickImageLauncher.launch("image/*")
                                mostrarPicker = false
                            }
                        ) {
                            Text("Seleccionar desde galería")
                        }
                    }
                }
            )
        }

        // Previsualización de imagen elegida
        when {
            imageResId != null -> {
                Image(
                    painter = painterResource(imageResId!!),
                    contentDescription = "Imagen seleccionada del recurso",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(vertical = 8.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                )
            }
            imageUri != null -> {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "Imagen del producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(vertical = 8.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                )
            }
        }

        if (error.isNotEmpty())
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(4.dp))

        Row(Modifier.padding(top = 8.dp)) {
            Button(onClick = {
                val precioDouble = precio.toDoubleOrNull()
                val descuentoDouble = descuento.toDoubleOrNull()
                if (nombre.isBlank() || categoria.isBlank() || marca.isBlank() || precioDouble == null || precioDouble <= 0) {
                    error = "Completa los campos obligatorios y asegúrate de que el precio sea positivo."
                } else {
                    onSubmit(
                        ProductEntity(
                            id = producto?.id ?: 0,
                            name = nombre,
                            category = categoria,
                            brand = marca,
                            price = precioDouble,
                            description = descripcion,
                            imageUrl = imageUri?.toString(),
                            imageResId = imageResId,
                            discountPercent = descuentoDouble
                        )
                    )
                }
            }) { Text(if (producto == null) "Agregar" else "Actualizar") }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onCancel) { Text("Cancelar") }
        }
    }
}
