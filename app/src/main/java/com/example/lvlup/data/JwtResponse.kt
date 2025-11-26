package com.example.lvlup.data

import com.google.gson.annotations.SerializedName

data class JwtResponse(
    // El ID del usuario/admin que usaremos para mostrar su información en MiCuenta
    @SerializedName("id")
    val userId: Long,

    // El token JWT que debe ser enviado en el encabezado Authorization
    val token: String,

    // El rol del usuario (ej: "USER", "ADMIN")
    val rol: String
)