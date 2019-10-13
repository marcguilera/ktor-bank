package com.marcguilera.bank.transfer.test.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.marcguilera.bank.service.setup
import com.marcguilera.bank.test.coroutine.on
import com.marcguilera.bank.test.json.isJsonEqualTo
import com.marcguilera.bank.test.json.toJSON
import com.marcguilera.bank.transfer.TRANSFERS_ENDPOINT
import com.marcguilera.bank.transfer.TRANSFER_ENDPOINT
import com.marcguilera.bank.transfer.Transfer
import com.marcguilera.bank.transfer.test.withMocks
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.application.Application
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import com.marcguilera.bank.transfer.transfer as transferModule

@RunWith(JUnitPlatform::class)
class TransferApiSpec : Spek({

    withMocks {

        val transferEndpoint = TRANSFER_ENDPOINT
                .replace("{transferId}", id)

        // We only want to mock data access stuff...
        fun Application.testModule() = setup(true) {
            import(transferModule)
            bind(overrides = true) from instance(identifierFactory)
            bind(overrides = true) from instance(repository)
            bind(overrides = true) from instance(accountClient)
            bind(overrides = true) from instance(balanceClient)
        }

        given("a TransferService") {

            on("GET: $TRANSFERS_ENDPOINT - not empty") {
                whenever(repository.findAll())
                        .thenReturn(listOf(resource))
                it("returns all accounts") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = TRANSFERS_ENDPOINT
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(listOf(transfer))
                        }
                    }
                }
            }

            on("GET: $TRANSFERS_ENDPOINT - empty") {
                it("returns empty array") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = TRANSFERS_ENDPOINT
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(listOf<Transfer>())
                        }
                    }
                }
            }

            on("POST: $TRANSFERS_ENDPOINT - OK") {
                whenever(identifierFactory.create())
                        .thenReturn(id)
                whenever(accountClient.getOne(fromAccountId))
                        .thenReturn(fromAccount)
                whenever(accountClient.getOne(toAccountId))
                        .thenReturn(toAccount)
                whenever(balanceClient.create(eq(fromAccountId), eq(-amount), any()))
                        .thenReturn(fromBalanceItem)
                whenever(balanceClient.create(eq(toAccountId), eq(amount), any()))
                        .thenReturn(toBalanceItem)

                it("creates an account") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Post
                            uri = TRANSFERS_ENDPOINT
                            addHeader(ContentType, Json.toString())
                            setBody(info.toJSON())
                        }.apply {
                            assertThat(response.status()).isEqualTo(Created)
                            assertThat(response.content).isJsonEqualTo(transfer)
                        }
                    }
                }
            }


            on("GET: $TRANSFER_ENDPOINT - not empty") {
                whenever(repository.findOne(id))
                        .thenReturn(resource)
                it("gets an account") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = transferEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(transfer)
                        }
                    }
                }
            }

            on("GET: $TRANSFER_ENDPOINT - empty") {
                it("returns a 404") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = transferEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(NotFound)
                        }
                    }
                }
            }
        }
    }
})