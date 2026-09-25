package com.callguard.app.ui.blacklist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.callguard.app.domain.model.BlacklistRule
import com.callguard.app.domain.model.MatchType
import com.callguard.app.ui.theme.NeuAccent
import com.callguard.app.ui.theme.NeuBackground
import com.callguard.app.ui.theme.NeuBorder
import com.callguard.app.ui.theme.NeuSuccess
import com.callguard.app.ui.theme.NeuTertiary
import com.callguard.app.ui.theme.NeuText
import com.callguard.app.ui.theme.NeuTextMuted
import com.callguard.app.ui.theme.TerminalButton
import com.callguard.app.ui.theme.TerminalHeader
import com.callguard.app.ui.theme.TerminalPanel
import com.callguard.app.ui.theme.TerminalRadioChip
import com.callguard.app.ui.theme.TerminalStatus
import com.callguard.app.ui.theme.terminalScanlines

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
            .terminalScanlines()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TerminalHeader(
            title = "LISTA_NEGRA",
            subtitle = "- patrones de numeros a bloquear"
        )

        TerminalPanel(modifier = Modifier.fillMaxWidth(), contentPadding = 14.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "- PATRON",
                    color = NeuTextMuted,
                    style = MaterialTheme.typography.labelMedium
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeuBackground)
                        .border(1.dp, NeuBorder)
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    if (pattern.isEmpty()) {
                        Text(
                            "INGRESE_PATRON_O_NUMERO...",
                            color = NeuTextMuted.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    BasicTextField(
                        value = pattern,
                        onValueChange = { pattern = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = NeuText,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        ),
                        cursorBrush = SolidColor(NeuAccent),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    "- TIPO_DE_MATCH",
                    color = NeuTextMuted,
                    style = MaterialTheme.typography.labelMedium
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MatchType.entries.take(2).forEach { type ->
                            TerminalRadioChip(
                                label = type.toTerminalLabel(),
                                selected = selectedType == type,
                                onClick = { selectedType = type },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MatchType.entries.drop(2).forEach { type ->
                            TerminalRadioChip(
                                label = type.toTerminalLabel(),
                                selected = selectedType == type,
                                onClick = { selectedType = type },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                TerminalButton(
                    onClick = {
                        viewModel.addRule(pattern, selectedType)
                        pattern = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(
                        "[ + ] AGREGAR_REGLA",
                        color = LocalContentColor.current,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "- ",
                color = NeuAccent,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${rules.size} REGLA(S)",
                color = NeuText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(10.dp))
            TerminalStatus(
                text = "${rules.count { it.isEnabled }} EN_LINEA",
                color = if (rules.any { it.isEnabled }) NeuSuccess else NeuTextMuted
            )
        }

        if (rules.isEmpty()) {
            Text(
                "- SIN_REGLAS. AGREGUE_UN_PATRON_DE_BLOQUEO.",
                color = NeuTextMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(rules, key = { _, rule -> rule.id }) { index, rule ->
                TerminalRuleRow(
                    index = index,
                    rule = rule,
                    onToggle = { viewModel.toggleRule(rule) },
                    onDelete = { viewModel.deleteRule(rule) }
                )
            }
        }
    }
}

private fun MatchType.toTerminalLabel(): String = when (this) {
    MatchType.CONTAINS -> "CONTIENE"
    MatchType.STARTS_WITH -> "EMPIEZA_CON"
    MatchType.ENDS_WITH -> "TERMINA_CON"
    MatchType.EXACT -> "EXACTO"
}

@Composable
private fun TerminalRuleRow(
    index: Int,
    rule: BlacklistRule,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val number = (index + 1).toString().padStart(2, '0')
    val borderColor = if (rule.isEnabled) NeuBorder else NeuTertiary.copy(alpha = 0.35f)

    TerminalPanel(
        modifier = Modifier.fillMaxWidth(),
        borderColor = borderColor,
        contentPadding = 12.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "[$number]",
                        color = NeuAccent.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        rule.pattern,
                        color = if (rule.isEnabled) NeuText else NeuTextMuted.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (rule.isEnabled) "[ON]" else "[OFF]",
                        color = if (rule.isEnabled) NeuSuccess else NeuTextMuted,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(onClick = onToggle)
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
            Spacer(Modifier.height(6.dp))
            Text(
                text = rule.matchType.toTerminalLabel(),
                color = NeuAccent,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}