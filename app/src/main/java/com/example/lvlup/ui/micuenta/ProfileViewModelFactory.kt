package com.example.lvlup.ui.micuenta

import ProfileViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lvlup.repository.UserRepository
import com.example.lvlup.repository.CouponRepository

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val couponRepository: CouponRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userRepository, couponRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
