package com.example.lvlup.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// Esta clase sirve tanto para Room como para el JSON del API
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") // Mapea el campo 'id' del JSON
    val id: Int = 0,

    val nombre: String,
    val email: String,
    val pass: String, // ⚠️ OJO: Tu backend puede esperar "password" en lugar de "pass".
    // Si el backend espera "password", añade @SerializedName("password")

    val rol: String = "USER", // Valor por defecto para nuevos usuarios

    // Campos opcionales para la UI de perfil
    val direccion: String? = null,
    val imagen: String? = null
)