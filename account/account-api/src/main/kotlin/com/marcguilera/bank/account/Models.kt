package com.marcguilera.bank.account

typealias AccountId = String
typealias CurrencyCode = String

/**
 * Represents the information relevant for an account.
 */
interface AccountInfo {
    val currency: CurrencyCode

    data class DTO (
            override val currency: CurrencyCode
    ) : AccountInfo
}

/**
 * Represents the information for a persisted account.
 */
interface Account : AccountInfo {
    val id: AccountId

    data class DTO (
            override val id: AccountId,
            override val currency: CurrencyCode
    ) : Account
}