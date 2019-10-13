package com.marcguilera.bank.account.test.unit.controller

import assertk.assertions.isEqualTo
import com.marcguilera.bank.account.controller.default.DefaultAccountController
import com.marcguilera.bank.account.test.withMocks
import com.marcguilera.bank.test.coroutine.assertThat
import com.marcguilera.bank.test.coroutine.on
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class DefaultAccountControllerSpec : SubjectSpek<DefaultAccountController>({

    withMocks {

        given("a DefaultAccountController") {

            subject { DefaultAccountController(kodein) }

            on("create") {
                whenever(facade.create(currency))
                        .thenReturn(entity)
                val result = subject.create(info)
                it("Produces an Account") {
                    assertThat(result)
                            .isEqualTo(account)
                }
            }

            on("getOne") {
                whenever(facade.getOne(id))
                        .thenReturn(entity)
                val result = subject.getOne(id)
                it("Produces an Account") {
                    assertThat(result)
                            .isEqualTo(account)
                }
            }

            on("getAll") {
                whenever(facade.getAll())
                        .thenReturn(listOf(entity))
                val result = subject.getAll()
                it("produces an Account list") {
                    assertThat(result)
                            .isEqualTo(listOf(account))
                }
            }
        }
    }
})