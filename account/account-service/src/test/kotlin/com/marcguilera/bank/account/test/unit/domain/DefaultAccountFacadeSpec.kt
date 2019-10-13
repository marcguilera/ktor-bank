package com.marcguilera.bank.account.test.unit.domain

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.marcguilera.bank.account.domain.AccountNotFoundException
import com.marcguilera.bank.account.domain.IllegalCurrencyException
import com.marcguilera.bank.account.domain.default.DefaultAccountFacade
import com.marcguilera.bank.account.test.withMocks
import com.marcguilera.bank.test.coroutine.catch
import com.marcguilera.bank.test.coroutine.given
import com.marcguilera.bank.test.coroutine.it
import com.marcguilera.bank.test.coroutine.on
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class DefaultAccountFacadeSpec : SubjectSpek<DefaultAccountFacade>({
    withMocks {

        given("a DefaultAccountFacade") {

            subject { DefaultAccountFacade(kodein) }

            on("create with legal arguments") {
                whenever(identifierFactory.create())
                        .thenReturn(id)
                whenever(instantFactory.now())
                        .thenReturn(now)
                val result = subject.create(currency)
                it("produces an entity") {
                    assertThat(result)
                            .isEqualTo(entity)
                }
                it("saves the resource in the repository") {
                    verify(repository, times(1)).save(resource)
                }
                it("creates a new identifier") {
                    verify(identifierFactory, times(1)).create()
                }
            }

            on("create with illegal currency") {
                it("throws an IllegalCurrencyException") {
                    assertThat { subject.create("invalid") }
                            .isFailure()
                            .isInstanceOf(IllegalCurrencyException::class)
                }
                it("doesn't interact with the repository") {
                    verify(repository, never()).save(any())
                }
            }

            on("getOne with exiting id") {
                whenever(repository.findOne(id))
                        .thenReturn(resource)
                val result = subject.getOne(id)
                it("produces an entity") {
                    assertThat(result)
                            .isEqualTo(entity)
                }
                it("asks the repository for the resource") {
                    verify(repository, times(1)).findOne(id)
                }
            }

            on("getOne with missing id") {
                val id = "missing"
                val result = catch { subject.getOne(id) }
                it("throws an AccountNotFoundException") {
                    assertThat(result)
                            .isNotNull()
                            .isInstanceOf(AccountNotFoundException::class)
                }
                it("asks the repository for the resource") {
                    verify(repository, times(1)).findOne(id)
                }
            }

            on("getAll with existing id") {
                whenever(repository.findAll())
                        .thenReturn(listOf(resource))
                val result = subject.getAll()
                it("produces an entity list") {
                    assertThat(result)
                            .contains(entity)
                }
                it("asks the repository for the resources") {
                    verify(repository, times(1)).findAll()
                }
            }
        }
    }
})