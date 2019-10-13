package com.marcguilera.bank.consul

import com.orbitz.consul.Consul
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.AttributeKey
import mu.KLogging

/**
 * A feature which registers this service into consul and allows it to access
 * other services in the grid.
 */
class ConsulClientFeature private constructor (
        private val url: String
) {

    class Configuration {
        /**
         * The url where consul is listening.
         */
        var url: String = "http://consul:8500"
    }

    companion object Feature : KLogging(), HttpClientFeature<Configuration, ConsulClientFeature> {

        override val key = AttributeKey<ConsulClientFeature>("ConsulClientFeature")

        override fun prepare(block: Configuration.() -> Unit): ConsulClientFeature
                = ConsulClientFeature(Configuration().apply(block).url)

        override fun install(feature: ConsulClientFeature, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Render) {
                val consulClient = Consul.builder().withUrl(feature.url).build()
                val nodes = consulClient.healthClient().getHealthyServiceInstances(context.url.host).response
                val selectedNode = nodes.random()
                context.url.host = selectedNode.service.address
                context.url.port = selectedNode.service.port
                logger.info { "Calling consul discovered ${selectedNode.service.id}: ${context.url.buildString()}" }
            }
        }
    }
}