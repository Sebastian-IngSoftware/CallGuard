package com.callguard.app.data.repository

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Consulta si el número está agendado en los contactos del dispositivo.
     * Requiere permiso READ_CONTACTS concedido; si no está concedido, devuelve false
     * (es decir, se lo trata como desconocido).
     */
    suspend fun isNumberInContacts(rawNumber: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(rawNumber)
            )
            val projection = arrayOf(ContactsContract.PhoneLookup._ID)

            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                cursor.count > 0
            } ?: false
        } catch (e: SecurityException) {
            // Sin permiso READ_CONTACTS: lo tratamos como desconocido.
            false
        }
    }
}
