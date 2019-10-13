package com.marcguilera.bank.balance.domain.default

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.BalanceSummary
import com.marcguilera.bank.balance.data.BalanceItemRepository
import com.marcguilera.bank.balance.data.BalanceItemResource
import com.marcguilera.bank.balance.domain.BalanceFacade
import com.marcguilera.bank.balance.domain.BalanceItemEntity
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory
import com.marcguilera.bank.balance.domain.BalanceNotFoundException
import com.marcguilera.bank.balance.domain.RevertBalanceItemResourceFactory
import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DefaultBalanceFacade (
        override val kodein: Kodein
) : BalanceFacade, KodeinAware {

    private companion object : KLogging()
    private val repository: BalanceItemRepository by instance()
    private val factory: BalanceItemResourceFactory by instance()
    private val revertFactory: RevertBalanceItemResourceFactory by instance()

    override suspend fun create(accountId: AccountId, amount: Long, summary: BalanceSummary?): BalanceItemEntity {
        val item = factory.create(accountId, amount, summary)
        repository.save(item)
        return item.toBalanceItemEntity()
    }

    override suspend fun revert(accountId: AccountId, id: BalanceItemId): BalanceItemEntity {
        val item = revertFactory.create(accountId, id)
        repository.save(item)
        return item.toBalanceItemEntity()
    }

    override suspend fun getAll(accountId: AccountId): Iterable<BalanceItemEntity> {
        val items = repository.findAll(accountId)
                .takeIf { it.any() }
                ?: throw BalanceNotFoundException("No balances for account $accountId")
        return items.map { it.toBalanceItemEntity() }
    }

    override suspend fun getOne(accountId: AccountId, id: BalanceItemId): BalanceItemEntity {
        val item = repository.findOne(accountId, id)
                ?: throw BalanceNotFoundException("Couldn't find balance $id in account $accountId")
        return item.toBalanceItemEntity()
    }

    override suspend fun getLast(accountId: AccountId): BalanceItemEntity {
        val item = repository.findLast(accountId)
                ?: throw BalanceNotFoundException("No balances for account $accountId")
        return item.toBalanceItemEntity()
    }

    private fun BalanceItemResource.toBalanceItemEntity() =
            BalanceItemEntity.DTO(id, accountId, amount, total, summary, createdAt)

}