package com.example.lvlup.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update // ⚠️ Importar @Update

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    // ⚠️ AGREGAR ESTE MÉTODO
    @Update
    suspend fun update(user: UserEntity)

    // Si tenías login local, asegúrate que coincida con UserEntity
    /*
    @Query("SELECT * FROM users WHERE email = :email AND pass = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?
    */
}