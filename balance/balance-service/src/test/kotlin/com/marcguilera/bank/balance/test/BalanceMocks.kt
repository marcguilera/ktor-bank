package com.marcguilera.bank.balance.test

import com.marcguilera.bank.balance.BalanceItem
import com.marcguilera.bank.balance.BalanceItemInfo
import com.marcguilera.bank.balance.controller.AccountBalanceController
import com.marcguilera.bank.balance.data.BalanceItemRepository
import com.marcguilera.bank.balance.data.BalanceItemResource
import com.marcguilera.bank.balance.domain.BalanceFacade
import com.marcguilera.bank.balance.domain.BalanceItemEntity
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory
import com.marcguilera.bank.balance.domain.RevertBalanceItemResourceFactory
import com.marcguilera.bank.test.mocks.Mocks
import com.marcguilera.bank.test.mocks.withMocks
import com.nhaarman.mockitokotlin2.mock
import org.jetbrains.spek.api.dsl.Spec
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import com.nhaarman.mockitokotlin2.reset as resetMocks

class BalanceMocks private constructor(): Mocks() {

    companion object Factory : Mocks.Factory<BalanceMocks> {
        override fun create() = BalanceMocks()
    }

    val accountId = "accountId"
    val amount = 100L
    val total = 100L
    val summary = "summary"

    val resource = BalanceItemResource(id, accountId, amount, total, summary, now)
    val entity = BalanceItemEntity.DTO(id, accountId, amount, total, summary, now)
    val balance = BalanceItem.DTO(id, accountId, amount, total, summary)
    val info = BalanceItemInfo.DTO(amount, summary)

    val facade: BalanceFacade = mock()
    val factory: BalanceItemResourceFactory = mock()
    val revertFactory: RevertBalanceItemResourceFactory = mock()
    val repository: BalanceItemRepository = mock()
    val controller: AccountBalanceController = mock()

    override val kodein = Kodein {
        extend(super.kodein)
        bind() from instance(facade)
        bind() from instance(factory)
        bind() from instance(revertFactory)
        bind() from instance(repository)
        bind() from instance(controller)
    }

    override fun reset() {
        super.reset()
        resetMocks(facade, factory, revertFactory, repository, controller)
    }
}

fun Spec.withMocks(mocks: BalanceMocks.() -> Unit)
        = withMocks(BalanceMocks, mocks)