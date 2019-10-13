package com.marcguilera.bank.balance.domain

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.BalanceSummary
import java.time.Instant

interface BalanceItemEntity {
    val id: BalanceItemId
    val accountId: AccountId
    val amount: Long
    val total: Long
    val summary: BalanceSummary?
    val date: Instant

    data class DTO(
            override val id: BalanceItemId,
            override val accountId: AccountId,
            override val amount: Long,
            override val total: Long,
            override val summary: BalanceSummary?,
            override val date: Instant
    ) : BalanceItemEntity
}