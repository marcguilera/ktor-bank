package com.marcguilera.bank.balance.controller

import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.balance.BalanceItem
import com.marcguilera.bank.balance.BalanceItemId
import com.marcguilera.bank.balance.BalanceItemInfo
import org.kodein.di.ktor.controller.KodeinController

/**
 * Represents a controller to interface with the balances.
 */
interface AccountBalanceController : KodeinController {
    suspend fun create(accountId: AccountId, info: BalanceItemInfo): BalanceItem
    suspend fun getAll(accountId: AccountId): Iterable<BalanceItem>
    suspend fun delete(accountId: AccountId, id: BalanceItemId): BalanceItem
    suspend fun getOne(accountId: AccountId, id: BalanceItemId): BalanceItem
    suspend fun getLast(accountId: AccountId): BalanceItem
}