package com.marcguilera.bank.transfer.domain

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.transfer.TransferId
import java.time.Instant

interface TransferEntity {
    val id: TransferId
    val fromAccountId: AccountId
    val fromBalanceItemId: BalanceItemId
    val toAccountId: AccountId
    val toBalanceItemId: BalanceItemId
    val amount: Long
    val date: Instant
    data class DTO(
            override val id: TransferId,
            override val fromAccountId: AccountId,
            override val fromBalanceItemId: BalanceItemId,
            override val toAccountId: AccountId,
            override val toBalanceItemId: BalanceItemId,
            override val amount: Long,
            override val date: Instant
    ) : TransferEntity
}