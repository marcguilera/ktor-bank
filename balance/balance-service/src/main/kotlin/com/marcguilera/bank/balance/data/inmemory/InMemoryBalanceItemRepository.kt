package com.marcguilera.bank.balance.data.inmemory

import com.google.common.collect.HashBasedTable.create
import com.google.common.collect.Table
import com.google.common.collect.Tables.synchronizedTable
import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.data.BalanceItemRepository
import com.marcguilera.bank.balance.data.BalanceItemResource

typealias BalanceItemTable = Table<AccountId, BalanceItemId, BalanceItemResource>

class InMemoryBalanceItemRepository(
        private val table: BalanceItemTable = synchronizedTable(create())
) : BalanceItemRepository {

    override fun findAll(accountId: AccountId): Iterable<BalanceItemResource> {
        return table.row(accountId).values.sortedByDescending { it.createdAt }
    }

    override fun findLast(accountId: AccountId): BalanceItemResource? {
        return table.row(accountId).values.maxBy { it.createdAt }
    }

    override fun findOne(accountId: AccountId, id: BalanceItemId): BalanceItemResource? {
        return table.get(accountId, id)
    }

    override fun save(resource: BalanceItemResource) {
        table.put(resource.accountId, resource.id, resource)
    }
}
