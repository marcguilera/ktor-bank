package com.marcguilera.bank.balance.test.unit.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory.Flags.Companion.overdraft
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory.Flags.Companion.zero
import com.marcguilera.bank.balance.domain.IllegalAmountException
import com.marcguilera.bank.balance.domain.default.DefaultBalanceItemResourceFactory
import com.marcguilera.bank.balance.test.withMocks
import com.marcguilera.bank.test.coroutine.catch
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class DefaultBalanceItemResourceFactorySpec : SubjectSpek<DefaultBalanceItemResourceFactory>({
    withMocks {
        given("a DefaultBalanceItemResourceFactory") {

            subject { DefaultBalanceItemResourceFactory(kodein) }

            beforeEachTest {
                whenever(identifierFactory.create())
                        .thenReturn(id)
                whenever(instantFactory.now())
                        .thenReturn(now)
            }

            on("create - with valid arguments") {
                val result = subject.create(accountId, amount, summary)
                it ("produces the balance item") {
                    assertThat(result)
                            .isEqualTo(resource)
                }
                it("asks the id factory to create an id") {
                    verify(identifierFactory, times(1)).create()
                }
            }

            on("create - with insufficient funds not allowed") {
                val result = catch { subject.create(accountId, -amount + 1) }
                it ("throws a IllegalAmountException") {
                    assertThat(result)
                            .isNotNull()
                            .isInstanceOf(IllegalAmountException::class)
                }
            }

            on("create - with insufficient funds allowed") {
                val result = subject.create(accountId, -amount, summary, overdraft())
                it ("produces the balance item") {
                    assertThat(result)
                            .isEqualTo(resource.copy(amount = -amount, total = -total))
                }
            }

            on("create - with zero not allowed") {
                val result = catch { subject.create(accountId, 0) }
                it ("throws a IllegalAmountException") {
                    assertThat(result)
                            .isNotNull()
                            .isInstanceOf(IllegalAmountException::class)
                }
            }

            on("create - with zero allowed") {
                val result = subject.create(accountId, 0, summary, zero())
                it ("produces the balance item") {
                    assertThat(result)
                            .isEqualTo(resource.copy(amount = 0, total = 0))
                }
            }
        }
    }
})