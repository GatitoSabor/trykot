package com.example.lvlup.repository

import com.example.lvlup.data.AddressEntity
import com.example.lvlup.data.UserDao
import com.example.lvlup.data.UserEntity

class UserRepository(private val dao: UserDao) {
    suspend fun login(email: String, password: String): UserEntity? {
        val user = dao.getByEmail(email)
        return if (user?.password == password) user else null
    }

    suspend fun register(user: UserEntity) = dao.insert(user)

    suspend fun getUserById(userId: Int): UserEntity? = dao.getUserById(userId)

    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)

    suspend fun getAddresses(userId: Int): List<AddressEntity> =
        dao.getAddressesForUser(userId)

    suspend fun addAddress(userId: Int, value: String) {
        dao.insertAddress(AddressEntity(userId = userId, value = value))
    }

    suspend fun updateAddress(address: AddressEntity) {
        dao.updateAddress(address)
    }

    suspend fun removeAddress(address: AddressEntity) {
        dao.deleteAddress(address)
    }
}
