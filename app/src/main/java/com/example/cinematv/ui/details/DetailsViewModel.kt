package com.example.cinematv.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinematv.data.model.MovieDetails
import com.example.cinematv.data.repository.MovieRepository
import com.example.cinematv.data.repository.TorrentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DetailsState(
    val details: MovieDetails? = null,
    val errorMessage: String? = null,
    val isResolving: Boolean = false,
    val torrentError: String? = null
)

class DetailsViewModel(
    private val movieId: Int,
    private val movieRepository: MovieRepository,
    private val torrentRepository: TorrentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state

    init {
        viewModelScope.launch {
            try {
                _state.value = DetailsState(details = movieRepository.details(movieId))
            } catch (e: Exception) {
                _state.value = DetailsState(errorMessage = e.message)
            }
        }
    }

    fun resolveAndPlay(magnetLink: String, onResolved: (String) -> Unit) {
        if (magnetLink.isBlank()) {
            _state.value = _state.value.copy(torrentError = "Вставь magnet-ссылку")
            return
        }
        viewModelScope.launch {
            _state.value = _state.value.copy(isResolving = true, torrentError = null)
            try {
                val streamUrl = torrentRepository.resolveStreamUrl(magnetLink)
                onResolved(streamUrl)
            } catch (e: Exception) {
                _state.value = _state.value.copy(torrentError = e.message)
            } finally {
                _state.value = _state.value.copy(isResolving = false)
            }
        }
    }

    companion object {
        fun factory(movieId: Int, movieRepository: MovieRepository, torrentRepository: TorrentRepository) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    DetailsViewModel(movieId, movieRepository, torrentRepository) as T
            }
    }
}
