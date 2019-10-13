package com.marcguilera.bank.time

import java.time.Instant

interface InstantFactory {
    fun now(): Instant
}