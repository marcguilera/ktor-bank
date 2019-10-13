package com.marcguilera.bank.transfer.test.unit.domain

import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.marcguilera.bank.test.coroutine.assertThat
import com.marcguilera.bank.test.coroutine.catch
import com.marcguilera.bank.test.coroutine.given
import com.marcguilera.bank.test.coroutine.it
import com.marcguilera.bank.test.coroutine.on
import com.marcguilera.bank.transfer.domain.TransferNotFoundException
import com.marcguilera.bank.transfer.domain.default.DefaultTransferFacade
import com.marcguilera.bank.transfer.test.withMocks
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class DefaultTransferFacadeSpec : SubjectSpek<DefaultTransferFacade>({
    withMocks {
        given("a DefaultTransferFacade") {

            subject { DefaultTransferFacade(kodein) }

            on("create") {

                whenever(factory.create(fromAccountId, toAccountId, amount))
                        .doReturn(resource)

                val result = subject.create(fromAccountId, toAccountId, amount)

                it("produces an entity") {
                    assertThat(result)
                            .isEqualTo(entity)
                }
                it("asks the factory to create the account") {
                    verify(factory, times(1))
                            .create(fromAccountId, toAccountId, amount)
                }
                it("saves the account in the repository") {
                    verify(repository, times(1))
                            .save(resource)
                }
            }

            on("getAll") {

                whenever(repository.findAll())
                        .doReturn(listOf(resource))

                val result = subject.getAll()

                it("produces an entity list") {
                    assertThat(result)
                            .isEqualTo(listOf(entity))
                }
            }

            on("getOne - missing") {

                val result = catch { subject.getOne(id) }

                it("throws a TransferNotFoundException") {
                    assertThat(result)
                            .isInstanceOf(TransferNotFoundException::class)
                }
            }

            on("getOne - not missing") {

                whenever(repository.findOne(id))
                        .thenReturn(resource)

                val result = subject.getOne(id)

                it("returns the transfer") {
                    assertThat(result)
                            .isEqualTo(entity)
                }
            }
        }
    }
})