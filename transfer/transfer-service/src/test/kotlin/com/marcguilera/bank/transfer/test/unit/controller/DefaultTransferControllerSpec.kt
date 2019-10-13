package com.marcguilera.bank.transfer.test.unit.controller

import assertk.assertions.isEqualTo
import com.marcguilera.bank.test.coroutine.assertThat
import com.marcguilera.bank.test.coroutine.given
import com.marcguilera.bank.test.coroutine.it
import com.marcguilera.bank.test.coroutine.on
import com.marcguilera.bank.transfer.controller.default.DefaultTransferController
import com.marcguilera.bank.transfer.test.withMocks
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class DefaultTransferControllerSpec : SubjectSpek<DefaultTransferController>({
    withMocks {
        given("a DefaultTransferController") {
            subject { DefaultTransferController(kodein) }

            on("create") {
                whenever(facade.create(fromAccountId, toAccountId, amount))
                        .thenReturn(entity)

                val result = subject.create(info)

                it("produces a response") {
                    assertThat(result)
                            .isEqualTo(transfer)
                }
            }

            on("getAll") {
                whenever(facade.getAll())
                        .thenReturn(listOf(entity))

                val result = subject.getAll()

                it("produces a response") {
                    assertThat(result)
                            .isEqualTo(listOf(transfer))
                }
            }

            on("getOne") {
                whenever(facade.getOne(id))
                        .thenReturn(entity)

                val result =  subject.getOne(id)

                it("produces a response") {
                    assertThat(result)
                            .isEqualTo(transfer)
                }
            }
        }
    }
})