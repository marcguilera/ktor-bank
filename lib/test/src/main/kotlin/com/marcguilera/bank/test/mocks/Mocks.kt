package com.marcguilera.bank.test.mocks

import com.marcguilera.bank.identifier.IdentifierFactory
import com.marcguilera.bank.time.InstantFactory
import com.nhaarman.mockitokotlin2.mock
import org.jetbrains.spek.api.dsl.Spec
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import java.time.Instant
import com.nhaarman.mockitokotlin2.reset as resetMocks

/**
 * A helper object containing the necessary mocks.
 */
abstract class Mocks {


    /**
     * A [Mocks] factory to create the specific instance of that class.
     */
    interface Factory <T: Mocks> {
        /**
         * Creates a [Mocks] instance of the [T] domain.
         */
        fun create(): T
    }

    var id = "id"
    var now = Instant.EPOCH

    val instantFactory: InstantFactory = mock()
    val identifierFactory: IdentifierFactory = mock()

    open val kodein = Kodein {
        bind() from instance(instantFactory)
        bind() from instance(identifierFactory)
    }

    open fun reset() {
        resetMocks(instantFactory, identifierFactory)
    }
}

/**
 * Creates a [Mocks] instance using it's domain factory.
 */
fun <T : Mocks> Spec.withMocks(factory: Mocks.Factory<T>, mock: T.() -> Unit) =
        factory
                .create()
                .apply { afterEachTest(::reset) }
                .apply(mock)

