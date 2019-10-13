package com.marcguilera.bank.account.data.inmemory

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.data.AccountRepository
import com.marcguilera.bank.account.data.AccountResource
import java.util.concurrent.ConcurrentHashMap

typealias AccountMap = MutableMap<AccountId, AccountResource>

class InMemoryAccountRepository (
        private val map: AccountMap = ConcurrentHashMap()
) : AccountRepository {

    override fun save(resource: AccountResource) {
        map[resource.id] = resource
    }

    override fun findOne(id: AccountId): AccountResource? {
        return map[id]
    }

    override fun findAll(): Iterable<AccountResource> {
        return map.values.toSet()
    }

}