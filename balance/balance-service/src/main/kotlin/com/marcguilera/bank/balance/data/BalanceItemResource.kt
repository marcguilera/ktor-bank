package com.marcguilera.bank.balance.data

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.BalanceSummary
import java.time.Instant

data class BalanceItemResource(
        val id: BalanceItemId,
        val accountId: AccountId,
        val amount: Long,
        val total: Long,
        val summary: BalanceSummary?,
        val createdAt: Instant
)