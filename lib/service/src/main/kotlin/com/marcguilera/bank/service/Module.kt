package com.marcguilera.bank.service

import com.marcguilera.bank.config.config
import com.marcguilera.bank.consul.ConsulClientFeature
import com.marcguilera.bank.identifier.identifier
import com.marcguilera.bank.time.time
import io.ktor.application.Application
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun Application.service(test: Boolean) = Kodein.Module("service") {
    import(identifier)
    import(time)
    import(config)
    bind<HttpClient>() with singleton { createHttpClient(!test) }
}

private fun createHttpClient(consul: Boolean) = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = JacksonSerializer {
            findAndRegisterModules()
        }
    }
    if (consul) {
        install(ConsulClientFeature)
    }
}