package com.marcguilera.bank.transfer.data

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.transfer.TransferId
import java.time.Instant

data class TransferResource(
        val id: TransferId,
        val fromAccountId: AccountId,
        val fromBalanceItemId: BalanceItemId,
        val toAccountId: AccountId,
        val toBalanceItemId: BalanceItemId,
        val amount: Long,
        val createdAt: Instant
)