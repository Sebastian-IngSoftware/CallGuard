package com.callguard.app.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.callguard.app.R
import com.callguard.app.ui.theme.NeuAccent
import com.callguard.app.ui.theme.NeuBackground
import com.callguard.app.ui.theme.NeuSuccess
import com.callguard.app.ui.theme.NeuText
import com.callguard.app.ui.theme.NeuTextMuted
import com.callguard.app.ui.theme.TerminalButton
import com.callguard.app.ui.theme.TerminalCursor
import com.callguard.app.ui.theme.TerminalHeader
import com.callguard.app.ui.theme.TerminalPanel
import com.callguard.app.ui.theme.terminalScanlines

@Composable
fun HomeScreen(
    onNavigateToBlacklist: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToNews: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuBackground)
            .terminalScanlines()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TerminalHeader(
            title = "CALL_GUARD",
        )

        TerminalPanel(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                StatusLine("ESTADO", "ACTIVO", NeuSuccess)
                StatusLine("MODO", "FILTRADO_AUTOMATICO", NeuText)
                StatusLine("VERSION", "1.0.0", NeuSuccess)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "- ",
                        color = NeuAccent,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "BLOQUEANDO_LLAMADAS",
                        color = NeuTextMuted,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.width(2.dp))
                    TerminalCursor()
                }
            }
        }

        TerminalMenuButton(
            index = 1,
            label = "LISTA_NEGRA",
            onClick = onNavigateToBlacklist,
            icon = { Icon(imageVector = Icons.Filled.Block, contentDescription = null) }
        )

        TerminalMenuButton(
            index = 2,
            label = "CONFIGURACION",
            onClick = onNavigateToSettings,
            icon = { Icon(imageVector = Icons.Filled.Settings, contentDescription = null) }
        )

        TerminalMenuButton(
            index = 3,
            label = "REPOSITORIO_GITHUB",
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/Sebastian-IngSoftware/CallGuard")
                )
                context.startActivity(intent)
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = "GitHub"
                )
            }
        )
//        TerminalMenuButton(
//            index = 4,
//            label = "NOVEDADES",
//            onClick = onNavigateToNews,
//            icon = { Icon(imageVector = Icons.Filled.NewReleases, contentDescription = null) }
//        )
    }
}

@Composable
private fun StatusLine(label: String, value: String, valueColor: Color) {
    Row {
        Text(
            text = "$label : ",
            color = NeuTextMuted,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "[ $value ]",
            color = valueColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TerminalMenuButton(
    index: Int,
    label: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    val number = index.toString().padStart(2, '0')
    TerminalButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "[$number]",
                color = LocalContentColor.current.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            icon()
            Text(
                text = label,
                color = LocalContentColor.current,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "->",
                color = LocalContentColor.current.copy(alpha = 0.5f),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}