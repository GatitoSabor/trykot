package com.example.lvlup.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lvlup.ui.login.LoginViewModel

@Composable
fun MainDrawer(
    drawerState: DrawerState,
    navController: NavHostController,
    loginVM: LoginViewModel,
    onInicio: () -> Unit,
    onCatalogo: () -> Unit,
    onMiCuenta: () -> Unit,
    onPuntos: () -> Unit,
    onComunidad: () -> Unit,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = onInicio
                )
                NavigationDrawerItem(
                    label = { Text("Catálogo") },
                    selected = false,
                    onClick = onCatalogo
                )
                NavigationDrawerItem(
                    label = { Text("Puntos") },
                    selected = false,
                    onClick = onPuntos
                )
                NavigationDrawerItem(
                    label = { Text("Comunidad") },
                    selected = false,
                    onClick = onComunidad
                )

                if (loginVM.usuarioActivo == null) {
                    NavigationDrawerItem(
                        label = { Text("Login") },
                        selected = false,
                        onClick = onLogin
                    )
                } else {
                    NavigationDrawerItem(
                        label = { Text("Mi Cuenta") },
                        selected = false,
                        onClick = onMiCuenta
                    )
                    Spacer(Modifier.height(12.dp))
                    NavigationDrawerItem(
                        label = { Text("Cerrar Sesión") },
                        selected = false,
                        onClick = onLogout
                    )
                }

                Spacer(Modifier.weight(1f))
                Divider()

                NavigationDrawerItem(
                    label = { Text("¡Contáctanos!") },
                    selected = false,
                    onClick = {
                        navController.navigate("contacto")
                    }
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp, top = 6.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { uriHandler.openUri("https://www.instagram.com/tuapp") }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Instagram", tint = Color(0xFFE1306C))
                    }
                    IconButton(onClick = { uriHandler.openUri("https://twitter.com/tuapp") }) {
                        Icon(Icons.Default.Share, contentDescription = "Twitter", tint = Color(0xFF1DA1F2))
                    }
                    IconButton(onClick = { uriHandler.openUri("https://www.facebook.com/tuapp") }) {
                        Icon(Icons.Default.Facebook, contentDescription = "Facebook", tint = Color(0xFF4267B2))
                    }
                    IconButton(onClick = { uriHandler.openUri("https://wa.me/56912345678") }) {
                        Icon(Icons.Default.Phone, contentDescription = "WhatsApp", tint = Color(0xFF25D366))
                    }
                }
            }
        },
        drawerState = drawerState,
        content = content
    )
}
