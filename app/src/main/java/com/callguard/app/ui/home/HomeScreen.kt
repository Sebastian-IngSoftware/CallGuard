package com.callguard.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.callguard.app.ui.theme.NeuAccent
import com.callguard.app.ui.theme.NeuBackground
import com.callguard.app.ui.theme.NeuSuccess
import com.callguard.app.ui.theme.NeuText
import com.callguard.app.ui.theme.NeuTextMuted
import com.callguard.app.ui.theme.NeumorphicButton
import com.callguard.app.ui.theme.NeumorphicCard
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Code
import androidx.compose.ui.platform.LocalContext
import com.callguard.app.R
import androidx.compose.ui.res.painterResource

@Composable
fun HomeScreen(
    onNavigateToBlacklist: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToNews: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HomeHeader()

        NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Protección activa",
                    color = NeuText,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "CallGuard está filtrando tus llamadas según tus reglas.",
                    color = NeuTextMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        HomeMenuButton(
            icon = Icons.Filled.Block,
            label = "Lista negra",
            onClick = onNavigateToBlacklist
        )
        HomeMenuButton(
            icon = Icons.Filled.Settings,
            label = "Configuración",
            onClick = onNavigateToSettings
        )

        GithubRepositoryButton()
//        HomeMenuButton(
//            icon = Icons.Filled.NewReleases,
//            label = "Novedades",
//            onClick = onNavigateToNews
//        )
    }
}

@Composable
private fun HomeHeader() {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column {
            Text("Call Guard", color = NeuText, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
private fun HomeMenuButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    NeumorphicButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = NeuAccent)
            Text(label, color = NeuText, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun GithubRepositoryButton() {
    val context = LocalContext.current

    NeumorphicButton(
        onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/Sebastian-IngSoftware/CallGuard")
            )
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_github),
                contentDescription = "GitHub",
                tint = NeuAccent
            )

            Column {
                Text(
                    "Repositorio en GitHub",
                    color = NeuText,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    "Ver código fuente",
                    color = NeuTextMuted,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
