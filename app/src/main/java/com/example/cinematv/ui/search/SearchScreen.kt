package com.example.cinematv.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.cinematv.data.model.Movie
import com.example.cinematv.data.repository.MovieRepository
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    repository: MovieRepository,
    onMovieClick: (Movie) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Поиск фильмов")
        Spacer(Modifier.height(12.dp))

        // NOTE: on Android TV, text input is usually driven by a connected keyboard
        // or the on-screen D-pad keyboard triggered by focusing this field.
        androidx.compose.material3.OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                scope.launch {
                    try {
                        results = repository.search(it)
                        error = null
                    } catch (e: Exception) {
                        error = e.message
                    }
                }
            },
            label = { androidx.compose.material3.Text("Название фильма") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        if (error != null) {
            Text("Ошибка: $error")
        }

        LazyVerticalGrid(columns = GridCells.Fixed(6), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(results, key = { it.id }) { movie ->
                Card(onClick = { onMovieClick(movie) }) {
                    Column {
                        AsyncImage(model = movie.posterUrl, contentDescription = movie.title, modifier = Modifier.height(200.dp))
                        Text(movie.title, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}
