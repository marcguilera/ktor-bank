package com.marcguilera.bank.balance.domain

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.data.BalanceItemResource

interface RevertBalanceItemResourceFactory {
    fun create(accountId: AccountId, id: BalanceItemId): BalanceItemResource
}