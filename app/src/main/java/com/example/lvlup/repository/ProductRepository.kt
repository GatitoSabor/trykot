package com.example.lvlup.repository

import com.example.lvlup.data.ProductEntity
import com.example.lvlup.data.ProductDao
import com.example.lvlup.data.ProductoDto
import com.example.lvlup.network.ProductoApiService
import com.example.lvlup.network.BASE_URL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import java.io.File

class ProductRepository(
    private val productDao: ProductDao,
    private val apiService: ProductoApiService
) {
    private val baseUrl = BASE_URL

    // --- Helpers para URLs ---
    private fun ProductoDto.withFullImageUrl(): ProductoDto {
        return if (this.imagen != null && this.imagen.startsWith("/uploads/")) {
            this.copy(imagen = baseUrl + this.imagen.removePrefix("/"))
        } else {
            this
        }
    }

    private fun List<ProductoDto>.withFullImageUrls(): List<ProductoDto> {
        return this.map { it.withFullImageUrl() }
    }

    // --- Obtención de Productos ---
    fun getProducts(): Flow<List<ProductoDto>> = flow {
        try {
            val productos = apiService.getProductos()
            emit(productos.withFullImageUrls())
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    fun getProductsByCategory(category: String): Flow<List<ProductoDto>> = flow {
        try {
            val productos = apiService.getProductosPorCategoria(category)
            emit(productos.withFullImageUrls())
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // --- Funciones CRUD ---
    suspend fun crearProducto(
        nombre: String, descripcion: String, precio: Double, categoria: String, marca: String,
        descuento: Double = 0.0, envioGratis: Boolean = false, juego: String = "", imagen: File? = null
    ): Result<ProductoDto> = withContext(Dispatchers.IO) {
        try {
            val mediaType = "text/plain".toMediaTypeOrNull()
            val nombrePart = nombre.toRequestBody(mediaType)
            val descripcionPart = descripcion.toRequestBody(mediaType)
            val precioPart = precio.toString().toRequestBody(mediaType)
            val categoriaPart = categoria.toRequestBody(mediaType)
            val marcaPart = marca.toRequestBody(mediaType)
            val descuentoPart = descuento.toString().toRequestBody(mediaType)
            val envioGratisPart = envioGratis.toString().toRequestBody(mediaType)
            val juegoPart = juego.toRequestBody(mediaType)

            var imagenPart: MultipartBody.Part? = null
            if (imagen != null) {
                val requestFile = okhttp3.RequestBody.create("image/*".toMediaTypeOrNull(), imagen)
                imagenPart = MultipartBody.Part.createFormData("imagen", imagen.name, requestFile)
            }

            val productoCreado = apiService.crearProducto(
                nombrePart, descripcionPart, precioPart, categoriaPart, marcaPart,
                descuentoPart, envioGratisPart, juegoPart, imagenPart
            )
            Result.success(productoCreado.withFullImageUrl())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun actualizarProducto(productoActualizado: ProductoDto): Result<ProductoDto> = withContext(Dispatchers.IO) {
        try {
            val productoActualizadoResponse = apiService.actualizarProducto(productoActualizado.idProducto, productoActualizado)
            Result.success(productoActualizadoResponse.withFullImageUrl())
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun eliminarProducto(idProducto: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.eliminarProducto(idProducto)
            Result.success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // --- Room (Local) ---
    suspend fun insertAllDemo(productos: List<ProductEntity>) {
        productDao.insertAll(*productos.toTypedArray())
    }
}