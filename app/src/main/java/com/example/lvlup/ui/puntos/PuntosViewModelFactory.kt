package com.example.lvlup.ui.puntos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lvlup.repository.CouponRepository

class PuntosViewModelFactory(
    private val couponRepository: CouponRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PuntosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PuntosViewModel(couponRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
