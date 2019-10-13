package com.marcguilera.bank.transfer.controller.default

import com.marcguilera.bank.transfer.Transfer
import com.marcguilera.bank.transfer.TransferId
import com.marcguilera.bank.transfer.TransferInfo
import com.marcguilera.bank.transfer.controller.AbstractTransferController
import com.marcguilera.bank.transfer.domain.TransferEntity
import com.marcguilera.bank.transfer.domain.TransferFacade
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class DefaultTransferController (
        override val kodein: Kodein
) : AbstractTransferController() {

    private val facade: TransferFacade by instance()

    override suspend fun create(info: TransferInfo): Transfer {
        val entity = facade.create(info.fromAccountId, info.toAccountId, info.amount)
        return entity.toTransfer()
    }

    override suspend fun getOne(id: TransferId): Transfer {
        val entity = facade.getOne(id)
        return entity.toTransfer()
    }

    override suspend fun getAll(): Iterable<Transfer> {
        val entities = facade.getAll()
        return entities.map { it.toTransfer() }
    }

    private fun TransferEntity.toTransfer()
            = Transfer.DTO(id, fromAccountId, fromBalanceItemId, toAccountId, toBalanceItemId, amount)

}