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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.PhoneDisabled
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.callguard.app.ui.theme.NeuAccent
import com.callguard.app.ui.theme.NeuBackground
import com.callguard.app.ui.theme.NeuSurface
import com.callguard.app.ui.theme.NeuText
import com.callguard.app.ui.theme.NeuTextMuted
import com.callguard.app.ui.theme.NeumorphicButton
import com.callguard.app.ui.theme.NeumorphicCard

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
            .padding(20.dp)
    ) {
        Text("Configuración", color = NeuText, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))

        NeumorphicCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(4.dp)) {
                SettingRow(
                    title = "Protección activa",
                    checked = protectionEnabled,
                    onCheckedChange = viewModel::setProtectionEnabled
                )
                SettingDivider()
                SettingRow(
                    title = "Bloquear todos los desconocidos",
                    checked = blockAllUnknown,
                    onCheckedChange = viewModel::setBlockAllUnknown
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Para que CallGuard pueda filtrar llamadas, Android exige dos cosas: " +
                "que le des acceso a tus contactos y que lo asignes como tu app de " +
                "bloqueo de llamadas.",
            color = NeuTextMuted,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        NeumorphicButton(
            onClick = { contactsPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS) },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Contacts, contentDescription = null)
                Text("Conceder acceso a contactos", style = MaterialTheme.typography.titleSmall)
            }
        }

        Spacer(Modifier.height(12.dp))

        NeumorphicButton(
            onClick = { requestCallScreeningRole(context, roleRequestLauncher) },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.PhoneDisabled, contentDescription = null)
                Text("Establecer como app de bloqueo", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = NeuText, modifier = Modifier.padding(end = 8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeuAccent,
                checkedTrackColor = NeuSurface,
                uncheckedThumbColor = NeuTextMuted,
                uncheckedTrackColor = NeuSurface
            )
        )
    }
}

@Composable
private fun SettingDivider() {
    androidx.compose.material3.HorizontalDivider(color = NeuSurface, thickness = 1.dp)
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
