package com.marcguilera.bank.balance.domain

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceSummary
import com.marcguilera.bank.balance.data.BalanceItemResource
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory.Flags.Companion.default

interface BalanceItemResourceFactory {
    fun create(accountId: AccountId, amount: Long, summary: BalanceSummary? = null, flags: Flags = default()): BalanceItemResource
    data class Flags(
            val isZeroAllowed: Boolean,
            val isOverdraftAllowed: Boolean
    ) {
        companion object {
            fun default() = Flags(isZeroAllowed = false, isOverdraftAllowed = false)
            fun overdraft() = default().copy(isOverdraftAllowed = true)
            fun zero() = default().copy(isZeroAllowed = true)
        }
    }
}