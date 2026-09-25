package com.callguard.app.ui.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.callguard.app.domain.model.NewsItem
import com.callguard.app.ui.theme.NeuAccent
import com.callguard.app.ui.theme.NeuBackground
import com.callguard.app.ui.theme.NeuText
import com.callguard.app.ui.theme.NeuTextMuted
import com.callguard.app.ui.theme.TerminalButton
import com.callguard.app.ui.theme.TerminalCursor
import com.callguard.app.ui.theme.TerminalHeader
import com.callguard.app.ui.theme.TerminalPanel
import com.callguard.app.ui.theme.terminalScanlines

@Composable
fun NewsScreen(viewModel: NewsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuBackground)
            .terminalScanlines()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TerminalHeader(
            title = "NOVEDADES",
            subtitle = "- ultimo.release.log"
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (val current = state) {
                is NewsUiState.Loading -> Loading()
                is NewsUiState.Error -> ErrorState(
                    message = current.message,
                    onRetry = viewModel::loadNews
                )
                is NewsUiState.Success -> NewsList(
                    items = current.items,
                    onRetry = viewModel::loadNews
                )
            }
        }
    }
}

@Composable
private fun Loading() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "- ",
            color = NeuAccent,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            "CARGANDO_DATOS",
            color = NeuTextMuted,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.width(4.dp))
        TerminalCursor()
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "- ERROR: $message",
            color = NeuAccent,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(14.dp))
        TerminalButton(onClick = onRetry) {
            Text(
                "[REINTENTAR]",
                color = LocalContentColor.current,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun NewsList(items: List<NewsItem>, onRetry: () -> Unit) {
    if (items.isEmpty()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "- SIN_DATOS_DISPONIBLES",
                color = NeuTextMuted,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(14.dp))
            TerminalButton(onClick = onRetry) {
                Text(
                    "[ACTUALIZAR]",
                    color = LocalContentColor.current,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items, key = { it.id }) { item ->
            TerminalPanel(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        "[DATE] ${item.publishedAt}",
                        color = NeuAccent,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        item.title,
                        color = NeuText,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        item.body,
                        color = NeuTextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}