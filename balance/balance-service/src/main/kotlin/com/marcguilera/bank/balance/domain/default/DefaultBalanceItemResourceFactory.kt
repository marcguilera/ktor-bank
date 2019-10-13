package com.marcguilera.bank.balance.domain.default

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceSummary
import com.marcguilera.bank.balance.data.BalanceItemRepository
import com.marcguilera.bank.balance.data.BalanceItemResource
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory.Flags
import com.marcguilera.bank.balance.domain.IllegalAmountException
import com.marcguilera.bank.identifier.IdentifierFactory
import com.marcguilera.bank.time.InstantFactory
import mu.KLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DefaultBalanceItemResourceFactory (
        override val kodein: Kodein
) : BalanceItemResourceFactory, KodeinAware {

    private val repository: BalanceItemRepository by instance()
    private val identifierFactory: IdentifierFactory by instance()
    private val instantFactory: InstantFactory by instance()
    private companion object : KLogging()

    override fun create(accountId: AccountId, amount: Long, summary: BalanceSummary?, flags: Flags): BalanceItemResource {
        if (amount == 0L) {
            if (!flags.isZeroAllowed) {
                throw IllegalAmountException("Amount can't be 0")
            }
            logger.warn { "Allowed creation of a 0 balance entry." }
        }

        val id = identifierFactory.create()
        val now = instantFactory.now()

        val total = repository.findLast(accountId)?.total ?: 0L

        val nextTotal = amount + total

        if (nextTotal < 0) {
            if(!flags.isOverdraftAllowed) {
                throw IllegalAmountException("Can't withdraw $amount from the current balance of $total")
            }
            logger.warn { "Allowed overdraft in account $accountId with amount $amount. Next balance is $nextTotal." }
        }

        val item = BalanceItemResource(id, accountId, amount, nextTotal, summary, now)

        logger.info { "Created $item" }

        return item
    }

}