package com.callguard.app.ui.blacklist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.callguard.app.domain.model.BlacklistRule
import com.callguard.app.domain.model.MatchType
import com.callguard.app.ui.theme.NeuAccent
import com.callguard.app.ui.theme.NeuBackground
import com.callguard.app.ui.theme.NeuSuccess
import com.callguard.app.ui.theme.NeuSurface
import com.callguard.app.ui.theme.NeuText
import com.callguard.app.ui.theme.NeuTextMuted
import com.callguard.app.ui.theme.NeumorphicButton
import com.callguard.app.ui.theme.NeumorphicCard
import com.callguard.app.ui.theme.NeumorphicInset

@Composable
fun BlacklistScreen(
    viewModel: BlacklistViewModel = hiltViewModel()
) {
    val rules by viewModel.rules.collectAsStateWithLifecycle()
    var pattern by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(MatchType.CONTAINS) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuBackground)
            .padding(20.dp)
    ) {
        Text("Lista negra", color = NeuText, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Número o patrón",
                    color = NeuTextMuted,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    NeumorphicInset(modifier = Modifier.weight(1f)) {
                        BasicTextField(
                            value = pattern,
                            onValueChange = { pattern = it },
                            singleLine = true,
                            textStyle = TextStyle(color = NeuText, fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                            cursorBrush = androidx.compose.ui.graphics.SolidColor(NeuAccent),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    NeumorphicButton(
                        onClick = {
                            viewModel.addRule(pattern, selectedType)
                            pattern = ""
                        },
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar regla")
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MatchType.entries.forEach { type ->
                        MatchTypeChip(
                            label = type.toLabel(),
                            selected = selectedType == type,
                            onClick = { selectedType = type }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (rules.isEmpty()) {
            Text(
                "Todavía no agregaste reglas a tu lista negra.",
                color = NeuTextMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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

@Composable
private fun MatchTypeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    if (selected) {
        NeumorphicButton(
            onClick = onClick,
            contentColor = NeuSuccess
        ) {
            Text(
                label,
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 8.dp
                ),
                style = MaterialTheme.typography.labelMedium
            )
        }
    } else {
        NeumorphicInset(
            modifier = Modifier.clickable(onClick = onClick)
        ) {
            Text(
                label,
                color = NeuTextMuted,
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 8.dp
                ),
                style = MaterialTheme.typography.labelMedium
            )
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
    NeumorphicCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 16.dp, elevation = 8.dp) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(rule.pattern, color = NeuText, style = MaterialTheme.typography.bodyLarge)
                Text(
                    rule.matchType.toLabel(),
                    color = NeuAccent,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(
                checked = rule.isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeuAccent,
                    checkedTrackColor = NeuSurface,
                    uncheckedThumbColor = NeuTextMuted,
                    uncheckedTrackColor = NeuSurface
                )
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar regla",
                    tint = NeuTextMuted
                )
            }
        }
    }
}