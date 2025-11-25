package com.example.lvlup.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lvlup.data.ProductEntity

@Composable
fun ProductItem(product: ProductEntity, onAdd: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name)
            Text(product.category)
            Text("${product.price}")
            Button(onClick = onAdd) { Text("Agregar al carrito") }
        }
    }
}