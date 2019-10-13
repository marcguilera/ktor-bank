package com.marcguilera.bank.consul

import com.marcguilera.bank.config.ConfigKeys
import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration
import io.ktor.application.Application
import io.ktor.application.ApplicationFeature
import io.ktor.application.ApplicationStarted
import io.ktor.util.AttributeKey
import mu.KLogging
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

/**
 * A feature which grabs registers this service into Consul
 * and allows it to send requests to other services in the grid.
 * It's opinionated about where the configuration lives, for more configurability
 * use the client feature directly.
 */
class ConsulFeature private constructor() {

    companion object Feature : KLogging(), ApplicationFeature<Application, Unit, ConsulFeature> {

        override val key = AttributeKey<ConsulFeature>("ConsulFeature")

        override fun install(pipeline: Application, configure: Unit.() -> Unit): ConsulFeature {
            pipeline.setUpAgent()
            return ConsulFeature()
        }

        private fun Application.setUpAgent() {
            environment.monitor.subscribe(ApplicationStarted) {
                val n: String by kodein().instance(ConfigKeys.APP_NAME)
                val p: String by kodein().instance(ConfigKeys.APP_PORT)
                val u: String by kodein().instance(ConfigKeys.CONSUL_URL)
                logger.info { "Registering the service in consul with $n:$p at $u" }

                val consulClient = Consul
                        .builder()
                        .withUrl(u)
                        .build()

                val service = ImmutableRegistration
                        .builder()
                        .id(n)
                        .name(n)
                        .address(n)
                        .port(p.toInt())
                        .build()

                consulClient
                        .agentClient()
                        .register(service)
            }
        }
    }
}