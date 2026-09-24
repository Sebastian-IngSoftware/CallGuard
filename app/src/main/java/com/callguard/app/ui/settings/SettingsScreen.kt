package com.callguard.app.ui.settings

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val protectionEnabled by viewModel.protectionEnabled.collectAsStateWithLifecycle()
    val blockAllUnknown by viewModel.blockAllUnknown.collectAsStateWithLifecycle()

    val roleRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { /* el usuario aceptó o rechazó asignarnos como app de screening */ }

    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* resultado del permiso READ_CONTACTS */ }

    Scaffold(topBar = { TopAppBar(title = { Text("Configuración") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            SettingRow(
                title = "Protección activa",
                checked = protectionEnabled,
                onCheckedChange = viewModel::setProtectionEnabled
            )
            SettingRow(
                title = "Bloquear todos los números desconocidos",
                checked = blockAllUnknown,
                onCheckedChange = viewModel::setBlockAllUnknown
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Para que CallGuard pueda filtrar llamadas, Android exige dos cosas: " +
                    "que le des acceso a tus contactos y que lo asignes como tu app de " +
                    "bloqueo de llamadas.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { contactsPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Conceder acceso a contactos")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { requestCallScreeningRole(context, roleRequestLauncher) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Establecer como app de bloqueo de llamadas")
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, modifier = Modifier.padding(end = 8.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

private fun requestCallScreeningRole(
    context: android.content.Context,
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
