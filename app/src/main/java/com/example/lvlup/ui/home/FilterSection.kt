package com.example.lvlup.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun FilterSection(categories: List<String>, onCategoryChange: (String) -> Unit) {
    Row {
        categories.forEach { cat ->
            Button(onClick = { onCategoryChange(cat) }) { Text(cat) }
        }
    }
}