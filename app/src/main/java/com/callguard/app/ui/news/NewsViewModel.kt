package com.callguard.app.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.callguard.app.data.repository.NewsRepository
import com.callguard.app.domain.model.NewsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NewsUiState {
    data object Loading : NewsUiState
    data class Success(val items: List<NewsItem>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            repository.fetchNews()
                .onSuccess { _uiState.value = NewsUiState.Success(it) }
                .onFailure { _uiState.value = NewsUiState.Error(it.message ?: "Error desconocido") }
        }
    }
}
