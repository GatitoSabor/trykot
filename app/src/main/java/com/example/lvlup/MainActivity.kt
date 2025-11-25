package com.example.lvlup

import ProductListViewModel
import ProfileViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lvlup.data.DatabaseProvider
import com.example.lvlup.data.productosDemo
import com.example.lvlup.repository.UserRepository
import com.example.lvlup.repository.ProductRepository
import com.example.lvlup.repository.CouponRepository
import kotlinx.coroutines.flow.first
import com.example.lvlup.ui.cart.CartViewModel
import com.example.lvlup.ui.cart.CartViewModelFactory
import com.example.lvlup.ui.home.ProductListViewModelFactory
import com.example.lvlup.ui.login.LoginViewModel
import com.example.lvlup.ui.login.LoginViewModelFactory
import com.example.lvlup.ui.theme.LvlUpTheme
import com.example.lvlup.ui.micuenta.ProfileViewModelFactory
import com.example.lvlup.util.MainAppWithDrawer
import com.example.lvlup.ui.puntos.PuntosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getInstance(applicationContext)
        val userRepo = UserRepository(db.userDao())
        val productRepo = ProductRepository(db.productDao())
        val couponRepo = CouponRepository(db.couponDao())

        setContent {
            LvlUpTheme {
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    val productosExistentes = productRepo.getProducts().first()
                    if (productosExistentes.isEmpty()) {
                        productRepo.insertAllDemo(productosDemo)
                    }
                }

                val navController = androidx.navigation.compose.rememberNavController()
                val loginVM: LoginViewModel = viewModel(factory = LoginViewModelFactory(userRepo))
                val productListVM: ProductListViewModel = viewModel(factory = ProductListViewModelFactory(applicationContext))
                val cartVM: CartViewModel = viewModel(factory = CartViewModelFactory())
                val puntosVM: com.example.lvlup.ui.puntos.PuntosViewModel = viewModel(factory = PuntosViewModelFactory(couponRepo))

                val prefs = getSharedPreferences("lvlup_prefs", MODE_PRIVATE)
                val usuarioIdGuardado = prefs.getInt("usuario_id", -1)

                val miCuentaVM: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(userRepo, couponRepo, usuarioIdGuardado)
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
