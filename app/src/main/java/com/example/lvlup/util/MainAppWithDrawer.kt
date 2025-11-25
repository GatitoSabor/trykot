package com.example.lvlup.util

import ProductListViewModel
import ProfileViewModel
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lvlup.screens.ComunidadScreen
import com.example.lvlup.screens.ContactoScreen
import com.example.lvlup.ui.adminproductos.AdminProductosScreen
import com.example.lvlup.ui.adminproductos.AdminProductosViewModel
import com.example.lvlup.ui.cart.CartScreen
import com.example.lvlup.ui.cart.CartViewModel
import com.example.lvlup.ui.home.HomeScreen
import com.example.lvlup.ui.login.LoginScreen
import com.example.lvlup.ui.login.LoginViewModel
import com.example.lvlup.ui.login.RegisterScreen
import com.example.lvlup.ui.puntos.PuntosStoreScreen
import com.example.lvlup.ui.puntos.PuntosViewModel
import kotlinx.coroutines.launch
import com.example.lvlup.ui.micuenta.MiCuentaScreen
import com.example.lvlup.ui.home.InicioScreen
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.example.lvlup.data.AppDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppWithDrawer(
    navController: NavHostController,
    loginVM: LoginViewModel,
    productListVM: ProductListViewModel,
    cartVM: CartViewModel,
    puntosVM: PuntosViewModel,
    miCuentaVM: ProfileViewModel,
    usuarioIdGuardado: Int
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    val uriHandler = LocalUriHandler.current
    val productosEnOferta by productListVM.productsFlow.collectAsState(initial = emptyList())

    val showDrawer = currentRoute != "login" && currentRoute != "register"

    val context = LocalContext.current

    if (showDrawer) {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("inicio")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Catálogo") }, selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("home")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Puntos") }, selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("puntos")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Comunidad") }, selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("comunidad")
                        }
                    )

                    if (loginVM.usuarioActivo == null) {
                        NavigationDrawerItem(
                            label = { Text("Login") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("login")
                            }
                        )
                    } else {
                        NavigationDrawerItem(
                            label = { Text("Mi Cuenta") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("micuenta")
                            }
                        )
                        NavigationDrawerItem(
                            label = { Text("Vista Administrador") },
                            selected = false,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate("adminproductos")
                            }
                        )
                        Spacer(Modifier.height(24.dp))
                        NavigationDrawerItem(
                            label = { Text("Cerrar Sesión") },
                            selected = false,
                            onClick = {
                                val prefs = context.getSharedPreferences("lvlup_prefs", android.content.Context.MODE_PRIVATE)
                                prefs.edit().remove("usuario_id").apply()
                                loginVM.logout()
                                scope.launch { drawerState.close() }
                                navController.navigate("login") {
                                    popUpTo("inicio") { inclusive = false }
                                }
                            }
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("¡Contáctanos!") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("contacto")
                        }
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp, top = 12.dp),
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
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.open() }
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            navigationIconContentColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    loginVM = loginVM,
                    productListVM = productListVM,
                    cartVM = cartVM,
                    puntosVM = puntosVM,
                    miCuentaVM = miCuentaVM,
                    productosEnOferta = productosEnOferta,
                    usuarioIdGuardado = usuarioIdGuardado,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    } else {
        AppNavHost(
            navController = navController,
            loginVM = loginVM,
            productListVM = productListVM,
            cartVM = cartVM,
            puntosVM = puntosVM,
            miCuentaVM = miCuentaVM,
            productosEnOferta = productosEnOferta,
            usuarioIdGuardado = usuarioIdGuardado
        )
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginVM: LoginViewModel,
    productListVM: ProductListViewModel,
    cartVM: CartViewModel,
    puntosVM: PuntosViewModel,
    miCuentaVM: ProfileViewModel,
    productosEnOferta: List<com.example.lvlup.data.ProductEntity>,
    usuarioIdGuardado: Int,
    modifier: Modifier = Modifier
) {
    val startScreen = if (usuarioIdGuardado != -1) "inicio" else "login"

    NavHost(
        navController = navController,
        startDestination = startScreen,
        modifier = modifier
    ) {
        composable("inicio") {
            InicioScreen(
                productosEnOferta = productosEnOferta.filter { (it.discountPercent ?: 0.0) > 0.0 },
                onGoCatalogo = { navController.navigate("home") },
                onGoComunidad = { navController.navigate("comunidad") }
            )
        }
        composable("login") {
            LoginScreen(
                viewModel = loginVM,
                onLoginSuccess = { userId ->
                    miCuentaVM.cargarUsuarioCompleto(userId)
                    navController.navigate("inicio")
                },
                onRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = loginVM,
                onBack = { navController.navigate("login") },
                onLogin = { navController.navigate("login") }
            )
        }
        composable("home") {
            HomeScreen(
                productListViewModel = productListVM,
                cartViewModel = cartVM,
                goToCart = { navController.navigate("cart") }
            )
        }
        composable("cart") {
            CartScreen(
                viewModel = cartVM,
                onBack = { navController.popBackStack() }
            )
        }
        composable("comunidad") {
            ComunidadScreen()
        }
        composable("puntos") {
            PuntosStoreScreen(
                puntosViewModel = puntosVM,
                userId = loginVM.usuarioActivo?.id ?: 0,
                onBack = { navController.popBackStack() }
            )
        }
        composable("micuenta") {
            MiCuentaScreen(
                viewModel = miCuentaVM,
                usuarioId = loginVM.usuarioActivo?.id,
                onBack = { navController.popBackStack() }
            )
        }
        composable("contacto") {
            ContactoScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("adminproductos") {
            val context = LocalContext.current.applicationContext
            val db = remember {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "lvlup_db"
                ).fallbackToDestructiveMigration().build()
            }
            val adminVM = remember { AdminProductosViewModel(db.productDao()) }
            AdminProductosScreen(adminVM)
        }
    }
}
