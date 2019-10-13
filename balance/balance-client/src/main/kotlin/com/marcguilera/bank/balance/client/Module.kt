package com.marcguilera.bank.balance.client

import com.marcguilera.bank.balance.client.default.DefaultBalanceClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val balance get() = Kodein.Module("balance") {
    bind<BalanceClient>() with singleton { DefaultBalanceClient(kodein) }
}