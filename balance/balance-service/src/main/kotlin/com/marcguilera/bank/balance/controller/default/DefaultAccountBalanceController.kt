package com.marcguilera.bank.balance.controller.default

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItem
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.BalanceItemInfo
import com.marcguilera.bank.balance.controller.AbstractAccountBalanceController
import com.marcguilera.bank.balance.domain.BalanceFacade
import com.marcguilera.bank.balance.domain.BalanceItemEntity
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class DefaultAccountBalanceController (
        override val kodein: Kodein
) : AbstractAccountBalanceController() {

    private val facade: BalanceFacade by instance()

    override suspend fun create(accountId: AccountId, info: BalanceItemInfo): BalanceItem {
        val item = facade.create(accountId, info.amount, info.summary)
        return item.toBalanceItem()
    }

    override suspend fun getAll(accountId: AccountId): Iterable<BalanceItem> {
        val items = facade.getAll(accountId)
        return items.map { it.toBalanceItem() }
    }

    override suspend fun delete(accountId: AccountId, id: BalanceItemId): BalanceItem {
        val item = facade.revert(accountId, id)
        return item.toBalanceItem()
    }

    override suspend fun getOne(accountId: AccountId, id: BalanceItemId): BalanceItem {
        val item = facade.getOne(accountId, id)
        return item.toBalanceItem()
    }

    override suspend fun getLast(accountId: AccountId): BalanceItem {
        val item = facade.getLast(accountId)
        return item.toBalanceItem()
    }

    private fun BalanceItemEntity.toBalanceItem()
        = BalanceItem.DTO(id, accountId, amount, total, summary)
}