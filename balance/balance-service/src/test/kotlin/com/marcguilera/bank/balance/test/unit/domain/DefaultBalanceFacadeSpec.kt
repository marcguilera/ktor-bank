package com.marcguilera.bank.balance.test.unit.domain

import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.marcguilera.bank.balance.domain.BalanceNotFoundException
import com.marcguilera.bank.balance.domain.default.DefaultBalanceFacade
import com.marcguilera.bank.balance.test.withMocks
import com.marcguilera.bank.test.coroutine.assertThat
import com.marcguilera.bank.test.coroutine.catch
import com.marcguilera.bank.test.coroutine.given
import com.marcguilera.bank.test.coroutine.it
import com.marcguilera.bank.test.coroutine.on
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class DefaultBalanceFacadeSpec : SubjectSpek<DefaultBalanceFacade>({
    withMocks {
        given("a DefaultBalanceFacade") {

            subject { DefaultBalanceFacade(kodein) }

            on("create") {
                whenever(factory.create(accountId, amount, summary))
                        .thenReturn(resource)

                val result = subject.create(accountId, amount, summary)

                it ("produces the balance item") {
                    assertThat(result)
                            .isEqualTo(entity)
                }
                it("asks the factory to create the resource") {
                    verify(factory, times(1)).create(accountId, amount, summary)
                }
                it("saves the resource in the repository") {
                    verify(repository, times(1)).save(resource)
                }
            }

            on("revert") {
                whenever(revertFactory.create(accountId, id))
                        .thenReturn(resource)
                val result = subject.revert(accountId, id)
                it ("produces the balance item") {
                    assertThat(result)
                            .isEqualTo(entity)
                }
                it("asks the factory to create the revert resource") {
                    verify(revertFactory, times(1)).create(accountId, id)
                }
                it("saves the resource in the repository") {
                    verify(repository, times(1)).save(resource)
                }
            }

            on("getAll - missing") {
                whenever(repository.findAll(accountId))
                        .thenReturn(listOf())

                val result = catch { subject.getAll(accountId) }
                it ("thows a BalanceNotFoundException") {
                    assertThat(result)
                            .isNotNull()
                            .isInstanceOf(BalanceNotFoundException::class)
                }
            }

            on("getAll - not missing") {
                whenever(repository.findAll(accountId))
                        .thenReturn(listOf(resource))

                val result = subject.getAll(accountId)
                it ("produces the list of entities") {
                    assertThat(result)
                            .contains(entity)
                }
            }

            on("getOne - missing") {
                it ("thows a BalanceNotFoundException") {
                    assertThat { subject.getOne(accountId, id) }
                            .isFailure()
                            .isInstanceOf(BalanceNotFoundException::class)
                }
            }

            on("getAll - not missing") {
                whenever(repository.findOne(accountId, id))
                        .thenReturn(resource)

                val result = subject.getOne(accountId, id)

                it ("produces an entity") {
                    assertThat(result)
                            .isEqualTo(entity)
                }
            }

            on("getLast - missing") {

                val result = catch { subject.getLast(accountId) }

                it ("throws a BalanceNotFoundException") {
                    assertThat(result)
                            .isNotNull()
                            .isInstanceOf(BalanceNotFoundException::class)
                }
            }

            on("getLast - not missing") {
                whenever(repository.findLast(accountId))
                        .thenReturn(resource)

                val result = subject.getLast(accountId)

                it ("produces an entity") {
                    assertThat(result)
                            .isEqualTo(entity)
                }
            }
        }
    }
})