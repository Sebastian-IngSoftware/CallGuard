package com.callguard.app.ui.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.callguard.app.domain.model.NewsItem

@Composable
fun NewsScreen(viewModel: NewsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { TopAppBar(title = { Text("Novedades") }) }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val current = state) {
                is NewsUiState.Loading -> CircularProgressIndicator()
                is NewsUiState.Error -> Column {
                    Text("No se pudieron cargar las novedades")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = viewModel::loadNews) { Text("Reintentar") }
                }
                is NewsUiState.Success -> NewsList(current.items, onRetry = viewModel::loadNews)
            }
        }
    }
}

@Composable
private fun NewsList(items: List<NewsItem>, onRetry: () -> Unit) {
    if (items.isEmpty()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No hay novedades por ahora")
            Spacer(Modifier.height(8.dp))
            Button(onClick = onRetry) { Text("Actualizar") }
        }
        return
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(items, key = { it.id }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(item.body, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(item.publishedAt, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
