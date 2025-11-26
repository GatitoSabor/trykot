package com.example.lvlup.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlup.data.AuthDataStore // Gestor de Token/Rol
import com.example.lvlup.data.LoginRequest // DTO para la petición de Login
import com.example.lvlup.data.UserEntity
import com.example.lvlup.network.ProductoApiService // Servicio API para la petición
import com.example.lvlup.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repo: UserRepository,
    private val apiService: ProductoApiService, // Inyectado para hacer la petición de login
    private val authDataStore: AuthDataStore // Inyectado para guardar el token y rol
) : ViewModel() {

    // --- Estado de la UI y de la Sesión ---

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMensaje by mutableStateOf<String?>(null)
        private set

    // Entidad de usuario local (si se usa Room)
    var usuarioActivo by mutableStateOf<UserEntity?>(null)
        private set

    // Estado clave: Rol del usuario (USER o ADMIN)
    var usuarioRole by mutableStateOf<String?>(authDataStore.getRole())
        private set

    // --- Funciones de Modificación de Input ---

    fun onEmailChange(newEmail: String) {
        email = newEmail
        errorMensaje = null
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        errorMensaje = null
    }

    // --- Funciones de Autenticación ---

    fun login(onLoginSuccess: (Long) -> Unit) {
        isLoading = true
        errorMensaje = null

        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)

                // 1. Petición a la API de Spring Boot para autenticarse y obtener el JWT
                val response = apiService.login(request)

                // 2. Guardar el token, el ID del usuario y el rol
                authDataStore.saveAuthData(response.token, response.userId, response.rol)

                // 3. Actualizar el estado del ViewModel
                usuarioRole = response.rol
                // Aquí deberías cargar el UserEntity real si es necesario para MiCuenta,
                // usando el ID del usuario. (Para este ejemplo, solo usamos el ID de la API).

                isLoading = false

                // El Long del ID de usuario se convierte a Int para que coincida con la firma original
                onLoginSuccess(response.userId)

            } catch (e: Exception) {
                // Manejo de errores de red o credenciales
                isLoading = false
                e.printStackTrace()
                // El error 403 o 401 a menudo se muestra como un 'HTTP 401 Unauthorized'
                // por Retrofit/OkHttp si el servidor no devuelve el JSON esperado.
                errorMensaje = "Error al iniciar sesión. Verifica credenciales y conexión."
            }
        }
    }

    fun logout() {
        // Limpiar el estado y los datos de autenticación
        usuarioActivo = null
        usuarioRole = null
        authDataStore.clearAuthData()
        // Limpiar inputs
        email = ""
        password = ""
    }

    // Función de registro (Mantenida por si acaso)
    fun register(nombre: String, email: String, pass: String, onRegisterSuccess: () -> Unit) {
        isLoading = true
        errorMensaje = null

        viewModelScope.launch {
            try {
                // Crear el objeto usuario para enviar
                // Nota: El ID se envía como 0 o null, el backend lo genera.
                // El rol por defecto lo ponemos como USER o ADMIN según quieras probar.
                val nuevoUsuario = UserEntity(
                    nombre = nombre,
                    email = email,
                    pass = pass, // Asegúrate que coincida con el nombre del campo en Backend
                    rol = "USER" // O "ADMIN" si quieres crear un admin de prueba
                )

                // ⚠️ Llamada a la API
                apiService.registrarUsuario(nuevoUsuario)

                isLoading = false
                onRegisterSuccess() // Navegar al login

            } catch (e: Exception) {
                isLoading = false
                e.printStackTrace()
                errorMensaje = "Error al registrar: ${e.localizedMessage}. Intenta con otro correo."
            }
        }
    }
}
