package com.marcguilera.bank.account.client.default

import com.marcguilera.bank.account.ACCOUNT_ENDPOINT
import com.marcguilera.bank.account.Account
import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.client.AccountClient
import com.marcguilera.bank.error.get
import io.ktor.client.HttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class DefaultAccountClient(
        override val kodein: Kodein
) : KodeinAware, AccountClient {

    private val client: HttpClient by instance()

    private companion object {
        // TODO put url in config
        const val ACCOUNT_URL = "http://account-service/$ACCOUNT_ENDPOINT"
    }

    override suspend fun getOne(accountId: AccountId)
            = client.get<Account.DTO>(accountUrlOf(accountId))

    private fun accountUrlOf(accountId: AccountId)
            = ACCOUNT_URL
                .replace("{accountId}", accountId)
}