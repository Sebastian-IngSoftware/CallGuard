package com.callguard.app.ui.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.callguard.app.domain.model.NewsItem
import com.callguard.app.ui.theme.NeuAccent
import com.callguard.app.ui.theme.NeuBackground
import com.callguard.app.ui.theme.NeuText
import com.callguard.app.ui.theme.NeuTextMuted
import com.callguard.app.ui.theme.NeumorphicButton
import com.callguard.app.ui.theme.NeumorphicCard

@Composable
fun NewsScreen(viewModel: NewsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuBackground)
            .padding(20.dp)
    ) {
        Text("Novedades", color = NeuText, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (val current = state) {
                is NewsUiState.Loading -> CircularProgressIndicator(color = NeuAccent)
                is NewsUiState.Error -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No se pudieron cargar las novedades", color = NeuTextMuted)
                    Spacer(Modifier.height(12.dp))
                    NeumorphicButton(onClick = viewModel::loadNews) {
                        Text("Reintentar", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
                    }
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
            Text("No hay novedades por ahora", color = NeuTextMuted)
            Spacer(Modifier.height(12.dp))
            NeumorphicButton(onClick = onRetry) {
                Text("Actualizar", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
            }
        }
        return
    }
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items, key = { it.id }) { item ->
            NeumorphicCard(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(item.title, color = NeuText, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(item.body, color = NeuTextMuted, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(item.publishedAt, color = NeuAccent, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
