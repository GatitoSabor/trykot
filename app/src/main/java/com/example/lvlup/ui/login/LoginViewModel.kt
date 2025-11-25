package com.example.lvlup.ui.login

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlup.data.UserEntity
import com.example.lvlup.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepository): ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loginSuccess by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)

    var usuarioActivo by mutableStateOf<UserEntity?>(null)

    fun login(onResult: (UserEntity?) -> Unit = {}) = viewModelScope.launch {
        val user = repo.login(email, password)
        loginSuccess = user != null
        loginError = if (user == null) "Credenciales inválidas" else null
        usuarioActivo = user
        onResult(user)
    }

    fun registerNewUser(username: String, email: String, password: String, onResult: (UserEntity?) -> Unit = {}) {
        viewModelScope.launch {
            val user = UserEntity(nombre = username, email = email, password = password)
            repo.register(user)
            val usuarioRegistrado = repo.login(email, password)
            usuarioActivo = usuarioRegistrado
            onResult(usuarioRegistrado)
        }
    }

    fun guardarSesion(context: Context, usuarioId: Int) {
        val prefs = context.getSharedPreferences("lvlup_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("usuario_id", usuarioId).apply()
    }

    fun logout() {
        usuarioActivo = null
        loginSuccess = false
        loginError = null
        email = ""
        password = ""
    }
}
