package com.marcguilera.bank.balance

import com.marcguilera.bank.account.AccountId

typealias BalanceItemId = String
typealias BalanceSummary = String

interface BalanceItemKey {
    val id: BalanceItemId
    val accountId: AccountId
    data class DTO(
            override val id: BalanceItemId,
            override val accountId: AccountId
    ) : BalanceItemKey
}

/**
 * Represents an item in a balance statement.
 */
interface BalanceItemInfo {
    /**
     * The amount agnostic of currency so it's a monetary amount in minor units.
     * ie: 1USD = 100. It's down to the consumer to be currency-aware.
     *
     * Negative numbers represents withdrawals while positive represent deposits.
     */
    val amount: Long

    /**
     * An optional arbitrary summary for the transaction.
     */
    val summary: BalanceSummary?

    data class DTO(
            override val amount: Long,
            override val summary: BalanceSummary? = null
    ) : BalanceItemInfo
}

/**
 * Represents a persisted item in a balance statement.
 */
interface BalanceItem : BalanceItemInfo {
    /**
     * The unique identifier of the balance statement.
     */
    val id: BalanceItemId

    /**
     * The account id associated with the balance.
     */
    val accountId: AccountId

    /**
     * The total amount of currency available after this movement.
     */
    val total: Long

    data class DTO(
            override val id: BalanceItemId,
            override val accountId: AccountId,
            override val amount: Long,
            override val total: Long,
            override val summary: BalanceSummary? = null
    ) : BalanceItem
}