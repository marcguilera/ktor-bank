package com.marcguilera.bank.time.default

import com.marcguilera.bank.time.InstantFactory
import java.time.Instant

class DefaultInstantFactory : InstantFactory {
    override fun now(): Instant = Instant.now()
}