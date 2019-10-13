package com.marcguilera.bank.account

import com.marcguilera.bank.account.controller.AccountController
import com.marcguilera.bank.account.controller.default.DefaultAccountController
import com.marcguilera.bank.account.data.AccountRepository
import com.marcguilera.bank.account.data.inmemory.InMemoryAccountRepository
import com.marcguilera.bank.account.domain.AccountFacade
import com.marcguilera.bank.account.domain.default.DefaultAccountFacade
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * The container to inject all dependencies from the account module.
 */
val account get() = Kodein.Module("account") {
    bind<AccountController>() with singleton { DefaultAccountController(kodein) }
    bind<AccountRepository>() with singleton { InMemoryAccountRepository() }
    bind<AccountFacade>() with singleton { DefaultAccountFacade(kodein) }
}