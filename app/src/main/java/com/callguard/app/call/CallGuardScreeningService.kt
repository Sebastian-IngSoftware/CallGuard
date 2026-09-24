package com.callguard.app.call

import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.CallScreeningService.CallResponse
import com.callguard.app.data.datastore.SettingsDataStore
import com.callguard.app.data.repository.BlacklistRepository
import com.callguard.app.data.repository.ContactsRepository
import com.callguard.app.domain.usecase.ShouldBlockCallUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Servicio que Android invoca para cada llamada entrante una vez que el usuario
 * nos asignó como "app de screening de llamadas" (RoleManager.ROLE_CALL_SCREENING,
 * ver SettingsScreen). Google llama a onScreenCall() por cada llamada nueva.
 *
 * Referencia oficial:
 * https://developer.android.com/develop/connectivity/telecom/dialer-app/screen-calls
 */
@AndroidEntryPoint
class CallGuardScreeningService : CallScreeningService() {

    @Inject lateinit var contactsRepository: ContactsRepository
    @Inject lateinit var blacklistRepository: BlacklistRepository
    @Inject lateinit var settingsDataStore: SettingsDataStore
    @Inject lateinit var shouldBlockCall: ShouldBlockCallUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        val number = callDetails.handle?.schemeSpecificPart

        if (number.isNullOrBlank()) {
            respondAllow(callDetails)
            return
        }

        serviceScope.launch {
            val protectionEnabled = settingsDataStore.protectionEnabled.first()
            if (!protectionEnabled) {
                respondAllow(callDetails)
                return@launch
            }

            val isKnownContact = contactsRepository.isNumberInContacts(number)
            val blockAllUnknown = settingsDataStore.blockAllUnknown.first()
            val rules = blacklistRepository.getRulesOnce()

            val shouldBlock = shouldBlockCall(
                rawNumber = number,
                isKnownContact = isKnownContact,
                blockAllUnknown = blockAllUnknown,
                blacklistRules = rules
            )

            if (shouldBlock) respondBlock(callDetails) else respondAllow(callDetails)
        }
    }

    private fun respondAllow(callDetails: Call.Details) {
        val response = CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipCallLog(false)
            .setSkipNotification(false)
            .build()
        respondToCall(callDetails, response)
    }

    private fun respondBlock(callDetails: Call.Details) {
        val response = CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipCallLog(false)   // dejamos registro, para que el usuario vea qué se bloqueó
            .setSkipNotification(true)
            .build()
        respondToCall(callDetails, response)
    }
}
