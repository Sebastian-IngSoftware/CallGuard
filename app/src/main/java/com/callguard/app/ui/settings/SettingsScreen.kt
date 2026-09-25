package com.callguard.app.ui.settings

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.callguard.app.ui.theme.NeuText
import com.callguard.app.ui.theme.NeuBackground
import com.callguard.app.ui.theme.NeuTextMuted
import com.callguard.app.ui.theme.TerminalButton
import com.callguard.app.ui.theme.TerminalCheckRow
import com.callguard.app.ui.theme.TerminalDivider
import com.callguard.app.ui.theme.TerminalHeader
import com.callguard.app.ui.theme.TerminalPanel
import com.callguard.app.ui.theme.terminalScanlines

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val protectionEnabled by viewModel.protectionEnabled.collectAsStateWithLifecycle()
    val blockAllUnknown by viewModel.blockAllUnknown.collectAsStateWithLifecycle()

    val roleRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { /* el usuario aceptó o rechazó asignarnos como app de screening */ }

    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* resultado del permiso READ_CONTACTS */ }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeuBackground)
            .terminalScanlines()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TerminalHeader(
            title = "CONFIGURACION",
            subtitle = "- acceso y comportamiento del filtro"
        )

        TerminalPanel(modifier = Modifier.fillMaxWidth(), contentPadding = 6.dp) {
            Column {
                TerminalCheckRow(
                    label = "PROTECCION_ACTIVA",
                    checked = protectionEnabled,
                    onClick = { viewModel.setProtectionEnabled(!protectionEnabled) }
                )
                TerminalDivider()
                TerminalCheckRow(
                    label = "BLOQUEAR_DESCONOCIDOS",
                    checked = blockAllUnknown,
                    onClick = { viewModel.setBlockAllUnknown(!blockAllUnknown) }
                )
            }
        }

        Text(
            "- android_exige_conceder_acceso_a_contactos_y_asignar_la_app_como_filtro_de_llamadas",
            color = NeuTextMuted,
            style = MaterialTheme.typography.bodySmall
        )

        TerminalActionButton(
            index = 1,
            label = "SOLICITAR_ACCESO_CONTACTOS",
            onClick = { contactsPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS) }
        )

        TerminalActionButton(
            index = 2,
            label = "SER_APP_DE_BLOQUEO",
            onClick = { requestCallScreeningRole(context, roleRequestLauncher) }
        )
    }
}

@Composable
private fun TerminalActionButton(
    index: Int,
    label: String,
    onClick: () -> Unit
) {
    TerminalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "[$index]",
                color = LocalContentColor.current.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = LocalContentColor.current,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun requestCallScreeningRole(
    context: Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Intent>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val roleManager = context.getSystemService(RoleManager::class.java)
        if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_CALL_SCREENING)) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
            launcher.launch(intent)
        }
    }
}