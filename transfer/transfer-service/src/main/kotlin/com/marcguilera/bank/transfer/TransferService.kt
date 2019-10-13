package com.marcguilera.bank.transfer

import com.marcguilera.bank.service.setup
import io.ktor.application.Application

fun main(args: Array<String>): Unit =
        io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    setup(testing) {
        import(transfer)
    }
}
