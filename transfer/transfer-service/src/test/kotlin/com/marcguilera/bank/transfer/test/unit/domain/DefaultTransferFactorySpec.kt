package com.marcguilera.bank.transfer.test.unit.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.marcguilera.bank.test.coroutine.catch
import com.marcguilera.bank.test.coroutine.given
import com.marcguilera.bank.test.coroutine.it
import com.marcguilera.bank.test.coroutine.on
import com.marcguilera.bank.transfer.domain.IllegalTransferException
import com.marcguilera.bank.transfer.domain.default.DefaultTransferFactory
import com.marcguilera.bank.transfer.test.withMocks
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class DefaultTransferFactorySpec : SubjectSpek<DefaultTransferFactory>({

    withMocks {
        given("A DefaultTransferFactory") {

            subject { DefaultTransferFactory(kodein) }

            on("create - same account") {

                val result = catch { subject.create(fromAccountId, fromAccountId, amount) }

                it("throws an IllegalTransferException") {
                    result.printStackTrace()
                    assertThat(result)
                            .isInstanceOf(IllegalTransferException::class)
                }
            }

            on("create - different currency") {

                whenever(accountClient.getOne(fromAccountId))
                        .doReturn(fromAccount.copy(currency = "USD"))
                whenever(accountClient.getOne(toAccountId))
                        .doReturn(toAccount)

                val result = catch { subject.create(fromAccountId, toAccountId, amount) }

                it("throws an IllegalTransferException") {
                    assertThat(result)
                            .isInstanceOf(IllegalTransferException::class)
                }
            }

            on ("create - failed deposit") {

                whenever(balanceClient.create(eq(fromAccountId), eq(-amount), any()))
                        .doReturn(fromBalanceItem)
                whenever(balanceClient.create(eq(toAccountId), eq(amount), any()))
                        .doThrow(RuntimeException::class)

                val result = catch { subject.create(fromAccountId, toAccountId, amount) }

                it ("throws the exception") {
                    assertThat(result)
                            .isInstanceOf(RuntimeException::class)
                }
            }

            on("create - all good") {
                whenever(accountClient.getOne(fromAccountId))
                        .doReturn(fromAccount)
                whenever(accountClient.getOne(toAccountId))
                        .doReturn(toAccount)

                whenever(balanceClient.create(eq(fromAccountId), eq(-amount), any()))
                        .doReturn(fromBalanceItem)
                whenever(balanceClient.create(eq(toAccountId), eq(amount), any()))
                        .doReturn(toBalanceItem)

                whenever(identifierFactory.create())
                        .doReturn(id)
                whenever(instantFactory.now())
                        .doReturn(now)

                val result = subject.create(fromAccountId, toAccountId, amount)

                it("produces the resource") {
                    assertThat(result)
                            .isEqualTo(resource)
                }
            }
        }
    }
})