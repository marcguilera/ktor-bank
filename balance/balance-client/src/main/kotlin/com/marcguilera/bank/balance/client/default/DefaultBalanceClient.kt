package com.marcguilera.bank.balance.client.default

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BALANCES_ENDPOINT
import com.marcguilera.bank.balance.BALANCE_ENDPOINT
import com.marcguilera.bank.balance.BalanceItem
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.BalanceItemInfo
import com.marcguilera.bank.balance.BalanceSummary
import com.marcguilera.bank.balance.client.BalanceClient
import com.marcguilera.bank.error.delete
import com.marcguilera.bank.error.post
import io.ktor.client.HttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DefaultBalanceClient(
        override val kodein: Kodein
) : BalanceClient, KodeinAware {

    private val client: HttpClient by instance()

    private companion object {
        // TODO put urls in config
        const val BALANCES_URL = "http://balance-service/$BALANCES_ENDPOINT"
        const val BALANCE_URL = "http://balance-service/$BALANCE_ENDPOINT"
    }

    override suspend fun create(accountId: AccountId, amount: Long, summary: BalanceSummary?)
            = client.post<BalanceItem.DTO>(balanceUrlOf(accountId), BalanceItemInfo.DTO(amount, summary))

    override suspend fun delete(accountId: AccountId, id: BalanceItemId)
            = client.delete<BalanceItem.DTO>(balanceUrlOf(accountId, id))

    private fun balanceUrlOf(accountId: AccountId)
            = BALANCES_URL
                .replace("{accountId}", accountId)

    private fun balanceUrlOf(accountId: AccountId, balanceId: BalanceItemId)
            = BALANCE_URL
                .replace("{accountId}", accountId)
                .replace("{balanceId}", balanceId)
}