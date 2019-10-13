package com.marcguilera.bank.identifier

/**
 * Represents a unique identifier.
 */
typealias Identifier = String

/**
 * A simple way to get unique identifiers.
 */
interface IdentifierFactory {
    /**
     * Creates a unique identifier.
     */
    fun create(): Identifier
}