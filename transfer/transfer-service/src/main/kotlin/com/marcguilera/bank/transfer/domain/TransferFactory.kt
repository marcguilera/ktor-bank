package com.marcguilera.bank.transfer.domain

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.transfer.data.TransferResource

interface TransferFactory {
    suspend fun create(fromAccountId: AccountId, toAccountId: AccountId, amount: Long): TransferResource
}