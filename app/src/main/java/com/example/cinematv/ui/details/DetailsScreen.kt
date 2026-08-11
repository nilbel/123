package com.example.cinematv.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.cinematv.data.repository.MovieRepository
import com.example.cinematv.data.repository.TorrentRepository

@Composable
fun DetailsScreen(
    movieId: Int,
    movieRepository: MovieRepository,
    torrentRepository: TorrentRepository,
    onPlay: (String) -> Unit,
    viewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModel.factory(movieId, movieRepository, torrentRepository)
    )
) {
    val state by viewModel.state.collectAsState()
    var magnetInput by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        val details = state.details
        if (details == null) {
            Text(state.errorMessage ?: "Загрузка...")
            return@Column
        }

        Row {
            AsyncImage(
                model = details.posterUrl,
                contentDescription = details.title,
                modifier = Modifier.width(220.dp).height(320.dp)
            )
            Spacer(Modifier.width(24.dp))
            Column(Modifier.weight(1f)) {
                Text(details.title)
                Text("${details.releaseDate?.take(4) ?: ""} · ${details.runtime ?: "?"} мин · ★ ${details.voteAverage ?: "-"}")
                Spacer(Modifier.height(8.dp))
                Text(details.genres.joinToString(", ") { it.name })
                Spacer(Modifier.height(12.dp))
                Text(details.overview ?: "")

                Spacer(Modifier.height(16.dp))
                Text("Актёры:")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(details.credits?.cast?.take(15) ?: emptyList(), key = { it.id }) { actor ->
                        Column(Modifier.width(90.dp)) {
                            AsyncImage(
                                model = actor.photoUrl,
                                contentDescription = actor.name,
                                modifier = Modifier.width(90.dp).height(120.dp)
                            )
                            Text(actor.name)
                            actor.character?.let { Text(it) }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Placeholder for a real tracker-parser module: for now the user pastes
                // a magnet link (e.g. found manually, or later fed in automatically by a parser)
                // and it's handed straight to their TorrServer instance.
                androidx.compose.material3.OutlinedTextField(
                    value = magnetInput,
                    onValueChange = { magnetInput = it },
                    label = { androidx.compose.material3.Text("Magnet-ссылка") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = { viewModel.resolveAndPlay(magnetInput, onPlay) }) {
                    Text(if (state.isResolving) "Подключение..." else "Смотреть")
                }
                state.torrentError?.let { Text("Ошибка TorrServer: $it") }
            }
        }
    }
}
