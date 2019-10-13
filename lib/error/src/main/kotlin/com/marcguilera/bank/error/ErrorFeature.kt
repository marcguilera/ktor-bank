package com.marcguilera.bank.error

import io.ktor.application.Application
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.request.path
import io.ktor.response.respond
import io.ktor.util.AttributeKey
import mu.KLogging
import java.time.Instant

/**
 * A feature which allows to automatically send an error response on exceptions.
 */
class ErrorFeature private constructor() {

    companion object Feature : KLogging(), ApplicationFeature<Application, Unit, ErrorFeature> {
        override val key: AttributeKey<ErrorFeature> = AttributeKey("ErrorFeature")

        override fun install(pipeline: Application, configure: Unit.() -> Unit): ErrorFeature {
            Unit.configure()

            pipeline.install(StatusPages) {

                exception<Throwable> {
                    logger.error(it) { "Caught exception: ${it.message}" }
                    val status = if (it is StatusCodeException) it.statusCode else InternalServerError
                    call.respond(status, ErrorResponse(status, it.message, call.request.path(), Instant.now()))
                }
            }

            return ErrorFeature()
        }

    }
}