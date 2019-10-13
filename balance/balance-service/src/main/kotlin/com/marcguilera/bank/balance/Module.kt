package com.marcguilera.bank.balance

import com.marcguilera.bank.balance.controller.AccountBalanceController
import com.marcguilera.bank.balance.controller.default.DefaultAccountBalanceController
import com.marcguilera.bank.balance.data.BalanceItemRepository
import com.marcguilera.bank.balance.data.inmemory.InMemoryBalanceItemRepository
import com.marcguilera.bank.balance.domain.BalanceFacade
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory
import com.marcguilera.bank.balance.domain.RevertBalanceItemResourceFactory
import com.marcguilera.bank.balance.domain.default.DefaultBalanceFacade
import com.marcguilera.bank.balance.domain.default.DefaultBalanceItemResourceFactory
import com.marcguilera.bank.balance.domain.default.DefaultRevertBalanceItemResourceFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


val balance get() = Kodein.Module("balance") {
    bind<BalanceItemRepository>() with singleton { InMemoryBalanceItemRepository() }
    bind<AccountBalanceController>() with singleton { DefaultAccountBalanceController(kodein) }
    bind<BalanceFacade>() with singleton { DefaultBalanceFacade(kodein) }
    bind<BalanceItemResourceFactory>() with singleton { DefaultBalanceItemResourceFactory(kodein) }
    bind<RevertBalanceItemResourceFactory>() with singleton { DefaultRevertBalanceItemResourceFactory(kodein) }
}