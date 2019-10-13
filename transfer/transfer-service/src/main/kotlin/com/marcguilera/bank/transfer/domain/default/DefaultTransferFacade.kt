package com.marcguilera.bank.transfer.domain.default

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.transfer.TransferId
import com.marcguilera.bank.transfer.data.TransferRepository
import com.marcguilera.bank.transfer.data.TransferResource
import com.marcguilera.bank.transfer.domain.TransferEntity
import com.marcguilera.bank.transfer.domain.TransferFacade
import com.marcguilera.bank.transfer.domain.TransferFactory
import com.marcguilera.bank.transfer.domain.TransferNotFoundException
import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DefaultTransferFacade (
        override val kodein: Kodein
) : TransferFacade, KodeinAware {

    private companion object : KLogging()
    private val repository: TransferRepository by instance()
    private val factory: TransferFactory by instance()

    override suspend fun create(fromAccountId: AccountId, toAccountId: AccountId, amount: Long): TransferEntity {
        val resource = factory.create(fromAccountId, toAccountId, amount)
        logger.info { "Created transfer $resource" }
        repository.save(resource)
        return resource.toTransferEntity()
    }

    override suspend fun getAll(): Iterable<TransferEntity> {
        val resources = repository.findAll()
        return resources.map { it.toTransferEntity() }
    }

    override suspend fun getOne(id: TransferId): TransferEntity {
        val resource = repository.findOne(id)
                ?: throw TransferNotFoundException("No transfer found with id = $id")
        return resource.toTransferEntity()
    }

    private fun TransferResource.toTransferEntity()
            = TransferEntity.DTO(id, fromAccountId, fromBalanceItemId, toAccountId, toBalanceItemId, amount, createdAt)
}