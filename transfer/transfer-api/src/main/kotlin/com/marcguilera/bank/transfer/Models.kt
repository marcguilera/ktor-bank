package com.marcguilera.bank.transfer

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId

typealias TransferId = String


interface TransferInfo {
    val fromAccountId: AccountId
    val toAccountId: AccountId
    val amount: Long

    data class DTO(
            override val fromAccountId: AccountId,
            override val toAccountId: AccountId,
            override val amount: Long
    ) : TransferInfo
}

interface Transfer : TransferInfo {
    val id: TransferId
    val fromBalanceItemId: BalanceItemId
    val toBalanceItemId: BalanceItemId

    data class DTO (
            override val id: TransferId,
            override val fromAccountId: AccountId,
            override val fromBalanceItemId: BalanceItemId,
            override val toAccountId: AccountId,
            override val toBalanceItemId: BalanceItemId,
            override val amount: Long
    ) : Transfer
}