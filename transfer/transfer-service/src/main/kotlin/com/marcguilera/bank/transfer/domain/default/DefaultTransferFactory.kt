package com.marcguilera.bank.transfer.domain.default

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.client.AccountClient
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.client.BalanceClient
import com.marcguilera.bank.identifier.IdentifierFactory
import com.marcguilera.bank.time.InstantFactory
import com.marcguilera.bank.transfer.data.TransferResource
import com.marcguilera.bank.transfer.domain.IllegalTransferException
import com.marcguilera.bank.transfer.domain.TransferFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DefaultTransferFactory (
        override val kodein: Kodein
) : TransferFactory, KodeinAware {

    private val accountClient: AccountClient by instance()
    private val balanceClient: BalanceClient by instance()
    private val identifierFactory: IdentifierFactory by instance()
    private val instantFactory: InstantFactory by instance()

    override suspend fun create(fromAccountId: AccountId, toAccountId: AccountId, amount: Long): TransferResource {
        if (fromAccountId == toAccountId) {
            throw IllegalTransferException("fromAccountId and toAccountId can't be the same")
        }

        // Will directly throw if the accounts don't exist
        val fromAccount = accountClient.getOne(fromAccountId)
        val toAccount = accountClient.getOne(toAccountId)

        if (fromAccount.currency != toAccount.currency) {
            throw IllegalTransferException("From and to accounts must have the same currency")
        }

        // Will directly throw if insufficient funds
        val from = balanceClient.withdraw(fromAccountId, toAccountId, amount)

        val to = try {
            balanceClient.deposit(fromAccountId, toAccountId, amount)
        } catch (t: Throwable) {
            balanceClient.rollback(from.accountId, from.id)
            throw t
        }

        val id = identifierFactory.create()
        val now = instantFactory.now()

        return TransferResource(id, from.accountId, from.id, to.accountId, to.id, amount, now)
    }

    private suspend fun BalanceClient.withdraw(fromAccountId: AccountId, toAccountId: AccountId, amount: Long)
            = create(fromAccountId, -amount, summary = "Transfer to $toAccountId")

    private suspend fun BalanceClient.deposit(fromAccountId: AccountId, toAccountId: AccountId, amount: Long)
            = create(toAccountId, amount, summary = "Transfer from $fromAccountId")

    private suspend fun BalanceClient.rollback(accountId: AccountId, balanceItemId: BalanceItemId)
            = delete(accountId, balanceItemId)

}