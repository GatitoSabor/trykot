package com.example.lvlup.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

@Composable
fun ContactoScreen(
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showError by remember { mutableStateOf(false) }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var imagenBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    var cameraPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        cameraPermissionGranted = isGranted
        if (!isGranted) {
            scope.launch {
                snackbarHostState.showSnackbar("Debes habilitar el permiso de cámara en ajustes.")
            }
        }
    }

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
        imagenBitmap = null
    }

    val takePhotoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        imagenBitmap = bitmap
        imagenUri = null
    }

    val camposValidos = nombre.isNotBlank() && email.isNotBlank() && mensaje.isNotBlank()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Contáctanos",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; showError = false },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; showError = false },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = mensaje,
                        onValueChange = { mensaje = it; showError = false },
                        label = { Text("¿En qué te podemos ayudar?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        maxLines = 6
                    )

                    Spacer(Modifier.height(8.dp))
                    Text("Adjuntar imagen (opcional)", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { pickPhotoLauncher.launch("image/*") }) {
                            Text("Galería")
                        }
                        Button(onClick = {
                            if (cameraPermissionGranted) {
                                takePhotoLauncher.launch(null)
                            } else {
                                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        }) {
                            Text("Cámara")
                        }
                    }
                    if (imagenUri != null) {
                        AsyncImage(
                            model = imagenUri,
                            contentDescription = "Imagen seleccionada desde galería",
                            modifier = Modifier
                                .size(120.dp)
                                .padding(top = 8.dp)
                        )
                    }
                    if (imagenBitmap != null) {
                        Image(
                            bitmap = imagenBitmap!!.asImageBitmap(),
                            contentDescription = "Imagen capturada desde cámara",
                            modifier = Modifier
                                .size(120.dp)
                                .padding(top = 8.dp)
                        )
                    }

                    if (showError && !camposValidos) {
                        Text(
                            "Debes completar todos los campos.",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (camposValidos) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("¡Gracias por contactarnos! Te responderemos pronto.")
                                    nombre = ""
                                    email = ""
                                    mensaje = ""
                                    imagenUri = null
                                    imagenBitmap = null
                                    showError = false
                                }
                            } else {
                                showError = true
                            }
                        },
                        enabled = camposValidos,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar")
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}
