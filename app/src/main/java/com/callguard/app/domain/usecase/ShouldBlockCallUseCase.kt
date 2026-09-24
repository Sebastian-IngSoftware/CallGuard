package com.callguard.app.domain.usecase

import com.callguard.app.domain.model.BlacklistRule
import com.callguard.app.domain.model.MatchType
import javax.inject.Inject

/**
 * Lógica pura (sin Android) que decide si una llamada debe bloquearse.
 * Al no depender de nada de Android es 100% testeable con JUnit normal.
 *
 * Orden de reglas:
 * 1. Contacto conocido -> nunca se bloquea.
 * 2. "Bloquear todos los desconocidos" activo -> se bloquea.
 * 3. Coincide con alguna regla de la lista negra habilitada -> se bloquea.
 * 4. Si no aplica nada de lo anterior -> se permite.
 */
class ShouldBlockCallUseCase @Inject constructor() {

    operator fun invoke(
        rawNumber: String,
        isKnownContact: Boolean,
        blockAllUnknown: Boolean,
        blacklistRules: List<BlacklistRule>
    ): Boolean {
        if (isKnownContact) return false

        val number = normalize(rawNumber)

        if (blockAllUnknown) return true

        return blacklistRules.any { rule ->
            rule.isEnabled && matches(number, normalize(rule.pattern), rule.matchType)
        }
    }

    private fun matches(number: String, pattern: String, type: MatchType): Boolean {
        if (pattern.isBlank()) return false
        return when (type) {
            MatchType.CONTAINS -> number.contains(pattern)
            MatchType.STARTS_WITH -> number.startsWith(pattern)
            MatchType.ENDS_WITH -> number.endsWith(pattern)
            MatchType.EXACT -> number == pattern
        }
    }

    /**
     * Deja solo dígitos: evita que espacios, guiones, paréntesis o el "+"
     * del código de país rompan la comparación.
     */
    private fun normalize(number: String): String = number.filter { it.isDigit() }
}
