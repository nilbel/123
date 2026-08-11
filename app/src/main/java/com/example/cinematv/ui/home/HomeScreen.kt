package com.example.cinematv.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinematv.data.model.Movie
import com.example.cinematv.data.repository.MovieRepository
import androidx.tv.material3.Button
import androidx.tv.material3.Card
import androidx.tv.material3.Text
import coil.compose.AsyncImage

@Composable
fun HomeScreen(
    repository: MovieRepository,
    onMovieClick: (Movie) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(repository))
) {
    val state by viewModel.state.collectAsState()

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Кинотеатр")
            Row {
                Button(onClick = onSearchClick) { Text("Поиск") }
                Spacer(Modifier.width(12.dp))
                Button(onClick = onSettingsClick) { Text("Настройки") }
            }
        }

        Spacer(Modifier.height(16.dp))

        if (state.errorMessage != null) {
            Text("Ошибка: ${state.errorMessage}. Проверь TMDB API ключ в настройках.")
            return@Column
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            item { MovieRow(title = "В тренде", movies = state.trending, onMovieClick = onMovieClick) }
            item { MovieRow(title = "Популярное", movies = state.popular, onMovieClick = onMovieClick) }
            item { MovieRow(title = "Топ рейтинг", movies = state.topRated, onMovieClick = onMovieClick) }
        }
    }
}

@Composable
private fun MovieRow(title: String, movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    Column {
        Text(title)
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(movies, key = { it.id }) { movie ->
                Card(onClick = { onMovieClick(movie) }) {
                    Column(Modifier.width(140.dp)) {
                        AsyncImage(
                            model = movie.posterUrl,
                            contentDescription = movie.title,
                            modifier = Modifier.width(140.dp).height(200.dp)
                        )
                        Text(movie.title, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}
