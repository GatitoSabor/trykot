package com.example.lvlup.repository

import com.example.lvlup.data.UserDao
import com.example.lvlup.data.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: UserEntity) {
        userDao.insert(user)
    }

    suspend fun getUserById(userId: Int): UserEntity? {
        return userDao.getUserById(userId)
    }

    suspend fun updateUser(user: UserEntity) {
        userDao.update(user)
    }

    // Si tienes lógica de login local antigua, asegúrate de usar 'pass'
    /*
    suspend fun loginLocal(email: String, pass: String): UserEntity? {
        // ⚠️ CORRECCIÓN: Usar el nombre correcto del campo en la entidad
        return userDao.getUserByEmailAndPassword(email, pass)
    }
    */
}