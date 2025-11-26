package com.example.lvlup.ui.adminproductos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lvlup.data.ProductoDto
import com.example.lvlup.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class AdminProductosViewModel(private val repository: ProductRepository) : ViewModel() {

    // Lista de productos actualizada desde el repositorio (Red)
    val uiState: StateFlow<List<ProductoDto>> = repository.getProducts()
        .map { it.sortedBy { dto -> dto.idProducto } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var estadoMensaje by mutableStateOf<String?>(null)
        private set

    var productoSeleccionado by mutableStateOf<ProductoDto?>(null)
        private set

    fun seleccionarProducto(producto: ProductoDto) {
        productoSeleccionado = producto
    }

    fun limpiarSeleccion() {
        productoSeleccionado = null
    }

    fun crearProducto(
        nombre: String, descripcion: String, precio: Double, categoria: String, marca: String,
        descuento: Double = 0.0, envioGratis: Boolean = false, juego: String = "", imagen: File? = null
    ) {
        viewModelScope.launch {
            val result = repository.crearProducto(
                nombre, descripcion, precio, categoria, marca,
                descuento, envioGratis, juego, imagen
            )
            result.onSuccess {
                estadoMensaje = "Producto '${it.nombre}' creado exitosamente."
            }.onFailure {
                estadoMensaje = "Error al crear el producto: ${it.localizedMessage}. (Revise la consola del servidor)"
            }
        }
    }

    fun actualizarProducto(productoActualizado: ProductoDto) {
        viewModelScope.launch {
            val result = repository.actualizarProducto(productoActualizado)
            result.onSuccess {
                estadoMensaje = "Producto '${it.nombre}' actualizado exitosamente."
                limpiarSeleccion()
            }.onFailure {
                estadoMensaje = "Error al actualizar el producto: ${it.localizedMessage}"
            }
        }
    }

    fun eliminarProducto(idProducto: Long) {
        viewModelScope.launch {
            val result = repository.eliminarProducto(idProducto)
            result.onSuccess {
                estadoMensaje = "Producto ID $idProducto eliminado exitosamente."
            }.onFailure {
                estadoMensaje = "Error al eliminar el producto: ${it.localizedMessage}"
            }
        }
    }

    fun mensajeMostrado() {
        estadoMensaje = null
    }
}