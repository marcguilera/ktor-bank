package com.marcguilera.bank.transfer.data.inmemory

import com.marcguilera.bank.transfer.TransferId
import com.marcguilera.bank.transfer.data.TransferRepository
import com.marcguilera.bank.transfer.data.TransferResource
import java.util.concurrent.ConcurrentHashMap

class InMemoryTransferRepository(
        private val map: MutableMap<TransferId, TransferResource> = ConcurrentHashMap()
) : TransferRepository {
    override fun findOne(id: TransferId): TransferResource? {
        return map[id]
    }

    override fun findAll(): Iterable<TransferResource> {
        return map.values.toList()
    }

    override fun save(resource: TransferResource) {
        map[resource.id] = resource
    }
}