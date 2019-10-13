package com.marcguilera.bank.transfer.test

import com.marcguilera.bank.account.Account
import com.marcguilera.bank.account.client.AccountClient
import com.marcguilera.bank.balance.BalanceItem
import com.marcguilera.bank.balance.client.BalanceClient
import com.marcguilera.bank.test.mocks.Mocks
import com.marcguilera.bank.test.mocks.withMocks
import com.marcguilera.bank.transfer.Transfer
import com.marcguilera.bank.transfer.TransferInfo
import com.marcguilera.bank.transfer.controller.TransferController
import com.marcguilera.bank.transfer.data.TransferRepository
import com.marcguilera.bank.transfer.data.TransferResource
import com.marcguilera.bank.transfer.domain.TransferEntity
import com.marcguilera.bank.transfer.domain.TransferFacade
import com.marcguilera.bank.transfer.domain.TransferFactory
import com.nhaarman.mockitokotlin2.mock
import org.jetbrains.spek.api.dsl.Spec
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import com.nhaarman.mockitokotlin2.reset as resetMocks

class TransferMocks private constructor() : Mocks() {
    companion object Factory : Mocks.Factory<TransferMocks> {
        override fun create() = TransferMocks()
    }

    val fromAccountId = "accountId1"
    val toAccountId = "accountId2"
    val fromAccountBalanceId = "balanceId1"
    val toAccountBalanceId = "balanceId2"
    val amount = 100L
    val currency = "EUR"

    val transfer = Transfer.DTO(id, fromAccountId, fromAccountBalanceId, toAccountId, toAccountBalanceId, amount)
    val info = TransferInfo.DTO(fromAccountId, toAccountId, amount)
    val entity = TransferEntity.DTO(id, fromAccountId, fromAccountBalanceId, toAccountId, toAccountBalanceId, amount, now)
    val resource = TransferResource(id, fromAccountId, fromAccountBalanceId, toAccountId, toAccountBalanceId, amount, now)
    val fromAccount = Account.DTO(fromAccountId, currency)
    val toAccount = Account.DTO(toAccountId, currency)
    val fromBalanceItem = BalanceItem.DTO(fromAccountBalanceId, fromAccountId, -amount, 0)
    val toBalanceItem = BalanceItem.DTO(toAccountBalanceId, toAccountId, amount, amount)

    val controller: TransferController = mock()
    val facade: TransferFacade = mock()
    val factory: TransferFactory = mock()
    val repository: TransferRepository = mock()
    val accountClient: AccountClient = mock()
    val balanceClient: BalanceClient = mock()

    override val kodein: Kodein = Kodein {
        extend(super.kodein)
        bind() from instance(controller)
        bind() from instance(facade)
        bind() from instance(factory)
        bind() from instance(repository)
        bind() from instance(accountClient)
        bind() from instance(balanceClient)
    }

    override fun reset() {
        super.reset()
        resetMocks(controller, facade, factory, repository, accountClient, balanceClient)
    }
}

fun Spec.withMocks(mocks: TransferMocks.() -> Unit)
        = withMocks(TransferMocks, mocks)