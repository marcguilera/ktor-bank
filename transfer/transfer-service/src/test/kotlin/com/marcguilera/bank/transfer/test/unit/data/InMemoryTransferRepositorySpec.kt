package com.marcguilera.bank.transfer.test.unit.data

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.marcguilera.bank.transfer.TransferId
import com.marcguilera.bank.transfer.data.TransferResource
import com.marcguilera.bank.transfer.data.inmemory.InMemoryTransferRepository
import com.marcguilera.bank.transfer.test.withMocks
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith


@RunWith(JUnitPlatform::class)
class InMemoryTransferRepositorySpec : SubjectSpek<InMemoryTransferRepository>({

    withMocks {

        given("an InMemoryTransferRepository") {

            val map = mutableMapOf<TransferId, TransferResource>()
            beforeEachTest { map.clear() }

            subject { InMemoryTransferRepository(map) }

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