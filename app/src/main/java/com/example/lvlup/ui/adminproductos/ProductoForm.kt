package com.example.lvlup.ui.adminproductos

import android.net.Uri
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lvlup.data.ProductoDto
import java.io.File
import java.io.FileOutputStream

@Composable
fun ProductoFormDialog(
    producto: ProductoDto?,
    onDismiss: () -> Unit,
    onSave: (ProductoDto, File?) -> Unit
) {
    val context = LocalContext.current
    val isEditing = producto != null

    // Estados del formulario
    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }
    var precio by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var stock by remember { mutableStateOf(producto?.stock?.toString() ?: "") }
    var categoria by remember { mutableStateOf(producto?.categoria ?: "") }
    var marca by remember { mutableStateOf(producto?.marca ?: "") }
    var descuento by remember { mutableStateOf(producto?.descuento?.toString() ?: "0") }
    var envioGratis by remember { mutableStateOf(producto?.envioGratis ?: false) }
    var juego by remember { mutableStateOf(producto?.juego ?: "") }

    // Estados para la imagen
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageFileToSend by remember { mutableStateOf<File?>(null) }

    // Launcher para la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        if (uri != null) {
            imageFileToSend = getFileFromUri(context, uri)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        title = {
            Text(
                text = if (isEditing) "Editar Producto" else "Nuevo Producto",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                // Sección 1: Información Básica
                Text("Información General", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.height(8.dp))

                CustomTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre del Producto")
                CustomTextField(value = descripcion, onValueChange = { descripcion = it }, label = "Descripción")

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomTextField(
                        value = categoria,
                        onValueChange = { categoria = it },
                        label = "Categoría",
                        modifier = Modifier.weight(1f)
                    )
                    CustomTextField(
                        value = marca,
                        onValueChange = { marca = it },
                        label = "Marca",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Sección 2: Precios y Stock
                Text("Detalles de Venta", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.height(8.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CustomTextField(
                        value = precio,
                        onValueChange = { precio = it },
                        label = "Precio ($)",
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    CustomTextField(
                        value = stock,
                        onValueChange = { stock = it },
                        label = "Stock",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
                CustomTextField(
                    value = descuento,
                    onValueChange = { descuento = it },
                    label = "Descuento (%)",
                    keyboardType = KeyboardType.Decimal
                )

                Spacer(Modifier.height(16.dp))

                // Sección 3: Extras e Imagen
                Text("Extras", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                Spacer(Modifier.height(8.dp))

                CustomTextField(value = juego, onValueChange = { juego = it }, label = "Juego (Opcional)")

                // Checkbox de Envío Gratis
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { envioGratis = !envioGratis }
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .padding(8.dp)
                ) {
                    Checkbox(checked = envioGratis, onCheckedChange = { envioGratis = it })
                    Text("Ofrecer Envío Gratis", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(12.dp))

                // --- Selector de Imagen Mejorado ---
                OutlinedButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (selectedImageUri != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = if (selectedImageUri != null) Icons.Default.Check else Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = when {
                            selectedImageUri != null -> "¡Imagen Seleccionada!"
                            isEditing && !producto?.imagen.isNullOrBlank() -> "Cambiar Imagen (Actual existe)"
                            else -> "Seleccionar Imagen"
                        }
                    )
                }
                if (selectedImageUri != null) {
                    Text(
                        text = "Archivo listo para subir",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dto = ProductoDto(
                        idProducto = producto?.idProducto ?: 0L,
                        nombre = nombre,
                        descripcion = descripcion,
                        envioGratis = envioGratis,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        descuento = descuento.toDoubleOrNull() ?: 0.0,
                        imagen = producto?.imagen, // Mantiene la URL anterior si es edición
                        marca = marca,
                        categoria = categoria,
                        juego = juego,
                        estado = producto?.estado ?: "Nuevo",
                        stock = stock.toIntOrNull() ?: 0
                    )
                    onSave(dto, imageFileToSend)
                },
                enabled = nombre.isNotBlank() && precio.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar Producto")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.error)
            }
        },
        containerColor = Color.White, // Fondo blanco limpio
        shape = RoundedCornerShape(16.dp)
    )
}

// Componente auxiliar para campos de texto limpios
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

// Función de utilidad para obtener el archivo (se mantiene igual)
fun getFileFromUri(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver
    val fileName = "temp_image_${System.currentTimeMillis()}.jpg"
    val tempFile = File(context.cacheDir, fileName)

    try {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}