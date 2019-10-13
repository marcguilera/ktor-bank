package com.marcguilera.bank.service

import com.fasterxml.jackson.annotation.JsonInclude
import com.marcguilera.bank.consul.ConsulFeature
import com.marcguilera.bank.error.ErrorFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallId
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.callIdMdc
import io.ktor.features.generate
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpHeaders.Accept
import io.ktor.jackson.jackson
import org.kodein.di.Kodein
import org.kodein.di.ktor.controller.KodeinControllerFeature
import org.kodein.di.ktor.kodein
import org.slf4j.event.Level

fun Application.setup(testing: Boolean, configuration: Kodein.MainBuilder.() -> Unit) {

    install(ErrorFeature)

    install(CallId) {
        generate(10)
    }

    install(ContentNegotiation) {
        jackson {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            findAndRegisterModules()
        }
    }

    install(CallLogging) {
        level = Level.INFO
        callIdMdc("X-Request-ID")
    }

    install(DefaultHeaders) {
        header(Accept, Json.toString())
    }

    kodein {
        import(service(testing))
        configuration()
    }

    install(KodeinControllerFeature)

    if (!testing) {
        install(ConsulFeature)
    }
}

