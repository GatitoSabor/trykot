package com.example.lvlup.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.lvlup.data.GamerNews

@Composable
fun ComunidadScreen() {
    val noticias = listOf(
        GamerNews(
            title = "Torneo Nacional de eSports 2025",
            description = "Competidores de todo Chile se enfrentarán en League of Legends, Valorant y más. ¡Premios, sorpresas y streaming para todos!",
            imageUrl = "https://staticctfassetsimg.gamingnetwork.com/tournament.jpg"
        ),
        GamerNews(
            title = "Actualización Fortnite",
            description = "¡Descubre nuevos mapas y modos de juego en el último parche de Fortnite! Disponible desde hoy.",
            imageUrl = "https://staticctfassetsimg.gamingnetwork.com/fortnite_update.jpg"
        )
    )

    val eventos = listOf(
        GamerNews(
            title = "Comunidad Level Up - Meetup Virtual",
            description = "Este domingo a las 21hrs, únete al Discord oficial para charlas y eventos especiales con influencers.",
            imageUrl = "https://staticctfassetsimg.gamingnetwork.com/meetup_gamer.jpg"
        ),
        GamerNews(
            title = "Fiesta Gamer Santiago",
            description = "¡Junta presencial con premios, juegos retro y streaming especial!",
            imageUrl = "https://staticctfassetsimg.gamingnetwork.com/fiesta_gamer.jpg"
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Surface(
            color = Color.White,
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text("Comunidad Gamer", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(16.dp))
                    Text("Noticias", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                }
                items(noticias) { noticia ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(noticia.imageUrl),
                                contentDescription = noticia.title,
                                modifier = Modifier
                                    .size(72.dp)
                                    .padding(end = 12.dp)
                            )
                            Column {
                                Text(noticia.title, style = MaterialTheme.typography.titleMedium)
                                Text(noticia.description, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(20.dp))
                    Text("Eventos", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                }
                items(eventos) { evento ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(evento.imageUrl),
                                contentDescription = evento.title,
                                modifier = Modifier
                                    .size(72.dp)
                                    .padding(end = 12.dp)
                            )
                            Column {
                                Text(evento.title, style = MaterialTheme.typography.titleMedium)
                                Text(evento.description, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
