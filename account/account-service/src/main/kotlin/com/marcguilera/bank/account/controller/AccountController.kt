package com.marcguilera.bank.account.controller

import com.marcguilera.bank.account.Account
import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.AccountInfo
import com.marcguilera.bank.account.domain.IllegalCurrencyException
import org.kodein.di.ktor.controller.KodeinController
import javax.security.auth.login.AccountNotFoundException

/**
 * Controller allowing to interact with bank accounts.
 */
interface AccountController : KodeinController {
    /**
     * Creates an [Account] in the system.
     * @return the [Account].
     */
    @Throws(IllegalCurrencyException::class)
    suspend fun create(info: AccountInfo): Account

    /**
     * Gets an [Account] from the system.
     * @return the [Account].
     * @throws [AccountNotFoundException] if not found.
     */
    @Throws(AccountNotFoundException::class)
    suspend fun getOne(id: AccountId): Account

    /**
     * Gets all [Account] instances from the system.
     * @return The [Account] list.
     */
    suspend fun getAll(): Iterable<Account>
}