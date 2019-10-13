package com.marcguilera.bank.account.data

import com.marcguilera.bank.account.AccountId

/**
 * A repository interfacing with the underlying database.
 */
interface AccountRepository {
    /**
     * Finds an [AccountResource] by its id.
     * @return the [AccountResource] or null
     */
    fun findOne(id: AccountId): AccountResource?

    /**
     * Saves a [AccountResource] in the database.
     */
    fun save(resource: AccountResource)

    /**
     * Gets all the [AccountResource] instances from the database.
     * @return the [AccountResource] list.
     */
    fun findAll(): Iterable<AccountResource>
}