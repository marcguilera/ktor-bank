package com.marcguilera.bank.transfer.domain

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.transfer.TransferId

interface TransferFacade {
    suspend fun create(fromAccountId: AccountId, toAccountId: AccountId, amount: Long): TransferEntity
    suspend fun getOne(id: TransferId): TransferEntity
    suspend fun getAll(): Iterable<TransferEntity>
}