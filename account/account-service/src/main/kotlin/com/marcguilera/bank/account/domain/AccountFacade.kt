package com.marcguilera.bank.account.domain

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.CurrencyCode

/**
 * Represents a gateway to the account domain.
 */
interface AccountFacade {
    /**
     * Creates an [AccountEntity] in the domain.
     * @return The [AccountEntity] that was created.
     * @throws [IllegalCurrencyException] if the currency can't be parsed.
     */
    @Throws(IllegalCurrencyException::class)
    suspend fun create(currency: CurrencyCode): AccountEntity

    /**
     * Gets one [AccountEntity] from the domain.
     * @return The [AccountEntity]
     * @throws [AccountNotFoundException] is the [AccountEntity] can't be found.
     */
    @Throws(AccountNotFoundException::class)
    suspend fun getOne(id: AccountId): AccountEntity

    /**
     * Gets all [AccountEntity] from the domain.
     * @return The [AccountEntity] iterable.
     */
    suspend fun getAll(): Iterable<AccountEntity>
}