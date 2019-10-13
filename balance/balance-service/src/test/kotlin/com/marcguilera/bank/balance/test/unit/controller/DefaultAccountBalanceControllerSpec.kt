package com.marcguilera.bank.balance.test.unit.controller

import assertk.assertions.isEqualTo
import com.marcguilera.bank.balance.controller.default.DefaultAccountBalanceController
import com.marcguilera.bank.balance.test.withMocks
import com.marcguilera.bank.test.coroutine.assertThat
import com.marcguilera.bank.test.coroutine.given
import com.marcguilera.bank.test.coroutine.it
import com.marcguilera.bank.test.coroutine.on
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class DefaultAccountBalanceControllerSpec : SubjectSpek<DefaultAccountBalanceController>({
    withMocks {
        given("A DefaultAccountBalanceController") {
            subject { DefaultAccountBalanceController(kodein) }

            on("create") {
                whenever(facade.create(accountId, amount, summary))
                        .thenReturn(entity)

                val result = subject.create(accountId, info)

                it("produces a response") {
                    assertThat(result)
                            .isEqualTo(balance)
                }
            }

            on("getAll") {
                whenever(facade.getAll(accountId))
                        .thenReturn(listOf(entity))

                val result = subject.getAll(accountId)

                it("produces a response") {
                    assertThat(result)
                            .isEqualTo(listOf(balance))
                }
            }

            on("delete") {
                whenever(facade.revert(accountId, id))
                        .thenReturn(entity)
                val result = subject.delete(accountId, id)

                it("produces a response") {
                    assertThat(result)
                            .isEqualTo(balance)
                }
            }

            on("getOne") {
                whenever(facade.getOne(accountId, id))
                        .thenReturn(entity)
                val result =  subject.getOne(accountId, id)

                it("produces a response") {
                    assertThat(result)
                            .isEqualTo(balance)
                }
            }

            on("getLast") {
                whenever(facade.getLast(accountId))
                        .thenReturn(entity)
                val result = subject.getLast(accountId)

                it("produces a response") {
                    assertThat(result)
                            .isEqualTo(balance)
                }
            }
        }
    }
})