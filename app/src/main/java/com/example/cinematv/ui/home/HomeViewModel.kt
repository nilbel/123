package com.example.cinematv.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cinematv.data.model.Movie
import com.example.cinematv.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val trending: List<Movie> = emptyList(),
    val popular: List<Movie> = emptyList(),
    val topRated: List<Movie> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                val trending = repository.trending()
                val popular = repository.popular()
                val topRated = repository.topRated()
                _state.value = HomeState(trending = trending, popular = popular, topRated = topRated)
            } catch (e: Exception) {
                _state.value = HomeState(errorMessage = e.message)
            }
        }
    }

    companion object {
        fun factory(repository: MovieRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                HomeViewModel(repository) as T
        }
    }
}
