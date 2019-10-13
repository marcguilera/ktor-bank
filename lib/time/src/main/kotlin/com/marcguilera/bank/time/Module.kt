package com.marcguilera.bank.time

import com.marcguilera.bank.time.default.DefaultInstantFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val time get() = Kodein.Module("time") {
    bind<InstantFactory>() with  singleton { DefaultInstantFactory() }
}