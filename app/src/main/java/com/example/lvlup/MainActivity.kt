package com.example.lvlup

import ProductListViewModel // Asegúrate que el import corresponde al paquete correcto
import com.example.lvlup.ui.micuenta.ProfileViewModel // Importación añadida
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lvlup.data.DatabaseProvider
import com.example.lvlup.repository.UserRepository
import com.example.lvlup.repository.CouponRepository
import com.example.lvlup.ui.cart.CartViewModel
import com.example.lvlup.ui.cart.CartViewModelFactory
import com.example.lvlup.ui.home.ProductListViewModelFactory
import com.example.lvlup.ui.login.LoginViewModel
import com.example.lvlup.ui.login.LoginViewModelFactory
import com.example.lvlup.ui.theme.LvlUpTheme
import com.example.lvlup.ui.micuenta.ProfileViewModelFactory
import com.example.lvlup.ui.puntos.PuntosViewModelFactory
import com.example.lvlup.data.AuthDataStore
import com.example.lvlup.network.RetrofitClient
import com.example.lvlup.util.MainAppWithDrawer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicializar AuthDataStore PRIMERO
        val authDataStore = AuthDataStore(applicationContext)

        // 2. Configurar RetrofitClient con el AuthDataStore
        RetrofitClient.authDataStore = authDataStore

        // 3. Obtener el ID del usuario de forma segura
        val usuarioIdGuardado = authDataStore.getUserId().toInt()

        // 4. Inicializar Base de Datos y Repositorios
        val db = DatabaseProvider.getInstance(applicationContext)
        val userRepo = UserRepository(db.userDao())
        val couponRepo = CouponRepository(db.couponDao())

        setContent {
            LvlUpTheme {
                val navController = androidx.navigation.compose.rememberNavController()

                // 5. Crear LoginViewModel pasando las dependencias requeridas
                val loginVM: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(
                        userRepo,
                        RetrofitClient.productoApiService,
                        authDataStore
                    )
                )

                // Instancia ProductListViewModel
                val productListVM: ProductListViewModel = viewModel(
                    factory = ProductListViewModelFactory(applicationContext)
                )

                val cartVM: CartViewModel = viewModel(factory = CartViewModelFactory())
                val puntosVM: com.example.lvlup.ui.puntos.PuntosViewModel = viewModel(factory = PuntosViewModelFactory(couponRepo))

                // ⚠️ CORREGIDO: Quita couponRepo, solo pasa los argumentos que pide el factory
                val miCuentaVM: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(userRepo, usuarioIdGuardado)
                )

                MainAppWithDrawer(
                    navController = navController,
                    loginVM = loginVM,
                    productListVM = productListVM,
                    cartVM = cartVM,
                    puntosVM = puntosVM,
                    miCuentaVM = miCuentaVM,
                    usuarioIdGuardado = usuarioIdGuardado
                )
            }
        }
    }
}
