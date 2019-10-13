package com.marcguilera.bank.account.domain

import com.marcguilera.bank.account.AccountId
import java.util.*

/**
 * Represents a domain entity.
 */
interface AccountEntity {
    /**
     * The unique identifier for this [AccountEntity].
     */
    val id: AccountId

    /**
     * The [Currency] this [AccountEntity] has associated.
     */
    val currency: Currency

    data class DTO (
            override val id: AccountId,
            override val currency: Currency
    ) : AccountEntity
}