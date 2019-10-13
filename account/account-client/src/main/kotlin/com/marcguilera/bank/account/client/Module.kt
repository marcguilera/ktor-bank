package com.marcguilera.bank.account.client

import com.marcguilera.bank.account.client.default.DefaultAccountClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val account get() = Kodein.Module("account") {
    bind<AccountClient>() with singleton { DefaultAccountClient(kodein) }
}