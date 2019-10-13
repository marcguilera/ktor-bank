package com.marcguilera.bank.balance.test.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.marcguilera.bank.balance.BALANCES_ENDPOINT
import com.marcguilera.bank.balance.BALANCE_ENDPOINT
import com.marcguilera.bank.balance.LAST_BALANCE_ENDPOINT
import com.marcguilera.bank.balance.test.withMocks
import com.marcguilera.bank.service.setup
import com.marcguilera.bank.test.coroutine.given
import com.marcguilera.bank.test.coroutine.it
import com.marcguilera.bank.test.coroutine.on
import com.marcguilera.bank.test.json.isJsonEqualTo
import com.marcguilera.bank.test.json.toJSON
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.application.Application
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpMethod.Companion.Delete
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.jetbrains.spek.api.Spek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import com.marcguilera.bank.balance.balance as balanceModule

@RunWith(JUnitPlatform::class)
class BalanceServiceSpec : Spek({
    withMocks {

        val balancesEndpoint = BALANCES_ENDPOINT
                .replace("{accountId}", accountId)
        val lastBalanceEndpoint = LAST_BALANCE_ENDPOINT
                .replace("{accountId}", accountId)
        val balanceEndpoint = BALANCE_ENDPOINT
                .replace("{accountId}", accountId)
                .replace("{balanceId}", id)

        fun Application.testModule() = setup(true) {
            import(balanceModule)
            bind(overrides = true) from instance(identifierFactory)
            bind(overrides = true) from instance(instantFactory)
            bind(overrides = true) from instance(repository)
        }

        given("a BalanceService") {
            on("GET: $BALANCES_ENDPOINT - not missing") {
                whenever(repository.findAll(accountId))
                        .thenReturn(listOf(resource))
                it("returns a list of balances") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = balancesEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(listOf(balance))
                        }
                    }
                }
            }
            on("GET: $BALANCES_ENDPOINT - missing") {
                it("returns a 404") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = balancesEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(NotFound)
                        }
                    }
                }
            }
            on("GET: $BALANCE_ENDPOINT - not missing") {
                whenever(repository.findOne(accountId, id))
                        .thenReturn(resource)
                it("returns a balance") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = balanceEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(balance)
                        }
                    }
                }
            }
            on("GET: $BALANCE_ENDPOINT - missing") {
                it("returns a 404") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = balanceEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(NotFound)
                        }
                    }
                }
            }
            on("GET: $LAST_BALANCE_ENDPOINT - not missing") {
                whenever(repository.findLast(accountId))
                        .thenReturn(resource)
                it("returns a balance") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = lastBalanceEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(balance)
                        }
                    }
                }
            }
            on("GET: $LAST_BALANCE_ENDPOINT - missing") {
                it("returns a 404") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = lastBalanceEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(NotFound)
                        }
                    }
                }
            }
            on("POST: $BALANCE_ENDPOINT - sufficient funds") {
                whenever(identifierFactory.create())
                        .doReturn(id)
                whenever(instantFactory.now())
                        .doReturn(now)
                it("returns a balance") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Post
                            uri = balancesEndpoint
                            addHeader(ContentType, Json.toString())
                            setBody(info.toJSON())
                        }.apply {
                            assertThat(response.status()).isEqualTo(Created)
                            assertThat(response.content).isJsonEqualTo(balance)
                        }
                    }
                }
            }
            on("POST: $BALANCE_ENDPOINT - insufficient funds") {
                whenever(identifierFactory.create())
                        .doReturn(id)
                whenever(instantFactory.now())
                        .doReturn(now)
                it("returns a balance") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Post
                            uri = balancesEndpoint
                            addHeader(ContentType, Json.toString())
                            setBody(info.copy(amount = -amount).toJSON())
                        }.apply {
                            assertThat(response.status()).isEqualTo(BadRequest)
                        }
                    }
                }
            }
            on("DELETE: $BALANCE_ENDPOINT - not missing") {
                whenever(identifierFactory.create())
                        .doReturn(id)
                whenever(instantFactory.now())
                        .doReturn(now)
                whenever(repository.findOne(accountId, id))
                        .thenReturn(resource)
                it("returns a balance") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Delete
                            uri = balanceEndpoint
                        }.apply {
                            val deleted = balance.copy(
                                    amount = -amount,
                                    total = -amount,
                                    summary = "[REVERT] $id $summary"
                            )
                            assertThat(response.status()).isEqualTo(Accepted)
                            assertThat(response.content).isJsonEqualTo(deleted)
                        }
                    }
                }
            }

            on("DELETE: $BALANCE_ENDPOINT - missing") {
                whenever(identifierFactory.create())
                        .doReturn(id)
                whenever(instantFactory.now())
                        .doReturn(now)
                it("returns a 404") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Delete
                            uri = balanceEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(NotFound)
                        }
                    }
                }
            }
        }
    }
})