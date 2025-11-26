package com.example.lvlup.ui.micuenta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lvlup.repository.UserRepository

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
