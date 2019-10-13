package com.marcguilera.bank.account.test.unit.data

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.marcguilera.bank.account.AccountId
import com.marcguilera.bank.account.data.AccountResource
import com.marcguilera.bank.account.data.inmemory.InMemoryAccountRepository
import com.marcguilera.bank.account.test.withMocks
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class InMemoryAccountRepositorySpec : SubjectSpek<InMemoryAccountRepository>({

    withMocks {

        given("an empty InMemoryAccountRepository") {

            val map = mutableMapOf<AccountId, AccountResource>()
            beforeEachTest { map.clear() }

            subject { InMemoryAccountRepository(map) }

            on("findOne from an empty repo") {
                val result = subject.findOne(id)
                it("produces null") {
                    assertThat(result)
                            .isNull()
                }
            }

            on("findAll from an empty repo") {
                val result = subject.findAll()
                it("produces empty") {
                    assertThat(result)
                            .isEmpty()
                }
            }

            on("findOne from a non empty repo") {
                map[id] = resource
                val result = subject.findOne(id)
                it("produces a resource") {
                    assertThat(result)
                            .isEqualTo(resource)
                }
            }

            on("findAll from a non empty repo") {
                map[id] = resource
                val result = subject.findAll()
                it("produces a resource list") {
                    assertThat(result)
                            .contains(resource)
                }
            }

            on("save") {
                subject.save(resource)
                it("stores the resource in the map") {
                    assertThat(map)
                            .contains(id, resource)
                }
            }
        }
    }
})