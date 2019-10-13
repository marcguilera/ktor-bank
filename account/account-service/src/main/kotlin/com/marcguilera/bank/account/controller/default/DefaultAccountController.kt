package com.marcguilera.bank.account.controller.default

import com.marcguilera.bank.account.Account
import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.AccountInfo
import com.marcguilera.bank.account.controller.AbstractAccountController
import com.marcguilera.bank.account.domain.AccountEntity
import com.marcguilera.bank.account.domain.AccountFacade
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

/**
 * An [AbstractAccountController] using a [AccountFacade] to
 * fulfil client requests.
 */
class DefaultAccountController(
        override val kodein: Kodein
) : AbstractAccountController() {

    private val facade: AccountFacade by instance()

    override suspend fun create(info: AccountInfo): Account.DTO {
        val account = facade.create(info.currency)
        return account.toResponse()
    }

    override suspend fun getOne(id: AccountId): Account.DTO {
        val account = facade.getOne(id)
        return account.toResponse()
    }

    override suspend fun getAll(): Iterable<Account.DTO> {
        val accounts = facade.getAll()
        return accounts.map { it.toResponse() }
    }

    private fun AccountEntity.toResponse() =
            Account.DTO(id, currency.currencyCode)

}