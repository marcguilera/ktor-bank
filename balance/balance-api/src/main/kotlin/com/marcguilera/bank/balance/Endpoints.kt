package com.marcguilera.bank.balance

import com.marcguilera.bank.account.ACCOUNT_ENDPOINT

const val BALANCES_ENDPOINT = "$ACCOUNT_ENDPOINT/balances"
const val BALANCE_ENDPOINT = "$ACCOUNT_ENDPOINT/balances/{balanceId}"
const val LAST_BALANCE_ENDPOINT = "$ACCOUNT_ENDPOINT/balances/last"