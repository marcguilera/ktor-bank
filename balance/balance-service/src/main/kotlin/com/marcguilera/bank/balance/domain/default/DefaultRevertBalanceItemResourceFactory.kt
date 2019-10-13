package com.marcguilera.bank.balance.domain.default

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.data.BalanceItemRepository
import com.marcguilera.bank.balance.data.BalanceItemResource
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory.Flags.Companion.overdraft
import com.marcguilera.bank.balance.domain.BalanceNotFoundException
import com.marcguilera.bank.balance.domain.RevertBalanceItemResourceFactory
import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DefaultRevertBalanceItemResourceFactory(
        override val kodein: Kodein
) : RevertBalanceItemResourceFactory, KodeinAware {

    private companion object : KLogging()
    private val factory: BalanceItemResourceFactory by instance()
    private val repository: BalanceItemRepository by instance()

    override fun create(accountId: AccountId, id: BalanceItemId): BalanceItemResource {
        val original = repository.findOne(accountId, id)
                ?: throw BalanceNotFoundException("Can't revert balance because there was none with account $accountId and balance $id")

        val item = factory.create(accountId, original.revertAmount, original.revertSummary, overdraft())

        logger.info { "Created revert of $original into $item" }

        return item
    }

    private val BalanceItemResource.revertSummary
        get() = "[REVERT] $id ${summary ?: ""}"
    private val BalanceItemResource.revertAmount
        get() = -amount

}