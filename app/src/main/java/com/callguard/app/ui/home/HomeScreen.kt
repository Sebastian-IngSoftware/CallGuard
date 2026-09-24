package com.callguard.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToBlacklist: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToNews: () -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("CallGuard") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Protegé tu teléfono de llamadas no deseadas.",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = onNavigateToBlacklist, modifier = Modifier.fillMaxWidth()) {
                Text("Lista negra")
            }
            Button(onClick = onNavigateToSettings, modifier = Modifier.fillMaxWidth()) {
                Text("Configuración")
            }
            Button(onClick = onNavigateToNews, modifier = Modifier.fillMaxWidth()) {
                Text("Novedades")
            }
        }
    }
}
