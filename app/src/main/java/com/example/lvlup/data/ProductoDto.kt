package com.example.lvlup.data

import com.google.gson.annotations.SerializedName
import com.example.lvlup.data.ProductEntity

// Este es el Data Transfer Object (DTO) para recibir datos de tu API de Spring Boot
data class ProductoDto(
    @SerializedName("idProducto")
    val idProducto: Long,
    val nombre: String,
    val descripcion: String,
    @SerializedName("envioGratis")
    val envioGratis: Boolean,
    val precio: Double,
    val descuento: Double,
    val imagen: String? = null,
    val marca: String,
    val categoria: String,
    val juego: String? = null,
    val estado: String? = null,
    val stock: Int
)

fun ProductoDto.toProductEntity(): ProductEntity {
    return ProductEntity(
        // 'id' es autogenerado en ProductEntity, pasamos 0 o el idProducto de la API
        // Dependiendo de cómo CartViewModel maneje la clave. Usaremos el idProducto de la API
        // pero lo convertimos a Int para que coincida con la firma de ProductEntity.
        // Si el ID excede un Int (más de 2 mil millones), esto podría fallar,
        // pero es el mapeo directo más lógico.
        id = this.idProducto.toInt(),
        name = this.nombre,
        category = this.categoria,
        brand = this.marca,
        price = this.precio,
        description = this.descripcion,
        imageUrl = this.imagen,
        imageResId = null, // Valor nulo, ya que viene de la API, no de recursos locales
        discountPercent = this.descuento
    )
}