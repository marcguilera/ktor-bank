package com.marcguilera.bank.balance.test.unit.data

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.google.common.collect.HashBasedTable.create
import com.marcguilera.bank.balance.data.inmemory.BalanceItemTable
import com.marcguilera.bank.balance.data.inmemory.InMemoryBalanceItemRepository
import com.marcguilera.bank.balance.test.withMocks
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class InMemoryBalanceItemRepositorySpec : SubjectSpek<InMemoryBalanceItemRepository>({
    withMocks {
        given("") {
            val table: BalanceItemTable = create()
            subject { InMemoryBalanceItemRepository(table) }

            afterEachTest {
                table.clear()
            }

            on("save") {
                subject.save(resource)
                it("saves the resource") {
                    assertThat(table.containsValue(resource))
                            .isTrue()
                }
            }

            on("findAll - missing") {
                val result = subject.findAll(accountId)
                it("produces an empty list") {
                    assertThat(result)
                            .isEmpty()
                }
            }

            on("findAll - not missing") {
                table.put(accountId, id, resource)
                val result = subject.findAll(accountId)
                it("produces a resource") {
                    assertThat(result)
                            .contains(resource)
                }
            }

            on("findOne - missing") {
                val result = subject.findOne(accountId, id)
                it("produces null") {
                    assertThat(result)
                            .isNull()
                }
            }

            on("findOne - not missing") {
                table.put(accountId, id, resource)
                val result = subject.findOne(accountId, id)
                it("produces a resource") {
                    assertThat(result)
                            .isEqualTo(resource)
                }
            }

            on("findLast - missing") {
                val result = subject.findOne(accountId, id)
                it("produces an empty list") {
                    assertThat(result)
                            .isNull()
                }
            }

            on("findLast - not missing") {
                table.put(accountId, id, resource)
                val result = subject.findLast(accountId)
                it("produces a resource") {
                    assertThat(result)
                            .isEqualTo(resource)
                }
            }
        }
    }
})