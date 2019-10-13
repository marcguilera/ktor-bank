package com.marcguilera.bank.balance.test.unit.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.marcguilera.bank.balance.domain.BalanceItemResourceFactory.Flags.Companion.overdraft
import com.marcguilera.bank.balance.domain.BalanceNotFoundException
import com.marcguilera.bank.balance.domain.default.DefaultRevertBalanceItemResourceFactory
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
class DefaultRevertBalanceItemResourceFactorySpec : SubjectSpek<DefaultRevertBalanceItemResourceFactory>({
    withMocks {
        given("a DefaultRevertBalanceItemResourceFactory") {

            subject { DefaultRevertBalanceItemResourceFactory(kodein) }

            on("create - missing") {
                val result = catch { subject.create(accountId, id) }
                it ("throws a BalanceNotFoundException") {
                    assertThat(result)
                            .isNotNull()
                            .isInstanceOf(BalanceNotFoundException::class)
                }
            }

            on("create - not missing") {
                whenever(repository.findOne(accountId, id))
                        .thenReturn(resource)
                whenever(factory.create(accountId, -amount, "[REVERT] $id $summary", overdraft()))
                        .thenReturn(resource)
                val result = subject.create(accountId, id)
                it("products a balance item") {
                    assertThat(result)
                            .isEqualTo(resource)
                }
                it("asks the factory to create a revert resource") {
                    verify(factory, times(1)).create(accountId, -amount, "[REVERT] $id $summary", overdraft())
                }
            }
        }
    }
})