package com.marcguilera.bank.transfer

import com.marcguilera.bank.account.client.account
import com.marcguilera.bank.balance.client.balance
import com.marcguilera.bank.transfer.controller.TransferController
import com.marcguilera.bank.transfer.controller.default.DefaultTransferController
import com.marcguilera.bank.transfer.data.TransferRepository
import com.marcguilera.bank.transfer.data.inmemory.InMemoryTransferRepository
import com.marcguilera.bank.transfer.domain.TransferFacade
import com.marcguilera.bank.transfer.domain.TransferFactory
import com.marcguilera.bank.transfer.domain.default.DefaultTransferFacade
import com.marcguilera.bank.transfer.domain.default.DefaultTransferFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val transfer get() = Kodein.Module("transfer") {
    import(balance)
    import(account)
    bind<TransferController>() with singleton { DefaultTransferController(kodein) }
    bind<TransferFacade>() with singleton { DefaultTransferFacade(kodein) }
    bind<TransferFactory>() with singleton { DefaultTransferFactory(kodein) }
    bind<TransferRepository>() with singleton { InMemoryTransferRepository() }
}