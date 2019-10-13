package com.marcguilera.bank.account.domain.default

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.CurrencyCode
import com.marcguilera.bank.account.data.AccountRepository
import com.marcguilera.bank.account.data.AccountResource
import com.marcguilera.bank.account.domain.AccountEntity
import com.marcguilera.bank.account.domain.AccountFacade
import com.marcguilera.bank.account.domain.AccountNotFoundException
import com.marcguilera.bank.account.domain.IllegalCurrencyException
import com.marcguilera.bank.identifier.IdentifierFactory
import com.marcguilera.bank.time.InstantFactory
import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.util.*


class DefaultAccountFacade (
        override val kodein: Kodein
) : KodeinAware, AccountFacade {

    private val repository: AccountRepository by instance()
    private val identifierFactory: IdentifierFactory by instance()
    private val instantFactory: InstantFactory by instance()

    private companion object Logger : KLogging()

    override suspend fun create(currency: CurrencyCode): AccountEntity {
        val id = identifierFactory.create()
        val cur = getCurrency(currency)
        val account = AccountEntity.DTO(id, cur)
        repository.save(account.toAccountEntity())
        logger.info { "Created the new account $id with currency $currency" }
        return account
    }

    override suspend fun getOne(id: AccountId): AccountEntity {
        val entity = repository.findOne(id)
                ?: throw notFound(id)
        return entity.toAccount()
    }

    override suspend fun getAll(): Iterable<AccountEntity> {
        val entities = repository.findAll()
        return entities.map { it.toAccount() }
    }

    private fun AccountResource.toAccount() =
            AccountEntity.DTO(id, getCurrency(currency))

    private fun AccountEntity.toAccountEntity() =
            AccountResource(id, currency.currencyCode, instantFactory.now())

    private fun getCurrency(currency: CurrencyCode): Currency {
        try {
            return Currency.getInstance(currency.toUpperCase())
        } catch (t: Throwable) {
            throw IllegalCurrencyException("Error parsing currency $currency", t)
        }
    }

    private fun notFound(id: AccountId) =
            AccountNotFoundException("Account not found for id = $id")
}