package com.marcguilera.bank.account.client

import com.marcguilera.bank.account.Account
import com.marcguilera.bank.account.AccountId

interface AccountClient {
    suspend fun getOne(accountId: AccountId): Account
}