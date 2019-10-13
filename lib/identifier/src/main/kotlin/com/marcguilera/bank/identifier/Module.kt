package com.marcguilera.bank.identifier

import com.marcguilera.bank.identifier.uuid.UUIDIdentifierFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val identifier get() = Kodein.Module("identifier") {
    bind<IdentifierFactory>() with singleton { UUIDIdentifierFactory() }
}