package com.marcguilera.bank.account.data

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.CurrencyCode
import java.time.Instant

/**
 * Represents a stored account.
 */
data class AccountResource(
        /**
         * The unique identifier of the account.
         */
        val id: AccountId,
        /**
         * The currency associated with the account.
         */
        val currency: CurrencyCode,

        /**
         * The time the account was created at.
         */
        val createdAt: Instant
)