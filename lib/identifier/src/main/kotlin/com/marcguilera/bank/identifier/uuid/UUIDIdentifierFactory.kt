package com.marcguilera.bank.identifier.uuid

import com.marcguilera.bank.identifier.IdentifierFactory
import java.util.*

class UUIDIdentifierFactory : IdentifierFactory {
    override fun create() = UUID.randomUUID().toString()
}