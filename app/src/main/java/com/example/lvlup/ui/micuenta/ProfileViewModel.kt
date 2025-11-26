package com.example.lvlup.ui.micuenta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlup.data.UserEntity
import com.example.lvlup.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val currentUserId: Int
) : ViewModel() {

    private val _usuario = MutableStateFlow<UserEntity?>(null)
    val usuario: StateFlow<UserEntity?> = _usuario.asStateFlow()

    init {
        if (currentUserId != -1) {
            cargarUsuarioCompleto(currentUserId)
        }
    }

    fun cargarUsuarioCompleto(userId: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            _usuario.value = user
        }
    }

    fun cargarUsuarioCompleto(userId: Long) {
        cargarUsuarioCompleto(userId.toInt())
    }
}
