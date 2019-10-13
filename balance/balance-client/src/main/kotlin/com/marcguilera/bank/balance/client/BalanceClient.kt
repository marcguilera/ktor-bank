package com.marcguilera.bank.balance.client

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItem
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.BalanceSummary

interface BalanceClient {
    suspend fun create(accountId: AccountId, amount: Long, summary: BalanceSummary? = null): BalanceItem
    suspend fun delete(accountId: AccountId, id: BalanceItemId): BalanceItem
}