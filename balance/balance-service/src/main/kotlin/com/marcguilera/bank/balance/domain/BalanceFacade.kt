package com.marcguilera.bank.balance.domain

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.BalanceSummary

interface BalanceFacade {
    suspend fun create(accountId: AccountId, amount: Long, summary: BalanceSummary? = null): BalanceItemEntity
    suspend fun getAll(accountId: AccountId): Iterable<BalanceItemEntity>
    suspend fun getOne(accountId: AccountId, id: BalanceItemId): BalanceItemEntity
    suspend fun getLast(accountId: AccountId): BalanceItemEntity
    suspend fun revert(accountId: AccountId, id: BalanceItemId): BalanceItemEntity
}