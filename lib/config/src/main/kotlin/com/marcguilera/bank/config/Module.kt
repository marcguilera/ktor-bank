package com.marcguilera.bank.config

import io.ktor.application.Application
import org.kodein.di.Kodein
import org.kodein.di.generic.with

val Application.config get() = Kodein.Module("config") {
    constant(ConfigKeys.APP_NAME) with getProperty("ktor.application.name")
    constant(ConfigKeys.APP_PORT) with getProperty("ktor.deployment.port")
    constant(ConfigKeys.CONSUL_URL) with getProperty("ktor.consul.url")
}

private fun Application.getProperty(name: String)
        = environment.config.propertyOrNull(name)?.getString() ?: ""