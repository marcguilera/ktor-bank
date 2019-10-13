package com.marcguilera.bank.transfer.data

import com.marcguilera.bank.transfer.TransferId

interface TransferRepository {
    fun save(resource: TransferResource)
    fun findAll(): Iterable<TransferResource>
    fun findOne(id: TransferId): TransferResource?
}