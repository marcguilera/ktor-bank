package com.marcguilera.bank.balance.data

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId

interface BalanceItemRepository {
    fun save(resource: BalanceItemResource)
    fun findAll(accountId: AccountId): Iterable<BalanceItemResource>
    fun findLast(accountId: AccountId): BalanceItemResource?
    fun findOne(accountId: AccountId, id: BalanceItemId): BalanceItemResource?
}