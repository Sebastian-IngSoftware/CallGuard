package com.callguard.app.ui.blacklist

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.callguard.app.domain.model.BlacklistRule
import com.callguard.app.domain.model.MatchType

@Composable
fun BlacklistScreen(
    viewModel: BlacklistViewModel = hiltViewModel()
) {
    val rules by viewModel.rules.collectAsStateWithLifecycle()
    var pattern by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(MatchType.CONTAINS) }

    Scaffold(topBar = { TopAppBar(title = { Text("Lista negra") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = pattern,
                    onValueChange = { pattern = it },
                    label = { Text("Número o patrón (ej: 999, 51900)") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    viewModel.addRule(pattern, selectedType)
                    pattern = ""
                }) {
                    Text("Agregar")
                }
            }

            Spacer(Modifier.height(8.dp))

            Row {
                MatchType.entries.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type.toLabel()) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            if (rules.isEmpty()) {
                Text(
                    "Todavía no agregaste reglas a tu lista negra.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LazyColumn {
                items(rules, key = { it.id }) { rule ->
                    BlacklistRuleRow(
                        rule = rule,
                        onToggle = { viewModel.toggleRule(rule) },
                        onDelete = { viewModel.deleteRule(rule) }
                    )
                }
            }
        }
    }
}

private fun MatchType.toLabel(): String = when (this) {
    MatchType.CONTAINS -> "Contiene"
    MatchType.STARTS_WITH -> "Empieza con"
    MatchType.ENDS_WITH -> "Termina con"
    MatchType.EXACT -> "Exacto"
}

@Composable
private fun BlacklistRuleRow(
    rule: BlacklistRule,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(rule.pattern, style = MaterialTheme.typography.bodyLarge)
                Text(rule.matchType.toLabel(), style = MaterialTheme.typography.bodySmall)
            }
            Switch(checked = rule.isEnabled, onCheckedChange = { onToggle() })
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar regla")
            }
        }
    }
}
