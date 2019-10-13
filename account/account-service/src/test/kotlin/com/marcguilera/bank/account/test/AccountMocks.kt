package com.marcguilera.bank.account.test

import com.marcguilera.bank.account.Account
import com.marcguilera.bank.account.AccountInfo
import com.marcguilera.bank.account.controller.AccountController
import com.marcguilera.bank.account.data.AccountRepository
import com.marcguilera.bank.account.data.AccountResource
import com.marcguilera.bank.account.domain.AccountEntity
import com.marcguilera.bank.account.domain.AccountFacade
import com.marcguilera.bank.test.mocks.Mocks
import com.marcguilera.bank.test.mocks.withMocks
import com.nhaarman.mockitokotlin2.mock
import org.jetbrains.spek.api.dsl.Spec
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import java.util.*
import com.nhaarman.mockitokotlin2.reset as resetMocks

class AccountMocks private constructor(): Mocks() {

    companion object Factory : Mocks.Factory<AccountMocks> {
        override fun create() = AccountMocks()
    }

    val currency = "USD"
    val resource = AccountResource(id, currency, now)
    val entity = AccountEntity.DTO(id, Currency.getInstance(currency))
    val info = AccountInfo.DTO(currency)
    val account = Account.DTO(id, currency)

    val repository: AccountRepository = mock()
    val facade: AccountFacade = mock()
    val controller: AccountController = mock()

    override val kodein = Kodein {
        extend(super.kodein)
        bind() from instance(repository)
        bind() from instance(facade)
        bind() from instance(controller)
    }

    override fun reset() {
        super.reset()
        resetMocks(repository, facade, controller)
    }
}

fun Spec.withMocks(mocks: AccountMocks.() -> Unit)
    = withMocks(AccountMocks, mocks)