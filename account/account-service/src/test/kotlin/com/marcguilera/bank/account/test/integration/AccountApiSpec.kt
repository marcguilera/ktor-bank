package com.marcguilera.bank.account.test.integration

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.marcguilera.bank.account.ACCOUNTS_ENDPOINT
import com.marcguilera.bank.account.ACCOUNT_ENDPOINT
import com.marcguilera.bank.account.Account
import com.marcguilera.bank.account.AccountInfo
import com.marcguilera.bank.account.test.withMocks
import com.marcguilera.bank.service.setup
import com.marcguilera.bank.test.json.isJsonEqualTo
import com.marcguilera.bank.test.json.toJSON
import com.nhaarman.mockitokotlin2.whenever
import io.ktor.application.Application
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import com.marcguilera.bank.account.account as accountModule

@RunWith(JUnitPlatform::class)
class AccountApiSpec : Spek({

    withMocks {

        val accountEndpoint = ACCOUNT_ENDPOINT
                .replace("{accountId}", id)

        // We only want to mock data access stuff...
        fun Application.testModule() = setup(true) {
            import(accountModule)
            bind(overrides = true) from instance(identifierFactory)
            bind(overrides = true) from instance(repository)
        }

        given("an AccountService") {

            on("GET: $ACCOUNTS_ENDPOINT - not empty") {
                whenever(repository.findAll())
                        .thenReturn(listOf(resource))
                it("returns all accounts") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = ACCOUNTS_ENDPOINT
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(listOf(account))
                        }
                    }
                }
            }

            on("GET: $ACCOUNTS_ENDPOINT - empty") {
                it("returns empty array") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = ACCOUNTS_ENDPOINT
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(listOf<Account>())
                        }
                    }
                }
            }

            on("POST: $ACCOUNTS_ENDPOINT - OK") {
                whenever(identifierFactory.create())
                        .thenReturn(id)
                it("creates an account") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Post
                            uri = ACCOUNTS_ENDPOINT
                            addHeader(ContentType, Json.toString())
                            setBody(info.toJSON())
                        }.apply {
                            assertThat(response.status()).isEqualTo(Created)
                            assertThat(response.content).isJsonEqualTo(account)
                        }
                    }
                }
            }

            on("POST: $ACCOUNTS_ENDPOINT - Invalid currency") {
                whenever(identifierFactory.create())
                        .thenReturn(id)
                val info = AccountInfo.DTO("invalid")
                it("returns a 400") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Post
                            uri = ACCOUNTS_ENDPOINT
                            addHeader(ContentType, Json.toString())
                            setBody(info.toJSON())
                        }.apply {
                            assertThat(response.status()).isEqualTo(BadRequest)
                        }
                    }
                }
            }

            on("GET: $ACCOUNT_ENDPOINT - not empty") {
                whenever(repository.findOne(id))
                        .thenReturn(resource)
                it("gets an account") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = accountEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(OK)
                            assertThat(response.content).isJsonEqualTo(account)
                        }
                    }
                }
            }

            on("GET: $ACCOUNT_ENDPOINT - empty") {
                it("returns a 404") {
                    withTestApplication({ testModule() }) {
                        handleRequest {
                            method = Get
                            uri = accountEndpoint
                        }.apply {
                            assertThat(response.status()).isEqualTo(NotFound)
                        }
                    }
                }
            }
        }
    }
})