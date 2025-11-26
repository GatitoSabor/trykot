package com.example.lvlup.network

import com.example.lvlup.data.JwtResponse
import com.example.lvlup.data.LoginRequest
import com.example.lvlup.data.ProductoDto
import com.example.lvlup.data.UserEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProductoApiService {
    // ⚠️ Estos nombres deben coincidir con las llamadas en ProductRepository
    @GET("api/v1/producto")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/v1/producto/categoria/{categoria}")
    suspend fun getProductosPorCategoria(@Path("categoria") categoria: String): List<ProductoDto>

    @POST("api/v1/usuario/login")
    suspend fun login(@Body request: LoginRequest): JwtResponse

    @POST("api/v1/usuario/registro")
    suspend fun registrarUsuario(@Body usuario: UserEntity): UserEntity

    @Multipart
    @POST("api/v1/producto")
    suspend fun crearProducto(
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("categoria") categoria: RequestBody,
        @Part("marca") marca: RequestBody,
        @Part("descuento") descuento: RequestBody,
        @Part("envioGratis") envioGratis: RequestBody,
        @Part("juego") juego: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): ProductoDto

    @PUT("api/v1/producto/{idProducto}")
    suspend fun actualizarProducto(
        @Path("idProducto") idProducto: Long,
        @Body productoDto: ProductoDto
    ): ProductoDto

    @DELETE("api/v1/producto/{idProducto}")
    suspend fun eliminarProducto(
        @Path("idProducto") idProducto: Long
    ): Unit
}